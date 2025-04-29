package anota.ai.api.controller;

import anota.ai.api.domain.usuario.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var usuarios = repository.findAllByAtivo(1, paginacao).map(DadosListagemUsuario::new);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{cod_usuario}")
    public ResponseEntity<DadosListagemUsuario> listarPorId(@PathVariable Long cod_usuario) {
        var usuario = repository.getReferenceById(cod_usuario);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                                          UriComponentsBuilder uriBuilder) {
        var usuario = repository.save(new Usuario(dados, passwordEncoder));
        var uri = uriBuilder.path("/usuario/{cod_usuario}").buildAndExpand(usuario.getCod_usuario()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {
        var usuario = repository.getReferenceById(dados.cod_usuario());
        usuario.atualizarDados(dados, passwordEncoder);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/{cod_usuario}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long cod_usuario) {
        var usuario = repository.getReferenceById(cod_usuario);
        usuario.excluir();

        return ResponseEntity.noContent().build();
    }
}
