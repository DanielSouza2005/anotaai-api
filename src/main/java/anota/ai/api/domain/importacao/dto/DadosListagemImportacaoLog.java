package anota.ai.api.domain.importacao.dto;

import anota.ai.api.domain.importacao.enums.StatusImportacao;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import anota.ai.api.domain.importacao.model.ImportacaoLog;

import java.time.LocalDateTime;

public record DadosListagemImportacaoLog(
        Long cod_importacaolog,
        TipoImportacao tipoImportacao,
        StatusImportacao statusImportacao,
        LocalDateTime dtInicio,
        LocalDateTime dtFim,
        Integer totalRegistros,
        Integer registrosSucesso,
        Integer registrosErro,
        String mensagemErro
) {
    public DadosListagemImportacaoLog(ImportacaoLog importacaoLog) {
        this(
                importacaoLog.getCodImportacaoLog(),
                importacaoLog.getTipo(),
                importacaoLog.getStatus(),
                importacaoLog.getDtInicio(),
                importacaoLog.getDtFim(),
                importacaoLog.getTotalRegistros(),
                importacaoLog.getRegistrosSucesso(),
                importacaoLog.getRegistrosErro(),
                importacaoLog.getMensagemErro()
        );
    }
}
