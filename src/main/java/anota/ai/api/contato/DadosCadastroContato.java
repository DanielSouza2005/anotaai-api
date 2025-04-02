package anota.ai.api.contato;

import anota.ai.api.endereco.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroContato(
        @NotBlank
        String nome,

        Long cod_empresa,

        @Pattern(regexp = "^\\d{11}$")
        String cpf,

        @Pattern(regexp = "^\\d{11}$")
        String celular,

        @Pattern(regexp = "^\\d{10}$")
        String telefone,

        @Pattern(regexp = "^\\d{10}$")
        String telefone2,

        @Email
        String email_pessoal,

        @Email
        String email_corp,

        @Valid
        DadosCadastroEndereco endereco,

        String cargo,
        String departamento,
        String obs
) {

}
