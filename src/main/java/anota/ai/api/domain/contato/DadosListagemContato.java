package anota.ai.api.domain.contato;

import anota.ai.api.domain.empresa.DadosCadastroEmpresa;
import anota.ai.api.domain.endereco.DadosCadastroEndereco;

import java.util.Date;

public record DadosListagemContato(
        Long cod_contato,
        String nome,
        String cpf,
        DadosCadastroEmpresa empresa,
        String celular,
        String telefone,
        String telefone2,
        String email_pessoal,
        String email_corp,
        DadosCadastroEndereco endereco,
        Date dt_inclusao,
        Date dt_alteracao,
        String cargo,
        String departamento,
        String obs,
        String foto
) {
    public DadosListagemContato(Contato contato) {
        this(contato.getCod_contato(),
                contato.getNome(),
                contato.getCpf(),
                contato.getEmpresa() != null ?
                        new DadosCadastroEmpresa(
                                contato.getEmpresa().getRazao(),
                                contato.getEmpresa().getFantasia(),
                                contato.getEmpresa().getCnpj(),
                                contato.getEmpresa().getIe(),
                                contato.getEmpresa().getEndereco() != null ?
                                        new DadosCadastroEndereco(
                                                contato.getEmpresa().getEndereco().getPais(),
                                                contato.getEmpresa().getEndereco().getUf(),
                                                contato.getEmpresa().getEndereco().getCidade(),
                                                contato.getEmpresa().getEndereco().getBairro(),
                                                contato.getEmpresa().getEndereco().getRua(),
                                                contato.getEmpresa().getEndereco().getNumero(),
                                                contato.getEmpresa().getEndereco().getComplemento(),
                                                contato.getEmpresa().getEndereco().getCep()
                                        ) : null
                        ) : null,
                contato.getCelular(),
                contato.getTelefone(),
                contato.getTelefone2(),
                contato.getEmail_pessoal(),
                contato.getEmail_corp(),
                contato.getEndereco() != null ?
                        new DadosCadastroEndereco(
                                contato.getEndereco().getPais(),
                                contato.getEndereco().getUf(),
                                contato.getEndereco().getCidade(),
                                contato.getEndereco().getBairro(),
                                contato.getEndereco().getRua(),
                                contato.getEndereco().getNumero(),
                                contato.getEndereco().getComplemento(),
                                contato.getEndereco().getCep()
                        ) : null,
                contato.getDt_inclusao(),
                contato.getDt_alteracao(),
                contato.getCargo(),
                contato.getDepartamento(),
                contato.getObs(),
                contato.getFoto());
    }

}
