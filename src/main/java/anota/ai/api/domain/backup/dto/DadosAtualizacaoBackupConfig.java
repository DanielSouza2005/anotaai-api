package anota.ai.api.domain.backup.dto;

import anota.ai.api.domain.backup.model.Frequencia;

import java.util.Date;

public record DadosAtualizacaoBackupConfig(
        Frequencia frequencia,
        Date dtProximoBackup,
        Integer ativo
) {
}
