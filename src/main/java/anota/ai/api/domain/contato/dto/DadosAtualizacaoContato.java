package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

import java.util.List;

public record DadosAtualizacaoContato(
        Long cod_contato,
        String nome,
        Long cod_empresa,
        String cpf,
        List<DadosCadastroContatoTelefone> telefones,
        List<DadosCadastroContatoEmail> emails,
        DadosCadastroEndereco endereco,
        String cargo,
        String departamento,
        String obs,
        String foto
) {
}
