package anota.ai.api.domain.backup.service;

import anota.ai.api.domain.backup.model.*;
import anota.ai.api.domain.backup.repository.BackupConfigRepository;
import anota.ai.api.domain.backup.repository.BackupLogRepository;
import anota.ai.api.domain.backup.repository.BackupRepository;
import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.supabase.provider.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BackupScheduler {

    @Value("${backup.retention.days:5}")
    private int retentionDays;

    @Autowired
    private BackupConfigRepository backupConfigRepo;

    @Autowired
    private BackupRepository backupRepo;

    @Autowired
    private BackupLogRepository backupLogRepo;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private BackupService backupService;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    /**
     * Roda a cada minuto, verifica se há backup agendado para executar
     */
    @Scheduled(cron = "0 * * * * *")
    public void verificarBackups() {
        List<BackupConfig> configs = backupConfigRepo.findAllByAtivo(StatusAtivo.ATIVO.getCodigo());

        Date agora = new Date();

        for (BackupConfig config : configs) {
            if (config.getDt_proximo_backup() != null && !config.getDt_proximo_backup().after(agora)) {
                executor.submit(() -> executarBackup(config));
            }
        }
    }

    /**
     * Executa o backup de forma isolada
     */
    private void executarBackup(BackupConfig config) {
        Date inicio = new Date();
        BackupLog log = new BackupLog();
        log.setBackupConfig(config);
        log.setTipoOperacao(TipoBackup.GERACAO.getCodigo());
        log.setDtInicio(inicio);

        try {
            backupService.gerarBackup(config);

            log.setSucesso(StatusAtivo.ATIVO.getCodigo());
            log.setMensagem("Backup gerado com sucesso -> Data do próximo backup atualizada.");

            Date proximo = calcularProximoBackup(config);
            config.setDt_proximo_backup(proximo);
            backupConfigRepo.save(config);
        } catch (Exception e) {
            log.setSucesso(StatusAtivo.INATIVO.getCodigo());
            log.setMensagem("Erro: " + e.getMessage());

            Calendar retryTime = Calendar.getInstance();
            retryTime.add(Calendar.MINUTE, 30);
            config.setDt_proximo_backup(retryTime.getTime());
            backupConfigRepo.save(config);
        } finally {
            log.setDtFim(new Date());
            backupLogRepo.save(log);
        }
    }

    /**
     * Calcula a próxima data de backup com base na frequência
     */
    private Date calcularProximoBackup(BackupConfig config) {
        Date agora = new Date();
        Frequencia freq = config.getFrequencia();

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(agora);

        switch (freq) {
            case Frequencia.DIARIO -> cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            case Frequencia.SEMANAL -> cal.add(java.util.Calendar.WEEK_OF_YEAR, 1);
            case Frequencia.MENSAL -> cal.add(java.util.Calendar.MONTH, 1);
        }

        return cal.getTime();
    }

    /**
     * Limpeza automática de backups antigos (executa 1x por dia às 03h)
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void limparBackupsAntigos() {

        Calendar limite = Calendar.getInstance();
        limite.add(Calendar.DAY_OF_MONTH, -retentionDays);

        List<Backup> antigos = backupRepo.findByDtInclusaoBefore(limite.getTime());

        for (Backup backup : antigos) {
            executor.submit(() -> LimparBackup(backup));
        }
    }

    private void LimparBackup(Backup backup) {
        Date inicio = new Date();
        BackupLog log = new BackupLog();
        log.setTipoOperacao(TipoBackup.LIMPEZA.getCodigo());
        log.setDtInicio(inicio);

        try {
            backupService.excluirBackupAntigo(backup);

            log.setSucesso(StatusAtivo.ATIVO.getCodigo());
            log.setMensagem("Backup limpo com sucesso.");
        } catch (Exception e) {
            log.setSucesso(StatusAtivo.INATIVO.getCodigo());
            log.setMensagem("Erro: " + e.getMessage());
        } finally {
            log.setDtFim(new Date());
            backupLogRepo.save(log);
        }
    }
}
