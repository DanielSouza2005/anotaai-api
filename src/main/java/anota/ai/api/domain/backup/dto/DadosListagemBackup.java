package anota.ai.api.domain.backup.dto;

import anota.ai.api.domain.backup.model.Backup;
import anota.ai.api.domain.backup.model.Formato;

import java.util.Date;

public record DadosListagemBackup(
        Long cod_backup,
        Long cod_usuario,
        String caminho_arquivo,
        Formato formato,
        Date dt_inclusao,
        Date dt_alteracao
) {
    public DadosListagemBackup(Backup backup) {
        this(backup.getCodBackup(),
                backup.getUsuario() != null
                        ? backup.getUsuario().getCod_usuario()
                        : null,
                backup.getCaminho_arquivo(),
                backup.getFormato(),
                backup.getDtInclusao(),
                backup.getDt_alteracao());

    }
}
