package anota.ai.api.domain.backup.model;

public enum Formato {
    SQL(0),
    XLSX(1),
    JSON(2);

    private final int codigo;

    Formato(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
