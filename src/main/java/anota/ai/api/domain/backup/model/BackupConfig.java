package anota.ai.api.domain.backup.model;

import anota.ai.api.domain.backup.dto.DadosAtualizacaoBackupConfig;
import anota.ai.api.domain.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "backup_config")
@Entity(name = "Backup Configurações")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codBackupConfig")
public class BackupConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_backupconfig")
    private Long codBackupConfig;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = true)
    private Usuario usuario;

    private Integer ativo;

    @Enumerated(EnumType.ORDINAL)
    private Frequencia frequencia;

    @Enumerated(EnumType.ORDINAL)
    private Formato formato;

    private Date dt_proximo_backup;
    private Date dt_inclusao;
    private Date dt_alteracao;

    public void atualizarDados(DadosAtualizacaoBackupConfig dados) {
        if (dados.frequencia() != null) {
            this.frequencia = dados.frequencia();
        }

        if (dados.dtProximoBackup() != null) {
            this.dt_proximo_backup = dados.dtProximoBackup();
        }

        this.ativo = dados.ativo();
        this.dt_alteracao = new Date();
    }
}
