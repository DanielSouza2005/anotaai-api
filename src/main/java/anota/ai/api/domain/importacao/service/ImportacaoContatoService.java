package anota.ai.api.domain.importacao.service;

import anota.ai.api.domain.contato.dto.DadosAtualizacaoContato;
import anota.ai.api.domain.contato.dto.DadosCadastroContato;
import anota.ai.api.domain.contato.dto.DadosCadastroContatoEmail;
import anota.ai.api.domain.contato.dto.DadosCadastroContatoTelefone;
import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.contato.repository.ContatoRepository;
import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;
import anota.ai.api.domain.enums.dados.ContatoExcelColunas;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import anota.ai.api.domain.importacao.model.ImportacaoLog;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

@Service
public class ImportacaoContatoService {

    @Autowired
    private ImportacaoService importacaoService;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private Validator validator;

    final DataFormatter formatter = new DataFormatter();

    public void importar(MultipartFile arquivoExcel) {
        int totalLinhas = contarLinhasExcel(arquivoExcel);

        if (totalLinhas <= 0) {
            throw new RuntimeException("O arquivo Excel não contém registros para importar.");
        }

        byte[] dados;
        try {
            dados = arquivoExcel.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivo: " + e.getMessage(), e);
        }

        importacaoService.executarImportacaoAsync(TipoImportacao.CONTATO, totalLinhas, log -> {
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
        Long codigo = getLongCellValue(row, ContatoExcelColunas.CODIGO.index(), formatter, 0L);
        String nome = getStringCellValue(row, ContatoExcelColunas.NOME.index(), formatter);
        String cpf = getStringCellValue(row, ContatoExcelColunas.CPF.index(), formatter);
        String cargo = getStringCellValue(row, ContatoExcelColunas.CARGO.index(), formatter);
        String departamento = getStringCellValue(row, ContatoExcelColunas.DEPARTAMENTO.index(), formatter);
        String obs = getStringCellValue(row, ContatoExcelColunas.OBSERVACAO.index(), formatter);

        String telefonesStr = getStringCellValue(row, ContatoExcelColunas.EMAILS.index(), formatter);
        Set<DadosCadastroContatoTelefone> listaTelefones = new HashSet<>();
        if (!telefonesStr.isBlank()) {
            for (String t : telefonesStr.split(";")) {
                String valor = t.trim();
                if (!valor.isBlank()) {
                    listaTelefones.add(new DadosCadastroContatoTelefone(valor, null));
                }
            }
        }

        String emailsStr = getStringCellValue(row, ContatoExcelColunas.TELEFONES.index(), formatter);
        Set<DadosCadastroContatoEmail> listaEmails = new HashSet<>();
        if (!emailsStr.isBlank()) {
            for (String e : emailsStr.split(";")) {
                String valor = e.trim();
                if (!valor.isBlank()) {
                    listaEmails.add(new DadosCadastroContatoEmail(valor, null));
                }
            }
        }

        Long empresa = getLongCellValue(row, ContatoExcelColunas.EMPRESA.index(), formatter, null);

        DadosCadastroEndereco dadosEndereco = new DadosCadastroEndereco(
                getStringCellValue(row, ContatoExcelColunas.PAIS.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.ESTADO.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.CIDADE.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.BAIRRO.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.RUA.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.NUMERO.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.COMPLEMENTO.index(), formatter),
                getStringCellValue(row, ContatoExcelColunas.CEP.index(), formatter)
        );

        Contato contato;
        if (codigo > 0 && contatoRepository.existsById(codigo)) {
            DadosAtualizacaoContato dadosAtualizacao = new DadosAtualizacaoContato(
                    codigo, nome, empresa, cpf, listaTelefones, listaEmails,
                    dadosEndereco, cargo, departamento, obs, null
            );

            validarDTO(dadosAtualizacao);

            contato = contatoRepository.findById(codigo).orElseThrow();
            contato.atualizarDados(dadosAtualizacao);
        } else {
            DadosCadastroContato dadosCadastro = new DadosCadastroContato(
                    nome, empresa, cpf, listaTelefones, listaEmails,
                    dadosEndereco, cargo, departamento, obs, null
            );

            validarDTO(dadosCadastro);

            contato = new Contato(dadosCadastro);
        }

        contato.getTelefones().forEach(t -> t.setContato(contato));
        contato.getEmails().forEach(e -> e.setContato(contato));

        contatoRepository.save(contato);
        log.incrementarSucesso();
    }

    private String getStringCellValue(Row row, int colIndex, DataFormatter formatter) {
        if (row == null) return "";
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return formatter.formatCellValue(cell).trim();
    }

    private Long getLongCellValue(Row row, int colIndex, DataFormatter formatter, Long defaultValue) {
        String s = getStringCellValue(row, colIndex, formatter);
        if (s == null || s.isBlank()) return defaultValue;
        s = s.replaceAll("\\.0+$", "");
        s = s.replaceAll("[\\s\\,\\.]", "");
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private int contarLinhasExcel(MultipartFile arquivoExcel) {
        try (Workbook workbook = WorkbookFactory.create(arquivoExcel.getInputStream())) {
            return workbook.getSheetAt(0).getPhysicalNumberOfRows() - 1;
        } catch (Exception e) {
            return 0;
        }
    }

    private void validarDTO(Object dto) {
        Set<ConstraintViolation<Object>> violacoes = validator.validate(dto);
        if (!violacoes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> v : violacoes) {
                sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }

}