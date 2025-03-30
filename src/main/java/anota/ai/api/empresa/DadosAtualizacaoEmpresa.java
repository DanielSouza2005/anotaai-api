package anota.ai.api.empresa;

import anota.ai.api.endereco.DadosCadastroEndereco;

public record DadosAtualizacaoEmpresa(
        Long cod_empresa,
        String razao,
        String fantasia,
        String cnpj,
        String ie,
        DadosCadastroEndereco endereco
) {
}
