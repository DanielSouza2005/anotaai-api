package anota.ai.api.domain.backup.service;

import anota.ai.api.domain.backup.model.*;
import anota.ai.api.domain.backup.repository.BackupLogRepository;
import anota.ai.api.domain.backup.repository.BackupRepository;
import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.supabase.provider.SupabaseStorageService;
import anota.ai.api.infra.dbinfo.DbInfoExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

@Service
public class BackupService {
    @Autowired
    private BackupRepository backupRepo;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private BackupLogRepository backupLogRepo;

    @Autowired
    private DbInfoExtractor dbInfoExtractor;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private static final String BACKUP_DIR = "backups";
    private static final String SUPABASE_BACKUP_DIR = "backup";

    public Backup gerarBackup(BackupConfig config) throws IOException, URISyntaxException, InterruptedException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Formato formato = config.getFormato();

        Path dir = Paths.get(BACKUP_DIR);
        Files.createDirectories(dir);

        String fileName = "backup_" + timestamp + "." + formato.name().toLowerCase();
        Path filePath = dir.resolve(fileName);

        try {
            switch (formato) {
                case SQL -> {
                    gerarSqlBackupPostgres(filePath, config);
                }
            }

            String publicUrl = supabaseStorageService.uploadFile(filePath.toFile(), SUPABASE_BACKUP_DIR);
            Files.deleteIfExists(filePath);

            Backup backup = new Backup();
            backup.setCaminho_arquivo(publicUrl);
            backup.setFormato(config.getFormato());
            backup.setDtInclusao(new Date());
            backup.setDt_alteracao(new Date());

            registrarLog(config, TipoBackup.GERACAO, "Backup gerado e salvo com sucesso no Banco de Dados.", StatusAtivo.ATIVO);
            return backupRepo.save(backup);
        } catch (Exception e) {
            registrarLog(config, TipoBackup.GERACAO, "Erro ao gerar backup: " + e.getMessage(), StatusAtivo.INATIVO);
            throw e;
        }
    }

    private void gerarSqlBackupPostgres(Path path, BackupConfig config) throws IOException, InterruptedException, URISyntaxException {
        Date inicio = new Date();
        BackupLog log = new BackupLog();
        log.setBackupConfig(config);
        log.setTipoOperacao(TipoBackup.GERACAO.getCodigo());
        log.setDtInicio(inicio);

        try {
            String host = dbInfoExtractor.getHost();
            String port = dbInfoExtractor.getPort();
            String dbName = dbInfoExtractor.getDbName();

            String pgDumpPath = localizarPgDump();
            if (pgDumpPath == null) {
                log.setSucesso(StatusAtivo.INATIVO.getCodigo());
                log.setMensagem("Arquivo pg_dump não encontrado! Verifique se o PostgreSQL está instalado corretamente.");
                throw new IOException("pg_dump não encontrado! Verifique se o PostgreSQL está instalado.");
            }

            String comando = String.format(
                    "\"%s\" -h %s -p %s -U %s -F c -b -v -f \"%s\" %s",
                    pgDumpPath, host, port, dbUser, path.toAbsolutePath(), dbName
            );

            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd.exe", "/c", comando);
            } else {
                pb = new ProcessBuilder("bash", "-c", comando);
            }

            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", dbPassword);

            pb.redirectErrorStream(true);
            Process processo = pb.start();

            StringBuilder output = new StringBuilder();
            try (var reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(processo.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = processo.waitFor();

            if (exitCode != 0) {
                log.setSucesso(StatusAtivo.INATIVO.getCodigo());
                log.setMensagem("Erro ao gerar backup SQL. Código: " + exitCode + "\nSaída:\n" + output);
                throw new IOException("Erro ao gerar backup SQL. Código de saída: " + exitCode);
            }

            log.setSucesso(StatusAtivo.ATIVO.getCodigo());
            log.setMensagem("Backup SQL gerado com sucesso pelo pg_dump.\nSaída:\n" + output);

        } catch (Exception e) {
            log.setSucesso(StatusAtivo.INATIVO.getCodigo());
            log.setMensagem("Falha no pg_dump: " + e.getMessage());
            throw e;
        } finally {
            log.setDtFim(new Date());
            backupLogRepo.save(log);
        }
    }

    private String localizarPgDump() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder pb = new ProcessBuilder(os.contains("win") ? "where" : "which", "pg_dump");
            Process process = pb.start();
            process.waitFor();

            if (process.exitValue() == 0) {
                byte[] output = process.getInputStream().readAllBytes();
                String path = new String(output).trim();
                if (!path.isEmpty()) return path;
            }
        } catch (Exception ignored) {
        }

        if (os.contains("win")) {
            String[] caminhos = {
                    "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe",
                    "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe",
                    "C:\\Program Files\\PostgreSQL\\15\\bin\\pg_dump.exe",
                    "C:\\Program Files\\PostgreSQL\\14\\bin\\pg_dump.exe",
            };
            for (String c : caminhos) {
                if (new File(c).exists()) return c;
            }
        } else {
            String[] caminhos = {"/usr/bin/pg_dump", "/usr/local/bin/pg_dump"};
            for (String c : caminhos) {
                if (new File(c).exists()) return c;
            }
        }

        return null;
    }

    public void excluirBackupAntigo(Backup backup) {
        Date inicio = new Date();
        BackupLog log = new BackupLog();
        log.setTipoOperacao(TipoBackup.LIMPEZA.getCodigo());
        log.setDtInicio(inicio);

        try {
            supabaseStorageService.deleteFileByUrl(backup.getCaminho_arquivo());
            backupRepo.delete(backup);

            log.setSucesso(StatusAtivo.ATIVO.getCodigo());
            log.setMensagem("Backup excluído com sucesso.");
        } catch (Exception e) {
            log.setSucesso(StatusAtivo.INATIVO.getCodigo());
            log.setMensagem("Erro ao excluir backup antigo: " + e.getMessage());
        } finally {
            log.setDtFim(new Date());
            backupLogRepo.save(log);
        }
    }

    private void registrarLog(BackupConfig config, TipoBackup tipo, String mensagem, StatusAtivo sucesso) {
        BackupLog log = new BackupLog();
        log.setBackupConfig(config);
        log.setTipoOperacao(tipo.getCodigo());
        log.setDtInicio(new Date());
        log.setDtFim(new Date());
        log.setMensagem(mensagem);
        log.setSucesso(sucesso.getCodigo());
        backupLogRepo.save(log);
    }
}
