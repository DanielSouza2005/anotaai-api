package anota.ai.api.domain.contato.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Table(name = "contato_telefone")
@Entity(name = "ContatoTelefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContatoTelefone)) return false;
        ContatoTelefone that = (ContatoTelefone) o;
        return Objects.equals(getTelefone(), that.getTelefone()) &&
                Objects.equals(getTipo(), that.getTipo()) &&
                Objects.equals(getContato(), that.getContato());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTelefone(), getTipo(), getContato());
    }
}
