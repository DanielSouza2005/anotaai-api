package anota.ai.api.domain.exportacao.service;

import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.contato.model.ContatoEmail;
import anota.ai.api.domain.contato.model.ContatoTelefone;
import anota.ai.api.domain.contato.repository.ContatoRepository;
import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;
import anota.ai.api.domain.supabase.provider.SupabaseStorageService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;

@Service
public class ExportacaoContatoService extends ExportacaoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    private final String[] cabecalhos = {
            "Código", "Nome", "CPF", "Cargo", "Departamento", "Observação",
            "Telefones", "Emails", "Empresa",
            "País", "Estado(UF)", "Cidade", "Bairro", "Rua", "Número", "Complemento", "Cep"
    };

    private final String pastaLocal = "uploads/exportacao";
    private final String pastaSupabase = "exportacao/contatos";

    @Override
    protected String gerarArquivo(ExportacaoLog exportacao) throws Exception {
        List<Contato> contatos = contatoRepository.findAllByAtivo(StatusAtivo.ATIVO.getCodigo());

        Files.createDirectories(new File(pastaLocal).toPath());

        String nomeArquivo = "contatos_" + exportacao.getCodExportacaoLog() + ".xlsx";
        String caminhoCompleto = pastaLocal + "/" + nomeArquivo;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contatos");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
                cell.setCellStyle(criarEstiloCabecalho(workbook));
            }

            int rowNum = 1;
            for (Contato c : contatos) {
                Row row = sheet.createRow(rowNum++);

                String telefones = c.getTelefones().stream()
                        .map(ContatoTelefone::getTelefone)
                        .reduce((a, b) -> a + ";" + b)
                        .orElse("");

                String emails = c.getEmails().stream()
                        .map(ContatoEmail::getEmail)
                        .reduce((a, b) -> a + ";" + b)
                        .orElse("");

                row.createCell(0).setCellValue(c.getCod_contato());
                row.createCell(1).setCellValue(c.getNome());
                row.createCell(2).setCellValue(c.getCpf());
                row.createCell(3).setCellValue(c.getCargo());
                row.createCell(4).setCellValue(c.getDepartamento());
                row.createCell(5).setCellValue(c.getObs());
                row.createCell(6).setCellValue(telefones);
                row.createCell(7).setCellValue(emails);

                if (c.getEmpresa() != null) {
                    row.createCell(8).setCellValue(c.getEmpresa().getCod_empresa());
                }

                if (c.getEndereco() != null) {
                    row.createCell(9).setCellValue(c.getEndereco().getPais());
                    row.createCell(10).setCellValue(c.getEndereco().getUf());
                    row.createCell(11).setCellValue(c.getEndereco().getCidade());
                    row.createCell(12).setCellValue(c.getEndereco().getBairro());
                    row.createCell(13).setCellValue(c.getEndereco().getRua());
                    row.createCell(14).setCellValue(c.getEndereco().getNumero());
                    row.createCell(15).setCellValue(c.getEndereco().getComplemento());
                    row.createCell(16).setCellValue(c.getEndereco().getCep());
                }
            }

            for (int i = 0; i < cabecalhos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(caminhoCompleto)) {
                workbook.write(fileOut);
            }
        }

        File arquivoLocal = new File(caminhoCompleto);
        String urlPublica = supabaseStorageService.uploadFile(arquivoLocal, pastaSupabase);

        arquivoLocal.delete();

        return urlPublica;
    }

    @Override
    protected String gerarArquivoCabecalho(ExportacaoLog exportacao) throws Exception {
        Files.createDirectories(new File(pastaLocal).toPath());

        String nomeArquivo = "contatos_cabecalho_" + exportacao.getCodExportacaoLog() + ".xlsx";
        String caminhoCompleto = pastaLocal + "/" + nomeArquivo;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contatos");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
                cell.setCellStyle(criarEstiloCabecalho(workbook));
            }

            for (int i = 0; i < cabecalhos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(caminhoCompleto)) {
                workbook.write(fileOut);
            }
        }

        File arquivoLocal = new File(caminhoCompleto);
        String urlPublica = supabaseStorageService.uploadFile(arquivoLocal, pastaSupabase);

        arquivoLocal.delete();

        return urlPublica;
    }

    private CellStyle criarEstiloCabecalho(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    public ExportacaoLog iniciarExportacao() throws InterruptedException {
        return super.iniciarExportacao(TipoExportacao.CONTATO);
    }

    public ExportacaoLog iniciarExportacaoCabecalho() throws Exception {
        return super.iniciarExportacaoCabecalho(TipoExportacao.CONTATO);
    }
}
