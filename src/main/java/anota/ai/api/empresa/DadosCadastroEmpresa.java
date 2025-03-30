package anota.ai.api.empresa;

import anota.ai.api.endereco.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroEmpresa(
        @NotBlank
        String razao,

        @NotBlank
        String fantasia,

        @NotBlank
        @Pattern(regexp = "^\\d{14}$")
        String cnpj,

        @Pattern(regexp = "^\\d{8,14}$")
        String ie,

        @Valid
        @NotNull
        DadosCadastroEndereco endereco
) {
}
