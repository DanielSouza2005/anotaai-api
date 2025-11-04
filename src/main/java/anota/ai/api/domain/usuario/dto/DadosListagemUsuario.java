package anota.ai.api.domain.usuario.dto;

import anota.ai.api.domain.usuario.enums.Admin;
import anota.ai.api.domain.usuario.model.Usuario;

import java.util.Date;

public record DadosListagemUsuario(
        Long cod_usuario,
        String nome,
        String email,
        Date dt_inclusao,
        Date dt_alteracao,
        String foto,
        int admin
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getCod_usuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDt_inclusao(),
                usuario.getDt_alteracao(),
                usuario.getFoto(),
                usuario.getAdmin());
    }
}
