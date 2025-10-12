package anota.ai.api.domain.contato.dto;

import jakarta.validation.constraints.Pattern;

public record DadosCadastroContatoTelefone(
        @Pattern(regexp = "^\\d{10,}$", message = "O número de telefone deve conter exatamente 11 dígitos (com DDD).")
        String telefone,
        String tipo
) {
}
