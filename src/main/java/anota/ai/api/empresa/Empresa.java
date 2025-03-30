package anota.ai.api.empresa;

import anota.ai.api.endereco.Endereco;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.Date;

@Table(name = "empresa")
@Entity(name = "Empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_empresa;
    private String razao;
    private String fantasia;
    private String cnpj;
    private String ie;

    @Embedded
    private Endereco endereco;

    private short ativo;
    private Date dt_inclusao;
    private Date dt_alteracao;

    public Empresa(DadosCadastroEmpresa dados) {
        this.razao = dados.razao();
        this.fantasia = dados.fantasia();
        this.cnpj = dados.cnpj();
        this.ie = dados.ie();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = 1;
        this.dt_inclusao = new Date();
        this.dt_alteracao = new Date();
    }

    public void atualizarDados(DadosAtualizacaoEmpresa dados) {
        this.dt_alteracao = new Date();

        if (dados.razao() != null) {
            this.razao = dados.razao();
        }

        if (dados.fantasia() != null) {
            this.fantasia = dados.fantasia();
        }

        if (dados.cnpj() != null) {
            this.cnpj = dados.cnpj();
        }

        if (dados.ie() != null) {
            this.ie = dados.ie();
        }

        if (dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void excluir() {
        this.ativo = 0;
    }
}
