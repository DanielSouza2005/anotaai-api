package anota.ai.api.controller;

import anota.ai.api.contato.Contato;
import anota.ai.api.contato.ContatoRepository;
import anota.ai.api.contato.DadosCadastroContato;
import anota.ai.api.contato.DadosListagemContato;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contato")
public class ContatoController {

    @Autowired
    private ContatoRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemContato>> lista(@PageableDefault(size = 10, sort = {"nome", "cpf"}) Pageable paginacao){
        Page<DadosListagemContato> contatos = repository.findAll(paginacao).map(DadosListagemContato::new);
        return ResponseEntity.ok(contatos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemContato> cadastrar(@RequestBody @Valid DadosCadastroContato dados){
        Contato contato = repository.save(new Contato(dados));
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosListagemContato(contato));
    }
}
