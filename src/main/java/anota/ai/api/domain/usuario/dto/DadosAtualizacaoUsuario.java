package anota.ai.api.domain.usuario.dto;

public record DadosAtualizacaoUsuario(
        Long cod_usuario,
        String nome,
        String senha,
        String email,
        String foto
) {
}
