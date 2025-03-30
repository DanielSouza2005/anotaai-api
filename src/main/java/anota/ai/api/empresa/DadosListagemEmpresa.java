package anota.ai.api.empresa;

import anota.ai.api.endereco.DadosCadastroEndereco;

import java.util.Date;

public record DadosListagemEmpresa(
        Long cod_empresa,
        String razao,
        String fantasia,
        String cnpj,
        String ie,
        DadosCadastroEndereco endereco,
        Date dt_inclusao,
        Date dt_alteracao
) {

    public DadosListagemEmpresa(Empresa empresa) {
        this(empresa.getCod_empresa(),
                empresa.getRazao(),
                empresa.getFantasia(),
                empresa.getCnpj(),
                empresa.getIe(),
                new DadosCadastroEndereco(
                        empresa.getEndereco().getPais(),
                        empresa.getEndereco().getBairro(),
                        empresa.getEndereco().getCep(),
                        empresa.getEndereco().getCidade(),
                        empresa.getEndereco().getUf(),
                        empresa.getEndereco().getNumero(),
                        empresa.getEndereco().getComplemento(),
                        empresa.getEndereco().getPais()
                ),
                empresa.getDt_inclusao(),
                empresa.getDt_alteracao());
    }
}
