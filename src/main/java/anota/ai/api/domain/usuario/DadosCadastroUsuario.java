package anota.ai.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(

        @NotBlank(message = "O nome do usuário não pode estar em branco.")
        String nome,

        @NotBlank(message = "A senha não pode estar em branco.")
        String senha,

        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O e-mail deve ser um endereço válido.")
        String email
) {
}
