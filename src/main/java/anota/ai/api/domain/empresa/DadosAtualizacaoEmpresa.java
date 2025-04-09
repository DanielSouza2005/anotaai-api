package anota.ai.api.domain.empresa;

import anota.ai.api.domain.endereco.DadosCadastroEndereco;

public record DadosAtualizacaoEmpresa(
        Long cod_empresa,
        String razao,
        String fantasia,
        String cnpj,
        String ie,
        DadosCadastroEndereco endereco
) {
}
