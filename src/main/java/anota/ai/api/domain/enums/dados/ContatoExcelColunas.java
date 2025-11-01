package anota.ai.api.domain.enums.dados;

public enum ContatoExcelColunas {
    CODIGO(0),
    NOME(1),
    CPF(2),
    CARGO(3),
    DEPARTAMENTO(4),
    OBSERVACAO(5),
    TELEFONES(6),
    EMAILS(7),
    EMPRESA(8),
    PAIS(9),
    ESTADO(10),
    CIDADE(11),
    BAIRRO(12),
    RUA(13),
    NUMERO(14),
    COMPLEMENTO(15),
    CEP(16);

    private final int index;

    ContatoExcelColunas(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
