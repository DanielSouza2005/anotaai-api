package anota.ai.api.controller;

import anota.ai.api.domain.empresa.*;
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
    private EmpresaRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemEmpresa>> listar(@PageableDefault(size = 10, sort = {"razao", "fantasia", "cnpj"}) Pageable paginacao) {
        var empresas = repository.findAllByAtivo(1, paginacao).map(DadosListagemEmpresa::new);

        return ResponseEntity.ok(empresas);
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
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoEmpresa dados) {
        var empresa = repository.getReferenceById(dados.cod_empresa());
        empresa.atualizarDados(dados);

        return ResponseEntity.ok(new DadosListagemEmpresa(empresa));
    }

    @DeleteMapping("/{cod_empresa}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long cod_empresa) {
        var empresa = repository.getReferenceById(cod_empresa);
        empresa.excluir();

        return ResponseEntity.noContent().build();
    }
}
