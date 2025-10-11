package anota.ai.api.domain.empresa.dto;

import anota.ai.api.domain.empresa.model.Empresa;
import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

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
                empresa.getEndereco() != null ?
                        new DadosCadastroEndereco(
                                empresa.getEndereco().getPais(),
                                empresa.getEndereco().getUf(),
                                empresa.getEndereco().getCidade(),
                                empresa.getEndereco().getBairro(),
                                empresa.getEndereco().getRua(),
                                empresa.getEndereco().getNumero(),
                                empresa.getEndereco().getComplemento(),
                                empresa.getEndereco().getCep()
                        ) : null,
                empresa.getDt_inclusao(),
                empresa.getDt_alteracao());
    }
}
