package anota.ai.api.domain.importacao.model;

import anota.ai.api.domain.importacao.enums.StatusImportacao;
import anota.ai.api.domain.importacao.enums.TipoImportacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "importacao_log")
@Entity(name = "Importação Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codImportacaoLog")
public class ImportacaoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_importacaolog")
    private Long codImportacaoLog;

    @Enumerated(EnumType.ORDINAL)
    private TipoImportacao tipo;

    @Enumerated(EnumType.ORDINAL)
    private StatusImportacao status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_inicio")
    private LocalDateTime dtInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_fim")
    private LocalDateTime dtFim;

    private Integer totalRegistros;
    private Integer registrosSucesso;
    private Integer registrosErro;

    @Column(columnDefinition = "TEXT", name = "mensagem_erro")
    private String mensagemErro;

    public ImportacaoLog(TipoImportacao tipo) {
        this.tipo = tipo;
        this.status = StatusImportacao.PROCESSANDO;
        this.dtInicio = LocalDateTime.now();
        this.totalRegistros = 0;
        this.registrosSucesso = 0;
        this.registrosErro = 0;
    }

    public void finalizar(StatusImportacao status) {
        this.status = status;
        this.dtFim = LocalDateTime.now();
    }

    public void adicionarErro(String mensagem) {
        if (this.mensagemErro == null || this.mensagemErro.isBlank()) {
            this.mensagemErro = mensagem;
        } else {
            this.mensagemErro += "\n" + mensagem;
        }
    }

    public void incrementarSucesso() {
        this.registrosSucesso++;
    }

    public void incrementarErro() {
        this.registrosErro++;
    }
}
