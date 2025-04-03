package anota.ai.api.controller;

import anota.ai.api.empresa.DadosListagemEmpresa;
import anota.ai.api.empresa.Empresa;
import anota.ai.api.usuario.*;
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
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var usuarios = repository.findAll(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        var usuario = repository.save(new Usuario(dados));
        return ResponseEntity.status(201).body(new DadosListagemUsuario(usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {
        Optional<Usuario> usuarioOpt = repository.findById(dados.cod_usuario());

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            usuario.atualizarDados(dados);
            return ResponseEntity.ok(new DadosListagemUsuario(usuario));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cod_usuario}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long cod_usuario) {
        Optional<Usuario> usuarioOpt = repository.findById(cod_usuario);

        if (usuarioOpt.isPresent()) {
            usuarioOpt.get().excluir();
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }
}
