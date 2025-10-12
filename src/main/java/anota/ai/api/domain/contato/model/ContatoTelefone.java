package anota.ai.api.domain.contato.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "contato_telefone")
@Entity(name = "ContatoTelefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_telefone")
public class ContatoTelefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_telefone;

    @ManyToOne
    @JoinColumn(name = "cod_contato", nullable = false)
    private Contato contato;

    @NotBlank
    private String telefone;
    
    private String tipo;
}
