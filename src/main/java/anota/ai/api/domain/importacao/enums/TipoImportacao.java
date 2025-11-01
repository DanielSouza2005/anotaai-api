package anota.ai.api.domain.importacao.enums;

public enum TipoImportacao {
    CONTATO(1),
    EMPRESA(2);

    private final int codigo;

    TipoImportacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
