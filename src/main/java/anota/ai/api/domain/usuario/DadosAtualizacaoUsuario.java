package anota.ai.api.domain.usuario;

public record DadosAtualizacaoUsuario(
        Long cod_usuario,
        String nome,
        String senha,
        String email,
        String foto
) {
}
