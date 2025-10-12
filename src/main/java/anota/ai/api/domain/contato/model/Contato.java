package anota.ai.api.domain.contato.model;

import anota.ai.api.domain.contato.dto.DadosAtualizacaoContato;
import anota.ai.api.domain.contato.dto.DadosCadastroContato;
import anota.ai.api.domain.contato.dto.DadosCadastroContatoEmail;
import anota.ai.api.domain.contato.dto.DadosCadastroContatoTelefone;
import anota.ai.api.domain.empresa.model.Empresa;
import anota.ai.api.domain.endereco.model.Endereco;
import anota.ai.api.domain.status.model.StatusAtivo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Empresa empresa;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContatoEmail> emails = new HashSet<>();

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContatoTelefone> telefones = new HashSet<>();

    @Embedded
    private Endereco endereco;

    private String cpf;
    private int ativo;
    private Date dt_inclusao;
    private Date dt_alteracao;

    private String cargo;
    private String departamento;
    private String obs;
    private String foto;

    public Contato(DadosCadastroContato dados) {
        this.nome = dados.nome();

        if (dados.cod_empresa() != null) {
            this.empresa = new Empresa();
            this.empresa.setCod_empresa(dados.cod_empresa());
        }

        this.cpf = dados.cpf();

        if (dados.telefones() != null) {
            for (DadosCadastroContatoTelefone contatoTelefone : dados.telefones()) {
                ContatoTelefone telefone = new ContatoTelefone();
                telefone.setContato(this);
                telefone.setTelefone(contatoTelefone.telefone());
                telefone.setTipo(contatoTelefone.tipo());
                this.telefones.add(telefone);
            }
        }

        if (dados.emails() != null) {
            for (DadosCadastroContatoEmail contatoEmail : dados.emails()) {
                ContatoEmail email = new ContatoEmail();
                email.setContato(this);
                email.setEmail(contatoEmail.email());
                email.setTipo(contatoEmail.tipo());
                this.emails.add(email);
            }
        }

        if (dados.endereco() != null) {
            this.endereco = new Endereco(dados.endereco());
        }

        this.ativo = StatusAtivo.ATIVO.getCodigo();
        this.dt_inclusao = new Date();
        this.dt_alteracao = new Date();

        this.cargo = dados.cargo();
        this.departamento = dados.departamento();
        this.obs = dados.obs();
        this.foto = dados.foto();
    }

    public void atualizarDados(DadosAtualizacaoContato dados) {
        this.dt_alteracao = new Date();

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.cod_empresa() != null) {
            this.empresa = new Empresa();
            this.empresa.setCod_empresa(dados.cod_empresa());
        }

        if (dados.cpf() != null) {
            this.cpf = dados.cpf();
        }

        if (dados.telefones() != null) {
            this.telefones.clear();
            for (DadosCadastroContatoTelefone contatoTelefone : dados.telefones()) {
                ContatoTelefone telefone = new ContatoTelefone();
                telefone.setContato(this);
                telefone.setTelefone(contatoTelefone.telefone());
                telefone.setTipo(contatoTelefone.tipo());
                this.telefones.add(telefone);
            }
        }

        if (dados.emails() != null) {
            this.emails.clear();
            for (DadosCadastroContatoEmail contatoEmail : dados.emails()) {
                ContatoEmail email = new ContatoEmail();
                email.setContato(this);
                email.setEmail(contatoEmail.email());
                email.setTipo(contatoEmail.tipo());
                this.emails.add(email);
            }
        }

        if (dados.endereco() != null) {
            if (this.endereco == null) {
                this.endereco = new Endereco(dados.endereco());
            } else {
                this.endereco.atualizarInformacoes(dados.endereco());
            }
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

        if (dados.foto() != null) {
            this.foto = dados.foto();
        }
    }

    public void excluir() {
        this.ativo = StatusAtivo.INATIVO.getCodigo();
    }
}
