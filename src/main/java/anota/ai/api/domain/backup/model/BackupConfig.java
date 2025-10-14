package anota.ai.api.domain.backup.model;

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
@EqualsAndHashCode(of = "cod_backupconfig")
public class BackupConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_backupconfig;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = true)
    private Usuario usuario;

    private int ativo;

    @Enumerated(EnumType.ORDINAL)
    private Frequencia frequencia;

    @Enumerated(EnumType.ORDINAL)
    private Formato formato;

    private Date dt_proximo_backup;
    private Date dt_inclusao;
    private Date dt_alteracao;
}
