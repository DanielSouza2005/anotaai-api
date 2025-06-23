package anota.ai.api.controller;

import anota.ai.api.domain.contato.*;
import anota.ai.api.domain.foto.FotoService;
import anota.ai.api.domain.status.StatusAtivo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("contato")
@SecurityRequirement(name = "bearer-key")
public class ContatoController {

    @Autowired
    private ContatoRepository repository;

    @Autowired
    private FotoService fotoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemContato>> listar(@PageableDefault(size = 10, sort = {"nome", "cpf"}) Pageable paginacao) {
        Page<DadosListagemContato> contatos = repository.findAllByAtivo(StatusAtivo.ATIVO.getCodigo(), paginacao).map(DadosListagemContato::new);

        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{cod_contato}")
    public ResponseEntity<DadosListagemContato> listarPorId(@PathVariable Long cod_contato) {
        var contato = repository.getReferenceById(cod_contato);

        return ResponseEntity.ok(new DadosListagemContato(contato));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<DadosListagemContato> cadastrar(@RequestPart("dados") @Valid DadosCadastroContato dados,
                                                          @RequestPart(value = "foto", required = false) MultipartFile foto,
                                                          UriComponentsBuilder uriBuilder) throws IOException {
        var contato = new Contato(dados);
        fotoService.salvarFoto(contato, foto);
        repository.save(contato);

        var uri = uriBuilder.path("/contato/{cod_contato}").buildAndExpand(contato.getCod_contato()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemContato(contato));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> atualizar(@RequestPart("dados") @Valid DadosAtualizacaoContato dados,
                                       @RequestPart(value = "foto", required = false) MultipartFile foto) throws IOException {
        var contato = repository.getReferenceById(dados.cod_contato());
        contato.atualizarDados(dados);
        fotoService.salvarFoto(contato, foto);

        return ResponseEntity.ok(new DadosListagemContato(contato));
    }

    @DeleteMapping("/{cod_contato}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long cod_contato) {
        var contato = repository.getReferenceById(cod_contato);
        contato.excluir();

        return ResponseEntity.noContent().build();
    }
}
