package anota.ai.api.domain.exportacao.dto;

import anota.ai.api.domain.exportacao.enums.StatusExportacao;
import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;

import java.util.Date;

public record DadosListagemExportacaoLog(
        Long cod_exportacaolog,
        TipoExportacao tipo_exportacao,
        StatusExportacao status,
        String caminho_arquivo,
        String mensagem_erro,
        Date dtInclusao,
        Date dtTermino
) {
    public DadosListagemExportacaoLog(ExportacaoLog exportacaoLog) {
        this(
                exportacaoLog.getCodExportacaoLog(),
                exportacaoLog.getTipo(),
                exportacaoLog.getStatus(),
                exportacaoLog.getCaminho_arquivo(),
                exportacaoLog.getMensagem_erro(),
                exportacaoLog.getDtInclusao(),
                exportacaoLog.getDtTermino()
        );
    }
}
