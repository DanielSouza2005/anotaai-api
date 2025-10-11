package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

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
        String obs,
        String foto
) {
}
