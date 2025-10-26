package anota.ai.api.domain.exportacao.enums;

public enum StatusExportacao {
    PENDENTE(0),
    PROCESSANDO(1),
    CONCLUIDO(2),
    ERRO(3);

    private final int codigo;

    StatusExportacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
