package anota.ai.api.domain.exportacao.enums;

public enum TipoExportacao {
    CONTATO(0),
    EMPRESA(1);

    private final int codigo;

    TipoExportacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
