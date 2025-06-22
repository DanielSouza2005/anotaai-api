package anota.ai.api.domain.status;

public enum StatusAtivo {
    ATIVO(1),
    INATIVO(0),
    PENDENTE(2);

    private final int codigo;

    StatusAtivo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}