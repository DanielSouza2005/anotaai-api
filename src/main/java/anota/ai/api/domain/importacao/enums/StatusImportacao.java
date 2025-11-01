package anota.ai.api.domain.importacao.enums;

public enum StatusImportacao {
    PROCESSANDO(0),
    SUCESSO(1),
    PARCIAL(2),
    ERRO(3);

    private final int codigo;

    StatusImportacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
