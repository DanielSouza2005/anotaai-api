package anota.ai.api.domain.importacao.service;

import anota.ai.api.domain.importacao.enums.StatusImportacao;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import anota.ai.api.domain.importacao.model.ImportacaoLog;
import anota.ai.api.domain.importacao.repository.ImportacaoLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
}
