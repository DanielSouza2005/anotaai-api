package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

import java.util.Set;

public record DadosAtualizacaoContato(
        Long cod_contato,
        String nome,
        Long cod_empresa,
        String cpf,
        Set<DadosCadastroContatoTelefone> telefones,
        Set<DadosCadastroContatoEmail> emails,
        DadosCadastroEndereco endereco,
        String cargo,
        String departamento,
        String obs,
        String foto
) {
}
