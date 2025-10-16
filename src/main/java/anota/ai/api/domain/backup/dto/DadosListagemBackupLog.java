package anota.ai.api.domain.backup.dto;

import anota.ai.api.domain.backup.model.BackupConfig;
import anota.ai.api.domain.backup.model.BackupLog;

import java.util.Date;

public record DadosListagemBackupLog(
        Long codBackupLog,
        BackupConfig backupConfig,
        int tipoOperacao,
        int sucesso,
        String mensagem,
        Date dtInicio,
        Date dtFim
) {
    public DadosListagemBackupLog(BackupLog backupLog) {
        this(backupLog.getCodBackupLog(),
                backupLog.getBackupConfig(),
                backupLog.getTipoOperacao(),
                backupLog.getSucesso(),
                backupLog.getMensagem(),
                backupLog.getDtInicio(),
                backupLog.getDtFim());
    }
}
