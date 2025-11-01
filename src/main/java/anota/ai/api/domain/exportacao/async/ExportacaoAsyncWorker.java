package anota.ai.api.domain.exportacao.async;

import anota.ai.api.domain.exportacao.enums.StatusExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;
import anota.ai.api.domain.exportacao.repository.ExportacaoLogRepository;
import anota.ai.api.domain.exportacao.service.ExportacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExportacaoAsyncWorker {

    @Autowired
    private ExportacaoLogRepository exportacaoRepository;

    @Async
    public void executar(ExportacaoService exportacaoService, ExportacaoLog exportacao) {
        try {
            exportacaoService.processarExportacao(exportacao);
        } catch (Exception e) {
            exportacao.setStatus(StatusExportacao.ERRO);
            exportacao.setMensagem_erro("Erro inesperado: " + e.getMessage());
            exportacao.setDtTermino(new Date());
            exportacaoRepository.save(exportacao);

            e.printStackTrace();
        }
    }
}

