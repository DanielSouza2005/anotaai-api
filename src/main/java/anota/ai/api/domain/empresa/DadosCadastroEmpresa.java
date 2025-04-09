package anota.ai.api.domain.empresa;

import anota.ai.api.domain.endereco.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroEmpresa(
        @NotBlank(message = "A razão social não pode estar em branco.")
        String razao,

        @NotBlank(message = "O nome fantasia não pode estar em branco.")
        String fantasia,

        @NotBlank(message = "O CNPJ não pode estar em branco.")
        @Pattern(regexp = "^\\d{14}$", message = "O CNPJ deve conter exatamente 14 dígitos numéricos.")
        String cnpj,

        @Pattern(regexp = "^\\d{8,14}$", message = "A inscrição estadual deve conter entre 8 e 14 dígitos.")
        String ie,

        @Valid
        DadosCadastroEndereco endereco
) {
}
