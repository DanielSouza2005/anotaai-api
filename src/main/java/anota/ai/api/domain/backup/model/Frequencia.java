package anota.ai.api.domain.backup.model;

public enum Frequencia {
    DIARIO(0),
    SEMANAL(1),
    MENSAL(2);

    private final int codigo;

    Frequencia(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
