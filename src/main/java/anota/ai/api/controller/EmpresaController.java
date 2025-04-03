package anota.ai.api.controller;

import anota.ai.api.empresa.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemEmpresa>> listar(@PageableDefault(size = 10, sort = {"razao", "fantasia", "cnpj"}) Pageable paginacao) {
        var empresas = repository.findAll(paginacao).map(DadosListagemEmpresa::new);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemEmpresa> cadastrar(@RequestBody @Valid DadosCadastroEmpresa dados) {
        var empresa = repository.save(new Empresa(dados));
        return ResponseEntity.status(201).body(new DadosListagemEmpresa(empresa));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestBody @Valid DadosAtualizacaoEmpresa dados) {
        Optional<Empresa> empresaOpt = repository.findById(dados.cod_empresa());

        if (empresaOpt.isPresent()) {
            var empresa = empresaOpt.get();
            empresa.atualizarDados(dados);
            return ResponseEntity.ok(new DadosListagemEmpresa(empresa));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cod_empresa}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long cod_empresa) {
        Optional<Empresa> empresaOpt = repository.findById(cod_empresa);

        if (empresaOpt.isPresent()) {
            empresaOpt.get().excluir();
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }
}
