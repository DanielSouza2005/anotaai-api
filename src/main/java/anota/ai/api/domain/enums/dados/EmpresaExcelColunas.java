package anota.ai.api.domain.enums.dados;

public enum EmpresaExcelColunas {
    CODIGO(0),
    RAZAO(1),
    FANTASIA(2),
    CNPJ(3),
    IE(4),
    PAIS(5),
    ESTADO(6),
    CIDADE(7),
    BAIRRO(8),
    RUA(9),
    NUMERO(10),
    COMPLEMENTO(11),
    CEP(12);

    private final int index;

    EmpresaExcelColunas(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
