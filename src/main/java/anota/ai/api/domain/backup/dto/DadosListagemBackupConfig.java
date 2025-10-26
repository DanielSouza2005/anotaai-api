package anota.ai.api.domain.backup.dto;

import anota.ai.api.domain.backup.model.BackupConfig;
import anota.ai.api.domain.backup.model.Formato;
import anota.ai.api.domain.backup.model.Frequencia;

import java.util.Date;

public record DadosListagemBackupConfig(
        Long codBackupConfig,
        Frequencia frequencia,
        Formato formato,
        Date dtProximoBackup,
        Date dtInclusao,
        Date dtAlteracao,
        Integer ativo
) {
    public DadosListagemBackupConfig(BackupConfig backupConfig) {
        this(backupConfig.getCodBackupConfig(),
                backupConfig.getFrequencia(),
                backupConfig.getFormato(),
                backupConfig.getDt_proximo_backup(),
                backupConfig.getDt_inclusao(),
                backupConfig.getDt_alteracao(),
                backupConfig.getAtivo());
    }
}
