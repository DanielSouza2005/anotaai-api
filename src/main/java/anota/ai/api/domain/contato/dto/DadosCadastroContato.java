package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record DadosCadastroContato(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        Long cod_empresa,

        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
        String cpf,

        @Valid
        Set<DadosCadastroContatoTelefone> telefones,

        @Valid
        Set<DadosCadastroContatoEmail> emails,

        @Valid
        DadosCadastroEndereco endereco,

        String cargo,
        String departamento,
        String obs,
        String foto
) {

}
