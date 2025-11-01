package anota.ai.api.domain.importacao.service;

import anota.ai.api.domain.importacao.enums.StatusImportacao;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import anota.ai.api.domain.importacao.model.ImportacaoLog;
import anota.ai.api.domain.importacao.repository.ImportacaoLogRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.function.Consumer;

@Service
public class ImportacaoService {

    private final ImportacaoLogRepository logRepository;

    public ImportacaoService(ImportacaoLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Async
    public void executarImportacaoAsync(TipoImportacao tipo, int totalRegistros, Consumer<ImportacaoLog> executor) {
        executarImportacao(tipo, totalRegistros, executor);
    }

    public void executarImportacao(TipoImportacao tipo, int totalRegistros, Consumer<ImportacaoLog> executor) {
        ImportacaoLog log = new ImportacaoLog(tipo);
        log.setTotalRegistros(totalRegistros);
        logRepository.save(log);

        try {
            executor.accept(log);

            StatusImportacao statusFinal;
            if (log.getRegistrosErro() == 0) {
                statusFinal = StatusImportacao.SUCESSO;
            } else if (log.getRegistrosSucesso() == 0) {
                statusFinal = StatusImportacao.ERRO;
            } else {
                statusFinal = StatusImportacao.PARCIAL;
            }

            log.finalizar(statusFinal);
        } catch (Exception e) {
            log.setMensagemErro("Erro na importação: " + e.getMessage());
            log.finalizar(StatusImportacao.ERRO);
        }

        logRepository.save(log);
    }

    public String getStringCellValue(Row row, int colIndex, DataFormatter formatter) {
        if (row == null) return "";
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return formatter.formatCellValue(cell).trim();
    }

    public Long getLongCellValue(Row row, int colIndex, DataFormatter formatter, Long defaultValue) {
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

    public int contarLinhasExcel(MultipartFile arquivoExcel) {
        try (Workbook workbook = WorkbookFactory.create(arquivoExcel.getInputStream())) {
            return workbook.getSheetAt(0).getPhysicalNumberOfRows() - 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public void validarDTO(Object dto, Validator validator) {
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
