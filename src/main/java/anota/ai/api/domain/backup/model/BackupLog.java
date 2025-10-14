package anota.ai.api.domain.backup.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "backup_log")
@Entity(name = "Backup Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_backuplog")
public class BackupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_backuplog;

    @ManyToOne
    @JoinColumn(name = "cod_backupconfig", nullable = false)
    private BackupConfig backupConfig;

    @Column(nullable = false)
    private int tipoOperacao;

    @Column(nullable = false)
    private int sucesso;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_inicio")
    private Date dtInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_fim")
    private Date dtFim;
}
