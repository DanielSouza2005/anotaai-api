package anota.ai.api.domain.backup.model;

public enum TipoBackup {
    GERACAO(0),
    LIMPEZA(1);

    private final int codigo;

    TipoBackup(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
