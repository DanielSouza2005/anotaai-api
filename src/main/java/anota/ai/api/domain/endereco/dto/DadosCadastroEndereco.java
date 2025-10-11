package anota.ai.api.domain.endereco.dto;

public record DadosCadastroEndereco(
        String pais,
        String uf,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String complemento,
        String cep
) {
}
