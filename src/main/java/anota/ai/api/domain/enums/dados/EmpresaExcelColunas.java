package anota.ai.api.domain.enums.dados;

public enum EmpresaExcelColunas {
    CODIGO(0),
    RAZAO(1),
    FANTASIA(2),
    CNPJ(3),
    IE(4),
    PAIS(9),
    ESTADO(10),
    CIDADE(11),
    BAIRRO(12),
    RUA(13),
    NUMERO(14),
    COMPLEMENTO(15),
    CEP(16);

    private final int index;

    EmpresaExcelColunas(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
