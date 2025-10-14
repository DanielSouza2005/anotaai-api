package anota.ai.api.controller;

import anota.ai.api.domain.contato.repository.ContatoRepository;
import anota.ai.api.domain.empresa.dto.DadosCadastroEmpresa;
import anota.ai.api.domain.empresa.dto.DadosListagemEmpresa;
import anota.ai.api.domain.empresa.model.Empresa;
import anota.ai.api.domain.enums.StatusAtivo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("empresa")
@SecurityRequirement(name = "bearer-key")
public class EmpresaController {

    @Autowired
    private anota.ai.api.domain.empresa.repository.EmpresaRepository repository;

    @Autowired
    private ContatoRepository contatoRepository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemEmpresa>> listar(
            @RequestParam(required = false) String razao,
            @RequestParam(required = false) String fantasia,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String ie,
            @PageableDefault(size = 10, sort = {"razao", "fantasia", "cnpj"}) Pageable paginacao) {

        if ((razao == null || razao.isBlank()) &&
                (fantasia == null || fantasia.isBlank()) &&
                (cnpj == null || cnpj.isBlank()) &&
                (ie == null || ie.isBlank())) {
            var empresas = repository.findAllByAtivo(StatusAtivo.ATIVO.getCodigo(), paginacao).map(DadosListagemEmpresa::new);

            return ResponseEntity.ok(empresas);
        }

        Page<Empresa> empresas = repository.buscarFiltrado(
                razao,
                fantasia,
                cnpj,
                ie,
                StatusAtivo.ATIVO.getCodigo(),
                paginacao
        );

        return ResponseEntity.ok(empresas.map(DadosListagemEmpresa::new));
    }

    @GetMapping("/{cod_empresa}")
    public ResponseEntity<DadosListagemEmpresa> listarPorId(@PathVariable Long cod_empresa) {
        var empresa = repository.getReferenceById(cod_empresa);

        return ResponseEntity.ok(new DadosListagemEmpresa(empresa));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemEmpresa> cadastrar(@RequestBody @Valid DadosCadastroEmpresa dados,
                                                          UriComponentsBuilder uriBuilder) {
        var empresa = repository.save(new Empresa(dados));
        var uri = uriBuilder.path("/empresa/{cod_empresa}").buildAndExpand(empresa.getCod_empresa()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemEmpresa(empresa));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid anota.ai.api.domain.empresa.dto.DadosAtualizacaoEmpresa dados) {
        var empresa = repository.getReferenceById(dados.cod_empresa());
        empresa.atualizarDados(dados);

        return ResponseEntity.ok(new DadosListagemEmpresa(empresa));
    }

    @DeleteMapping("/{cod_empresa}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long cod_empresa) {
        var empresa = repository.getReferenceById(cod_empresa);

        empresa.excluir();
        contatoRepository.desvincularEmpresa(cod_empresa);

        return ResponseEntity.noContent().build();
    }
}
