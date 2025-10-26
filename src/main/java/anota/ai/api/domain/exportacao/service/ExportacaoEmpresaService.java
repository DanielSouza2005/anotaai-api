package anota.ai.api.domain.exportacao.service;

import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.contato.model.ContatoEmail;
import anota.ai.api.domain.contato.model.ContatoTelefone;
import anota.ai.api.domain.contato.repository.ContatoRepository;
import anota.ai.api.domain.empresa.model.Empresa;
import anota.ai.api.domain.empresa.repository.EmpresaRepository;
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
public class ExportacaoEmpresaService extends ExportacaoService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    private final String[] cabecalhos = {
            "Código", "Razão", "Fantasia", "CNPJ", "Inscrição Estadual",
            "País", "Estado(UF)", "Cidade", "Bairro", "Rua", "Número", "Complemento", "Cep"
    };

    private final String pastaSupabase = "exportacao/empresas";
    private final String pastaLocal = "uploads/exportacao";

    @Override
    protected String gerarArquivo(ExportacaoLog exportacao) throws Exception {
        List<Empresa> empresas = empresaRepository.findAllByAtivo(StatusAtivo.ATIVO.getCodigo());

        Files.createDirectories(new File(pastaLocal).toPath());

        String nomeArquivo = "empresas_" + exportacao.getCodExportacaoLog() + ".xlsx";
        String caminhoCompleto = pastaLocal + "/" + nomeArquivo;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Empresas");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
                cell.setCellStyle(criarEstiloCabecalho(workbook));
            }

            int rowNum = 1;
            for (Empresa e : empresas) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(e.getCod_empresa());
                row.createCell(1).setCellValue(e.getRazao());
                row.createCell(2).setCellValue(e.getFantasia());
                row.createCell(3).setCellValue(e.getCnpj());
                row.createCell(4).setCellValue(e.getIe());

                if (e.getEndereco() != null) {
                    row.createCell(5).setCellValue(e.getEndereco().getPais());
                    row.createCell(6).setCellValue(e.getEndereco().getUf());
                    row.createCell(7).setCellValue(e.getEndereco().getCidade());
                    row.createCell(8).setCellValue(e.getEndereco().getBairro());
                    row.createCell(9).setCellValue(e.getEndereco().getRua());
                    row.createCell(10).setCellValue(e.getEndereco().getNumero());
                    row.createCell(11).setCellValue(e.getEndereco().getComplemento());
                    row.createCell(12).setCellValue(e.getEndereco().getCep());
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

        String nomeArquivo = "empresas_cabecalho_" + exportacao.getCodExportacaoLog() + ".xlsx";
        String caminhoCompleto = pastaLocal + "/" + nomeArquivo;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Empresas");

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
        return super.iniciarExportacao(TipoExportacao.EMPRESA);
    }

    public ExportacaoLog iniciarExportacaoCabecalho() throws Exception {
        return super.iniciarExportacaoCabecalho(TipoExportacao.EMPRESA);
    }
}
