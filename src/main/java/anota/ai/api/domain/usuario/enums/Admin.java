package anota.ai.api.domain.usuario.enums;

public enum Admin {
    VERDADEIRO(1),
    FALSO(0);

    private final int codigo;

    Admin(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
