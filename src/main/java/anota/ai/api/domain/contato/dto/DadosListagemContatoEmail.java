package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.contato.model.ContatoEmail;

public record DadosListagemContatoEmail(
        Long cod_email,
        String email,
        String tipo
) {
    public DadosListagemContatoEmail(ContatoEmail contatoEmail) {
        this(contatoEmail.getCod_email(),
                contatoEmail.getEmail(),
                contatoEmail.getTipo());
    }
}
