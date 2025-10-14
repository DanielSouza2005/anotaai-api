package anota.ai.api.domain.backup.model;

import anota.ai.api.domain.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "backup")
@Entity(name = "Backup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_backup")
public class Backup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_backup;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = true)
    private Usuario usuario;

    private String caminho_arquivo;

    @Enumerated(EnumType.ORDINAL)
    private Formato formato;

    @Column(name = "dt_inclusao")
    private Date dtInclusao;

    private Date dt_alteracao;
}
