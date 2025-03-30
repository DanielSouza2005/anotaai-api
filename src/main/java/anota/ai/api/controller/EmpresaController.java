package anota.ai.api.controller;

import anota.ai.api.empresa.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @GetMapping
    public Page<DadosListagemEmpresa> listar(@PageableDefault(size = 10, sort={"razao", "fantasia", "cnpj"}) Pageable paginacao){
        return repository.findAllByAtivo(1, paginacao).map(DadosListagemEmpresa::new);
    }

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroEmpresa dados){
        repository.save(new Empresa(dados));
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoEmpresa dados){
        var empresa = repository.getReferenceById(dados.cod_empresa());
        empresa.atualizarDados(dados);
    }

    @DeleteMapping("/{cod_empresa}")
    @Transactional
    public void excluir(@PathVariable Long cod_empresa){
        var empresa = repository.getReferenceById(cod_empresa);
        empresa.excluir();
    }
}
