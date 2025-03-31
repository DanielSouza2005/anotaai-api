package anota.ai.api.usuario;

import java.util.Date;

public record DadosListagemUsuario(
        Long cod_usuario,
        String nome,
        String email,
        int ativo,
        Date dt_inclusao,
        Date dt_alteracao
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getCod_usuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getDt_inclusao(),
                usuario.getDt_alteracao());
    }
}
