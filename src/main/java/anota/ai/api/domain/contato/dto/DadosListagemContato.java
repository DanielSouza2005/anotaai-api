package anota.ai.api.domain.contato.dto;

import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.empresa.dto.DadosListagemEmpresa;
import anota.ai.api.domain.endereco.dto.DadosCadastroEndereco;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListagemContato(
        Long cod_contato,
        String nome,
        String cpf,
        DadosListagemEmpresa empresa,
        List<DadosListagemContatoEmail> emails,
        List<DadosListagemContatoTelefone> telefones,
        DadosCadastroEndereco endereco,
        Date dt_inclusao,
        Date dt_alteracao,
        String cargo,
        String departamento,
        String obs,
        String foto
) {
    public DadosListagemContato(Contato contato) {
        this(contato.getCod_contato(),
                contato.getNome(),
                contato.getCpf(),
                contato.getEmpresa() != null
                        ? new DadosListagemEmpresa(contato.getEmpresa())
                        : null,
                contato.getEmails() != null
                        ? contato.getEmails().stream()
                            .map(DadosListagemContatoEmail::new)
                            .collect(Collectors.toList())
                        : List.of(),
                contato.getTelefones() != null
                        ? contato.getTelefones().stream()
                            .map(DadosListagemContatoTelefone::new)
                            .collect(Collectors.toList())
                        : List.of(),
                contato.getEndereco() != null ?
                        new DadosCadastroEndereco(
                                contato.getEndereco().getPais(),
                                contato.getEndereco().getUf(),
                                contato.getEndereco().getCidade(),
                                contato.getEndereco().getBairro(),
                                contato.getEndereco().getRua(),
                                contato.getEndereco().getNumero(),
                                contato.getEndereco().getComplemento(),
                                contato.getEndereco().getCep()
                        ) : null,
                contato.getDt_inclusao(),
                contato.getDt_alteracao(),
                contato.getCargo(),
                contato.getDepartamento(),
                contato.getObs(),
                contato.getFoto());
    }

}
