package anota.ai.api.domain.exportacao.model;

import anota.ai.api.domain.exportacao.enums.StatusExportacao;
import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "exportacao_log")
@Entity(name = "Exportação Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codExportacaoLog")
public class ExportacaoLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_exportacaolog")
    private Long codExportacaoLog;

    @Enumerated(EnumType.ORDINAL)
    private TipoExportacao tipo;

    @Enumerated(EnumType.ORDINAL)
    private StatusExportacao status;

    private String caminho_arquivo;

    @Column(columnDefinition = "TEXT")
    private String mensagem_erro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_inclusao")
    private Date dtInclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_termino")
    private Date dtTermino;
}
