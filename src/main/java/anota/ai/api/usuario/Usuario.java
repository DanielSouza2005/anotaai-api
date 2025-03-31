package anota.ai.api.usuario;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.Date;

@Table(name = "usuario")
@Entity(name = "Usuário")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_usuario")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_usuario;

    private String nome;
    private String senha;
    private String email;
    private int ativo;
    private Date dt_inclusao;
    private Date dt_alteracao;

    public Usuario(DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.senha = dados.senha();
        this.email = dados.email();

        this.ativo = 1;
        this.dt_inclusao = new Date();
        this.dt_alteracao = new Date();
    }

    public void atualizarDados(DadosAtualizacaoUsuario dados) {
        this.dt_alteracao = new Date();

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.senha() != null) {
            this.senha = dados.senha();
        }

        if (dados.email() != null) {
            this.email = dados.email();
        }
    }

    public void excluir() {
        this.ativo = 0;
    }
}
