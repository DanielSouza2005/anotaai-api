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
        Long codigo = importacaoService.getLongCellValue(row, ContatoExcelColunas.CODIGO.index(), formatter, 0L);
        String nome = importacaoService.getStringCellValue(row, ContatoExcelColunas.NOME.index(), formatter);
        String cpf = importacaoService.getStringCellValue(row, ContatoExcelColunas.CPF.index(), formatter);
        String cargo = importacaoService.getStringCellValue(row, ContatoExcelColunas.CARGO.index(), formatter);
        String departamento = importacaoService.getStringCellValue(row, ContatoExcelColunas.DEPARTAMENTO.index(), formatter);
        String obs = importacaoService.getStringCellValue(row, ContatoExcelColunas.OBSERVACAO.index(), formatter);

        String telefonesStr = importacaoService.getStringCellValue(row, ContatoExcelColunas.TELEFONES.index(), formatter);
        Set<DadosCadastroContatoTelefone> listaTelefones = new HashSet<>();
        if (!telefonesStr.isBlank()) {
            for (String t : telefonesStr.split(";")) {
                String valor = t.trim();
                if (!valor.isBlank()) {
                    listaTelefones.add(new DadosCadastroContatoTelefone(valor, null));
                }
            }
        }

        String emailsStr = importacaoService.getStringCellValue(row, ContatoExcelColunas.EMAILS.index(), formatter);
        Set<DadosCadastroContatoEmail> listaEmails = new HashSet<>();
        if (!emailsStr.isBlank()) {
            for (String e : emailsStr.split(";")) {
                String valor = e.trim();
                if (!valor.isBlank()) {
                    listaEmails.add(new DadosCadastroContatoEmail(valor, null));
                }
            }
        }

        Long empresa = importacaoService.getLongCellValue(row, ContatoExcelColunas.EMPRESA.index(), formatter, null);

        DadosCadastroEndereco dadosEndereco = new DadosCadastroEndereco(
                importacaoService.getStringCellValue(row, ContatoExcelColunas.PAIS.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.ESTADO.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.CIDADE.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.BAIRRO.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.RUA.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.NUMERO.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.COMPLEMENTO.index(), formatter),
                importacaoService.getStringCellValue(row, ContatoExcelColunas.CEP.index(), formatter)
        );

        Contato contato;
        if (codigo > 0 && contatoRepository.existsById(codigo)) {
            DadosAtualizacaoContato dadosAtualizacao = new DadosAtualizacaoContato(
                    codigo, nome, empresa, cpf, listaTelefones, listaEmails,
                    dadosEndereco, cargo, departamento, obs, null
            );

            importacaoService.validarDTO(dadosAtualizacao, validator);

            contato = contatoRepository.findById(codigo).orElseThrow();
            contato.atualizarDados(dadosAtualizacao);
        } else {
            DadosCadastroContato dadosCadastro = new DadosCadastroContato(
                    nome, empresa, cpf, listaTelefones, listaEmails,
                    dadosEndereco, cargo, departamento, obs, null
            );

            importacaoService.validarDTO(dadosCadastro, validator);

            contato = new Contato(dadosCadastro);
        }

        contato.getTelefones().forEach(t -> t.setContato(contato));
        contato.getEmails().forEach(e -> e.setContato(contato));

        contatoRepository.save(contato);
        log.incrementarSucesso();
    }

}