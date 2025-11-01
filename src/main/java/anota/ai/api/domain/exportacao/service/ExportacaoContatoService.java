package anota.ai.api.domain.exportacao.service;

import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.contato.model.ContatoEmail;
import anota.ai.api.domain.contato.model.ContatoTelefone;
import anota.ai.api.domain.contato.repository.ContatoRepository;
import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.enums.dados.ContatoExcelColunas;
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

                row.createCell(ContatoExcelColunas.CODIGO.index()).setCellValue(c.getCod_contato());
                row.createCell(ContatoExcelColunas.NOME.index()).setCellValue(c.getNome());
                row.createCell(ContatoExcelColunas.CPF.index()).setCellValue(c.getCpf());
                row.createCell(ContatoExcelColunas.CARGO.index()).setCellValue(c.getCargo());
                row.createCell(ContatoExcelColunas.DEPARTAMENTO.index()).setCellValue(c.getDepartamento());
                row.createCell(ContatoExcelColunas.OBSERVACAO.index()).setCellValue(c.getObs());
                row.createCell(ContatoExcelColunas.TELEFONES.index()).setCellValue(telefones);
                row.createCell(ContatoExcelColunas.EMAILS.index()).setCellValue(emails);

                if (c.getEmpresa() != null) {
                    row.createCell(ContatoExcelColunas.EMPRESA.index()).setCellValue(c.getEmpresa().getCod_empresa());
                }

                if (c.getEndereco() != null) {
                    row.createCell(ContatoExcelColunas.PAIS.index()).setCellValue(c.getEndereco().getPais());
                    row.createCell(ContatoExcelColunas.ESTADO.index()).setCellValue(c.getEndereco().getUf());
                    row.createCell(ContatoExcelColunas.CIDADE.index()).setCellValue(c.getEndereco().getCidade());
                    row.createCell(ContatoExcelColunas.BAIRRO.index()).setCellValue(c.getEndereco().getBairro());
                    row.createCell(ContatoExcelColunas.RUA.index()).setCellValue(c.getEndereco().getRua());
                    row.createCell(ContatoExcelColunas.NUMERO.index()).setCellValue(c.getEndereco().getNumero());
                    row.createCell(ContatoExcelColunas.COMPLEMENTO.index()).setCellValue(c.getEndereco().getComplemento());
                    row.createCell(ContatoExcelColunas.CEP.index()).setCellValue(c.getEndereco().getCep());
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
