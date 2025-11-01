package anota.ai.api.domain.importacao.service;

import anota.ai.api.domain.empresa.dto.DadosAtualizacaoEmpresa;
import anota.ai.api.domain.empresa.dto.DadosCadastroEmpresa;
import anota.ai.api.domain.empresa.model.Empresa;
import anota.ai.api.domain.empresa.repository.EmpresaRepository;
import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;
import anota.ai.api.domain.enums.dados.EmpresaExcelColunas;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import anota.ai.api.domain.importacao.model.ImportacaoLog;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Service
public class ImportacaoEmpresaService {

    @Autowired
    private ImportacaoService importacaoService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private Validator validator;

    final DataFormatter formatter = new DataFormatter();

    public void importar(MultipartFile arquivoExcel) {
        int totalLinhas = importacaoService.contarLinhasExcel(arquivoExcel);

        if (totalLinhas <= 0) {
            throw new RuntimeException("O arquivo Excel não contém registros para importar.");
        }

        byte[] dados;
        try {
            dados = arquivoExcel.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivo: " + e.getMessage(), e);
        }

        importacaoService.executarImportacaoAsync(TipoImportacao.EMPRESA, totalLinhas, log -> {
            try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(dados))) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;

                    try {
                        processarLinha(row, log);
                    } catch (Exception e) {
                        log.incrementarErro();
                        String erro = "Linha " + (row.getRowNum() + 1) + " → " + e.getMessage();
                        log.adicionarErro(erro);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar planilha: " + e.getMessage(), e);
            }
        });
    }

    @Transactional
    private void processarLinha(Row row, ImportacaoLog log) {
        Long codigo = importacaoService.getLongCellValue(row, EmpresaExcelColunas.CODIGO.index(), formatter, 0L);
        String razao = importacaoService.getStringCellValue(row, EmpresaExcelColunas.RAZAO.index(), formatter);
        String fantasia = importacaoService.getStringCellValue(row, EmpresaExcelColunas.FANTASIA.index(), formatter);
        String cnpj = importacaoService.getStringCellValue(row, EmpresaExcelColunas.CNPJ.index(), formatter);
        String ie = importacaoService.getStringCellValue(row, EmpresaExcelColunas.IE.index(), formatter);

        DadosCadastroEndereco dadosEndereco = new DadosCadastroEndereco(
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.PAIS.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.ESTADO.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.CIDADE.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.BAIRRO.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.RUA.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.NUMERO.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.COMPLEMENTO.index(), formatter),
                importacaoService.getStringCellValue(row, EmpresaExcelColunas.CEP.index(), formatter)
        );

        Empresa empresa;
        if (codigo > 0 && empresaRepository.existsById(codigo)) {
            DadosAtualizacaoEmpresa dadosAtualizacao = new DadosAtualizacaoEmpresa(
                    codigo, razao, fantasia, cnpj, ie, dadosEndereco
            );

            importacaoService.validarDTO(dadosAtualizacao, validator);

            empresa = empresaRepository.findById(codigo).orElseThrow();
            empresa.atualizarDados(dadosAtualizacao);
        } else {
            DadosCadastroEmpresa dadosCadastro = new DadosCadastroEmpresa(
                    razao, fantasia, cnpj, ie, dadosEndereco
            );

            importacaoService.validarDTO(dadosCadastro, validator);

            empresa = new Empresa(dadosCadastro);
        }

        empresaRepository.save(empresa);
        log.incrementarSucesso();
    }
}
