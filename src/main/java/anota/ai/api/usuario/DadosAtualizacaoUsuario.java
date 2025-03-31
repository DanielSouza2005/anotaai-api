package anota.ai.api.usuario;

public record DadosAtualizacaoUsuario(
        Long cod_usuario,
        String nome,
        String senha,
        String email
) {
}
