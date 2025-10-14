package anota.ai.api.controller;

import anota.ai.api.domain.foto.service.FotoService;
import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.usuario.dto.DadosAtualizacaoUsuario;
import anota.ai.api.domain.usuario.dto.DadosCadastroUsuario;
import anota.ai.api.domain.usuario.dto.DadosListagemUsuario;
import anota.ai.api.domain.usuario.model.Usuario;
import anota.ai.api.domain.usuario.repository.UsuarioRepository;
import anota.ai.api.infra.security.SecurityFilter;
import anota.ai.api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

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

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listarFiltrado(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {

        if ((nome == null || nome.isBlank()) && (email == null || email.isBlank())) {
            var usuarios = repository.findAllByAtivo(StatusAtivo.ATIVO.getCodigo(), paginacao)
                    .map(DadosListagemUsuario::new);
            return ResponseEntity.ok(usuarios);
        }

        Page<Usuario> usuarios = repository.buscarFiltrado(
                nome,
                email,
                StatusAtivo.ATIVO.getCodigo(),
                paginacao
        );

        return ResponseEntity.ok(usuarios.map(DadosListagemUsuario::new));
    }

    @GetMapping("/{cod_usuario}")
    public ResponseEntity<DadosListagemUsuario> listarPorId(@PathVariable Long cod_usuario) {
        var usuario = repository.getReferenceById(cod_usuario);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
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
    public ResponseEntity<?> excluir(@PathVariable Long cod_usuario,
                                     HttpServletRequest request) {
        var usuario = repository.getReferenceById(cod_usuario);

        String token = securityFilter.recuperarToken(request);
        String codigoUsuarioToken = tokenService.getCodigo(token);

        if (Objects.equals(codigoUsuarioToken, usuario.getCod_usuario().toString())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("X-Error-Code", "USUARIO_LOGADO_EXCLUSAO_BLOQUEADA")
                    .body("Não é possível excluir o Usuário logado.");
        } else {
            usuario.excluir();
            return ResponseEntity.noContent().build();
        }
    }
}
