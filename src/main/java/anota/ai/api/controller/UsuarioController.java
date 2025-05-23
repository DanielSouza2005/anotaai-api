package anota.ai.api.controller;

import anota.ai.api.domain.foto.FotoService;
import anota.ai.api.domain.usuario.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FotoService fotoService;

    @Value("${upload.dir.usuario}")
    private String uploadDir;

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

    @GetMapping("/foto/{cod_usuario}")
    public ResponseEntity<?> servirFoto(@PathVariable Long cod_usuario) throws IOException {
        var usuario = repository.getReferenceById(cod_usuario);
        Path caminho = Paths.get(uploadDir).toAbsolutePath().resolve(usuario.getFoto()).normalize();

        if (!Files.exists(caminho)) {
            return ResponseEntity.notFound().build();
        }

        Resource recurso = new UrlResource((caminho).toUri());
        String contentType = Files.probeContentType(caminho);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(recurso);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestPart("dados") @Valid DadosCadastroUsuario dados,
                                                          @RequestPart(value = "foto", required = false) MultipartFile foto,
                                                          UriComponentsBuilder uriBuilder) throws IOException {
        var usuario = new Usuario(dados, passwordEncoder);
        fotoService.salvarFoto(usuario, foto);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuario/{cod_usuario}").buildAndExpand(usuario.getCod_usuario()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> atualizar(@RequestPart("dados") @Valid DadosAtualizacaoUsuario dados,
                                       @RequestPart(value = "foto", required = false) MultipartFile foto) throws IOException {
        var usuario = repository.getReferenceById(dados.cod_usuario());
        usuario.atualizarDados(dados, passwordEncoder);
        fotoService.salvarFoto(usuario, foto);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/{cod_usuario}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long cod_usuario) {
        var usuario = repository.getReferenceById(cod_usuario);
        usuario.excluir();

        return ResponseEntity.noContent().build();
    }
}
