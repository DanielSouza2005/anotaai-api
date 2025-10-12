package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.contato.model.ContatoTelefone;

public record DadosListagemContatoTelefone(
        Long cod_telefone,
        String email,
        String tipo
) {
    public DadosListagemContatoTelefone(ContatoTelefone contatoTelefone) {
        this(contatoTelefone.getCod_telefone(),
                contatoTelefone.getTelefone(),
                contatoTelefone.getTipo());
    }
}
