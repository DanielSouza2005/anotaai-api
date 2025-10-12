package anota.ai.api.domain.contato.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "contato_email")
@Entity(name = "ContatoEmail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_email")
public class ContatoEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_email;

    @ManyToOne
    @JoinColumn(name = "cod_contato", nullable = false)
    private Contato contato;

    @NotBlank
    private String email;
    
    private String tipo;
}
