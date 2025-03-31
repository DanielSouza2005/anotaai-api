package anota.ai.api.controller;

import anota.ai.api.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public Page<DadosListagemUsuario> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return repository.findAllByAtivo(1, paginacao).map(DadosListagemUsuario::new);
    }

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        repository.save(new Usuario(dados));
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {
        var usuario = repository.getReferenceById(dados.cod_usuario());
        usuario.atualizarDados(dados);
    }

    @DeleteMapping("/{cod_usuario}")
    @Transactional
    public void excluir(@PathVariable Long cod_usuario) {
        var usuario = repository.getReferenceById(cod_usuario);
        usuario.excluir();
    }
}
