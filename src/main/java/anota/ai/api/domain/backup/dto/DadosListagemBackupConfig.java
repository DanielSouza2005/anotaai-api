package anota.ai.api.domain.backup.dto;

import anota.ai.api.domain.backup.model.BackupConfig;
import anota.ai.api.domain.backup.model.Formato;
import anota.ai.api.domain.backup.model.Frequencia;
import anota.ai.api.domain.usuario.model.Usuario;

import java.util.Date;

public record DadosListagemBackupConfig(
        Long codBackupConfig,
        Usuario usuario,
        Frequencia frequencia,
        Formato formato,
        Date dtProximoBackup,
        Date dtInclusao,
        Date dtAlteracao,
        Integer ativo
) {
    public DadosListagemBackupConfig(BackupConfig backupConfig) {
        this(backupConfig.getCodBackupConfig(),
                backupConfig.getUsuario() != null
                    ? backupConfig.getUsuario()
                    : null,
                backupConfig.getFrequencia(),
                backupConfig.getFormato(),
                backupConfig.getDt_proximo_backup(),
                backupConfig.getDt_inclusao(),
                backupConfig.getDt_alteracao(),
                backupConfig.getAtivo());
    }
}
