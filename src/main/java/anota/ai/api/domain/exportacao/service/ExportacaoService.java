package anota.ai.api.domain.exportacao.service;

import anota.ai.api.domain.exportacao.async.ExportacaoAsyncWorker;
import anota.ai.api.domain.exportacao.enums.StatusExportacao;
import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;
import anota.ai.api.domain.exportacao.repository.ExportacaoLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public abstract class ExportacaoService {

    @Autowired
    private ExportacaoLogRepository exportacaoRepository;

    @Autowired
    private ExportacaoAsyncWorker worker;

    public ExportacaoLog iniciarExportacao(TipoExportacao tipo) throws InterruptedException {
        ExportacaoLog exportacao = new ExportacaoLog();
        exportacao.setTipo(tipo);
        exportacao.setStatus(StatusExportacao.PENDENTE);
        exportacao.setDtInclusao(new Date());
        exportacaoRepository.save(exportacao);

        worker.executar(this, exportacao);
        return exportacao;
    }

    public ExportacaoLog iniciarExportacaoCabecalho(TipoExportacao tipo) throws Exception {
        ExportacaoLog exportacao = new ExportacaoLog();
        exportacao.setTipo(tipo);
        exportacao.setStatus(StatusExportacao.PENDENTE);
        exportacao.setDtInclusao(new Date());
        exportacaoRepository.save(exportacao);

        this.processarExportacaoCabecalho(exportacao);
        return exportacao;
    }

    protected abstract String gerarArquivo(ExportacaoLog exportacao) throws Exception;
    protected abstract String gerarArquivoCabecalho(ExportacaoLog exportacao) throws Exception;

    @Async
    public void processarExportacao(ExportacaoLog exportacao) {
        try {
            exportacao.setStatus(StatusExportacao.PROCESSANDO);
            exportacaoRepository.save(exportacao);

            String caminhoArquivo = gerarArquivo(exportacao);

            exportacao.setCaminho_arquivo(caminhoArquivo);
            exportacao.setStatus(StatusExportacao.CONCLUIDO);
        } catch (Exception e) {
            exportacao.setStatus(StatusExportacao.ERRO);
            exportacao.setMensagem_erro(e.getMessage());
        } finally {
            exportacao.setDtTermino(new Date());
            exportacaoRepository.save(exportacao);
        }
    }

    public void processarExportacaoCabecalho(ExportacaoLog exportacao) {
        try {
            exportacao.setStatus(StatusExportacao.PROCESSANDO);
            exportacaoRepository.save(exportacao);

            String caminhoArquivo = gerarArquivoCabecalho(exportacao);

            exportacao.setCaminho_arquivo(caminhoArquivo);
            exportacao.setStatus(StatusExportacao.CONCLUIDO);
        } catch (Exception e) {
            exportacao.setStatus(StatusExportacao.ERRO);
            exportacao.setMensagem_erro(e.getMessage());
        } finally {
            exportacao.setDtTermino(new Date());
            exportacaoRepository.save(exportacao);
        }
    }
}
