package anota.ai.api.domain.enums;

public enum StatusAtivo {
    ATIVO(1),
    INATIVO(0);

    private final int codigo;

    StatusAtivo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}