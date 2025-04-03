package anota.ai.api.contato;

import anota.ai.api.endereco.DadosCadastroEndereco;

public record DadosAtualizacaoContato(
        Long cod_contato,
        String nome,
        Long cod_empresa,
        String cpf,
        String celular,
        String telefone,
        String telefone2,
        String email_pessoal,
        String email_corp,
        DadosCadastroEndereco endereco,
        String cargo,
        String departamento,
        String obs
) {
}
