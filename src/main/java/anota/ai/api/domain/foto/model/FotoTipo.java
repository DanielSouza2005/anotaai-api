package anota.ai.api.domain.foto.model;

public enum FotoTipo {
    USUARIOS("usuarios"),
    CONTATOS("contatos");

    private final String pasta;

    FotoTipo(String pasta) {
        this.pasta = pasta;
    }

    public String getPasta() {
        return pasta;
    }
}