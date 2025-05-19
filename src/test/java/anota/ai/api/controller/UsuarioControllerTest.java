package anota.ai.api.controller;

import anota.ai.api.domain.usuario.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroUsuario> dadosCadastroUsuarioJson;

    @Autowired
    private JacksonTester<DadosAtualizacaoUsuario> dadosAtualizacaoUsuarioJson;

    @Autowired
    private JacksonTester<DadosListagemUsuario> dadosListagemUsuarioJson;

    @MockitoBean
    private UsuarioRepository repository;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deveria devolver 400 ao tentar cadastrar com dados inválidos")
    @WithMockUser
    void cadastrarUsuarioCenario1() throws Exception {
        var response = mvc.perform(post("/usuario")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver 201 ao cadastrar com dados válidos")
    @WithMockUser
    void cadastrarUsuarioCenario2() throws Exception {
        var dados = new DadosCadastroUsuario("João da Silva", "123456", "joao@email.com");

        when(passwordEncoder.encode(any())).thenReturn("123456");
        var usuario = new Usuario(dados, passwordEncoder);

        when(repository.save(any())).thenReturn(usuario);

        var response = mvc.perform(
                post("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroUsuarioJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).contains("/usuario/");
    }

    @Test
    @DisplayName("Deveria devolver 200 ao listar usuários")
    @WithMockUser
    void listarUsuariosCenario1() throws Exception {
        var dados = new DadosCadastroUsuario("João", "123456", "joao@email.com");
        when(passwordEncoder.encode(any())).thenReturn("123456");
        var usuario = new Usuario(dados, passwordEncoder);
        usuario.setCod_usuario(1L);

        var usuarios = new PageImpl<>(List.of(usuario));

        when(repository.findAllByAtivo(eq(1), any(Pageable.class))).thenReturn(usuarios);

        var response = mvc.perform(get("/usuario")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("Deveria devolver 200 ao buscar usuário por ID existente")
    @WithMockUser
    void buscarUsuarioPorIdCenario1() throws Exception {
        var dados = new DadosCadastroUsuario("Maria", "123456", "maria@email.com");

        when(passwordEncoder.encode(any())).thenReturn("123456");
        var usuario = new Usuario(dados, passwordEncoder);
        usuario.setCod_usuario(1L);

        when(repository.getReferenceById(1L)).thenReturn(usuario);

        var response = mvc.perform(get("/usuario/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var esperado = dadosListagemUsuarioJson.write(new DadosListagemUsuario(usuario)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(esperado);
    }


    @Test
    @DisplayName("Deveria devolver 404 ao buscar usuário com ID inexistente")
    @WithMockUser
    void buscarUsuarioPorIdCenario2() throws Exception {
        when(repository.getReferenceById(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        var response = mvc.perform(get("/usuario/99")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao atualizar usuário com dados válidos")
    @WithMockUser
    void atualizarUsuario() throws Exception {
        var dados = new DadosAtualizacaoUsuario(1L, "Novo Nome", "novaSenha", "novo@email.com");

        var usuario = new Usuario(new DadosCadastroUsuario("Antigo", "senhaAntiga", "antigo@email.com"), passwordEncoder);
        usuario.setCod_usuario(1L);

        when(repository.getReferenceById(1L)).thenReturn(usuario);
        when(passwordEncoder.encode(any())).thenReturn("novaSenha");

        var response = mvc.perform(
                put("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAtualizacaoUsuarioJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Novo Nome", "novo@email.com");
    }

    @Test
    @DisplayName("Deveria devolver 204 ao excluir usuário existente")
    @WithMockUser
    void excluirUsuario() throws Exception {
        var usuario = new Usuario(new DadosCadastroUsuario("Usuário", "senha", "email@email.com"), passwordEncoder);
        usuario.setCod_usuario(1L);

        when(repository.getReferenceById(1L)).thenReturn(usuario);

        var response = mvc.perform(delete("/usuario/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao buscar usuário por ID")
    @WithMockUser
    void buscarUsuarioPorId() throws Exception {
        var usuario = new Usuario(new DadosCadastroUsuario("Nome", "senha", "email@email.com"), passwordEncoder);
        usuario.setCod_usuario(1L);

        when(repository.getReferenceById(1L)).thenReturn(usuario);

        var response = mvc.perform(get("/usuario/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Nome", "email@email.com");
    }
}