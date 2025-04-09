package anota.ai.api.domain.contato;

import anota.ai.api.domain.endereco.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroContato(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        Long cod_empresa,

        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
        String cpf,

        @Pattern(regexp = "^\\d{11}$", message = "O número de celular deve conter exatamente 11 dígitos (com DDD).")
        String celular,

        @Pattern(regexp = "^\\d{10}$", message = "O telefone deve conter exatamente 10 dígitos (com DDD).")
        String telefone,

        @Pattern(regexp = "^\\d{10}$", message = "O segundo telefone deve conter exatamente 10 dígitos (com DDD).")
        String telefone2,

        @Email(message = "O e-mail pessoal deve ser um endereço de e-mail válido.")
        String email_pessoal,

        @Email(message = "O e-mail corporativo deve ser um endereço de e-mail válido.")
        String email_corp,

        @Valid
        DadosCadastroEndereco endereco,

        String cargo,
        String departamento,
        String obs
) {

}
