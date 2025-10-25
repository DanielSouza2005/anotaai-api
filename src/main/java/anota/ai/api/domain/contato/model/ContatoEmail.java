package anota.ai.api.domain.contato.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Table(name = "contato_email")
@Entity(name = "ContatoEmail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContatoEmail)) return false;
        ContatoEmail that = (ContatoEmail) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getTipo(), that.getTipo()) &&
                Objects.equals(getContato(), that.getContato());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getTipo(), getContato());
    }
}
