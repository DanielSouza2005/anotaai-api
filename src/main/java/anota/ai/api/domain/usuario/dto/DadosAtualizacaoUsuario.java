package anota.ai.api.domain.usuario.dto;

import anota.ai.api.domain.usuario.enums.Admin;

public record DadosAtualizacaoUsuario(
        Long cod_usuario,
        String nome,
        String senha,
        String email,
        String foto,
        int admin
) {
}
