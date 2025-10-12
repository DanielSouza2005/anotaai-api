package anota.ai.api.domain.contato.dto;

import jakarta.validation.constraints.Email;

public record DadosCadastroContatoEmail(
        @Email(message = "O e-mail deve ser um endereço de e-mail válido.")
        String email,

        String tipo
) {
}
