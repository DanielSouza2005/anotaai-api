package anota.ai.api.domain.contato;

import anota.ai.api.domain.empresa.Empresa;
import anota.ai.api.domain.endereco.Endereco;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "contato")
@Entity(name = "Contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_contato;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "cod_empresa", nullable = true)
    private Empresa cod_empresa;

    private String cpf;
    private String celular;
    private String telefone;
    private String telefone2;
    private String email_pessoal;
    private String email_corp;

    @Embedded
    private Endereco endereco;

    private short ativo;
    private Date dt_inclusao;
    private Date dt_alteracao;

    private String cargo;
    private String departamento;
    private String obs;

    public Contato(DadosCadastroContato dados) {
        this.nome = dados.nome();

        if (dados.cod_empresa() != null) {
            this.cod_empresa = new Empresa();
            this.cod_empresa.setCod_empresa(dados.cod_empresa());
        }

        this.cpf = dados.cpf();
        this.celular = dados.celular();
        this.telefone = dados.telefone();
        this.telefone2 = dados.telefone2();
        this.email_pessoal = dados.email_pessoal();
        this.email_corp = dados.email_corp();

        this.endereco = new Endereco(dados.endereco());
        this.ativo = 1;
        this.dt_inclusao = new Date();
        this.dt_alteracao = new Date();

        this.cargo = dados.cargo();
        this.departamento = dados.departamento();
        this.obs = dados.obs();
    }

    public void atualizarDados(DadosAtualizacaoContato dados) {
        this.dt_alteracao = new Date();

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.cod_empresa() != null) {
            this.cod_empresa = new Empresa();
            this.cod_empresa.setCod_empresa(dados.cod_empresa());
        }

        if (dados.cpf() != null) {
            this.cpf = dados.cpf();
        }

        if (dados.celular() != null) {
            this.celular = dados.celular();
        }

        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }

        if (dados.telefone2() != null) {
            this.telefone2 = dados.telefone2();
        }

        if (dados.email_pessoal() != null) {
            this.email_pessoal = dados.email_pessoal();
        }

        if (dados.email_corp() != null) {
            this.email_corp = dados.email_corp();
        }

        if (this.endereco == null) {
            this.endereco = new Endereco(dados.endereco());
        } else {
            this.endereco.atualizarInformacoes(dados.endereco());
        }

        if (dados.cargo() != null) {
            this.cargo = dados.cargo();
        }

        if (dados.departamento() != null) {
            this.departamento = dados.departamento();
        }

        if (dados.obs() != null) {
            this.obs = dados.obs();
        }
    }

    public void excluir() {
        this.ativo = 0;
    }
}
