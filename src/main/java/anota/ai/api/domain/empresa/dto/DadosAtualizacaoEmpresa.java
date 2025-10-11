package anota.ai.api.domain.empresa.dto;

import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

public record DadosAtualizacaoEmpresa(
        Long cod_empresa,
        String razao,
        String fantasia,
        String cnpj,
        String ie,
        DadosCadastroEndereco endereco
) {
}
