package anota.ai.api.domain.contato;

import anota.ai.api.domain.endereco.DadosCadastroEndereco;

import java.util.Date;

public record DadosListagemContato(
        Long cod_contato,
        String nome,
        String cpf,
        Long cod_empresa,
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
        String obs
) {
    public DadosListagemContato(Contato contato) {
        this(contato.getCod_contato(),
                contato.getNome(),
                contato.getCpf(),
                contato.getCod_empresa() != null ? contato.getCod_empresa().getCod_empresa() : null,
                contato.getCelular(),
                contato.getTelefone(),
                contato.getTelefone2(),
                contato.getEmail_pessoal(),
                contato.getEmail_corp(),
                new DadosCadastroEndereco(
                        contato.getEndereco().getPais(),
                        contato.getEndereco().getUf(),
                        contato.getEndereco().getCidade(),
                        contato.getEndereco().getBairro(),
                        contato.getEndereco().getRua(),
                        contato.getEndereco().getNumero(),
                        contato.getEndereco().getComplemento(),
                        contato.getEndereco().getCep()
                ),
                contato.getDt_inclusao(),
                contato.getDt_alteracao(),
                contato.getCargo(),
                contato.getDepartamento(),
                contato.getObs());
    }

}
