package anota.ai.api.controller;

import anota.ai.api.contato.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("contato")
public class ContatoController {

    @Autowired
    private ContatoRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemContato>> lista(@PageableDefault(size = 10, sort = {"nome", "cpf"}) Pageable paginacao) {
        Page<DadosListagemContato> contatos = repository.findAll(paginacao).map(DadosListagemContato::new);
        return ResponseEntity.ok(contatos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemContato> cadastrar(@RequestBody @Valid DadosCadastroContato dados) {
        Contato contato = repository.save(new Contato(dados));
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosListagemContato(contato));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestBody @Valid DadosAtualizacaoContato dados) {
        Optional<Contato> contatoOpt = repository.findById(dados.cod_contato());

        if (contatoOpt.isPresent()) {
            var contato = contatoOpt.get();
            contato.atualizarDados(dados);
            return ResponseEntity.ok(new DadosListagemContato(contato));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cod_contato}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long cod_contato){
        Optional<Contato> contatoOpt = repository.findById(cod_contato);

        if (contatoOpt.isPresent()) {
            contatoOpt.get().excluir();
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }
}
