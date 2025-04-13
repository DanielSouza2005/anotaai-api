package anota.ai.api.controller;

import anota.ai.api.domain.contato.*;
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
@RequestMapping("contato")
@SecurityRequirement(name = "bearer-key")
public class ContatoController {

    @Autowired
    private ContatoRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemContato>> listar(@PageableDefault(size = 10, sort = {"nome", "cpf"}) Pageable paginacao) {
        Page<DadosListagemContato> contatos = repository.findAll(paginacao).map(DadosListagemContato::new);

        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{cod_contato}")
    public ResponseEntity<DadosListagemContato> listarPorId(@PathVariable Long cod_contato) {
        var contato = repository.getReferenceById(cod_contato);

        return ResponseEntity.ok(new DadosListagemContato(contato));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemContato> cadastrar(@RequestBody @Valid DadosCadastroContato dados,
                                                          UriComponentsBuilder uriBuilder) {
        Contato contato = repository.save(new Contato(dados));
        var uri = uriBuilder.path("/contato/{cod_contato}").buildAndExpand(contato.getCod_contato()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemContato(contato));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid
                                    DadosAtualizacaoContato dados) {
        var contato = repository.getReferenceById(dados.cod_contato());
        contato.atualizarDados(dados);

        return ResponseEntity.ok(new DadosListagemContato(contato));
    }

    @DeleteMapping("/{cod_contato}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long cod_contato) {
        var contato = repository.getReferenceById(cod_contato);
        contato.excluir();

        return ResponseEntity.noContent().build();
    }
}
