package anota.ai.api.controller;

import anota.ai.api.domain.contato.*;
import anota.ai.api.domain.endereco.DadosCadastroEndereco;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ContatoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroContato> dadosCadastroContatoJson;

    @Autowired
    private JacksonTester<DadosAtualizacaoContato> dadosAtualizacaoContatoJson;

    @Autowired
    private JacksonTester<DadosListagemContato> dadosListagemContatoJson;

    @MockitoBean
    private ContatoRepository repository;

    private DadosCadastroContato criarDadosContato() {
        return new DadosCadastroContato(
                "João Silva", 1L, "12345678901", "11999999999", "1133334444", "1133335555",
                "joao@email.com", "joao@empresa.com", new DadosCadastroEndereco("Brasil", "SP", "Americana", "Centro", "Rua X", "123", "", "13465120"),
                "Analista", "TI", "Sem observações"
        );
    }

    private Contato criarContato(DadosCadastroContato dados) {
        var contato = new Contato(dados);
        contato.setCod_contato(1L);
        return contato;
    }

    @Test
    @DisplayName("Deveria devolver 400 ao tentar cadastrar contato com dados inválidos")
    @WithMockUser
    void cadastrarContatoCenario1() throws Exception {
        var response = mvc.perform(post("/contato")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver 201 ao cadastrar contato com dados válidos")
    @WithMockUser
    void cadastrarContatoCenario2() throws Exception {
        var dados = criarDadosContato();
        var contato = criarContato(dados);

        when(repository.save(any())).thenReturn(contato);

        var response = mvc.perform(
                post("/contato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroContatoJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).contains("/contato/");
    }

    @Test
    @DisplayName("Deveria devolver 200 ao listar contatos")
    @WithMockUser
    void listarContatos() throws Exception {
        var dados = criarDadosContato();
        var contato = criarContato(dados);

        var contatos = new PageImpl<>(List.of(contato));

        when(repository.findAll(any(Pageable.class))).thenReturn(contatos);

        var response = mvc.perform(get("/contato")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao buscar contato por ID existente")
    @WithMockUser
    void buscarContatoPorIdCenario1() throws Exception {
        var contato = criarContato(criarDadosContato());

        when(repository.getReferenceById(1L)).thenReturn(contato);

        var response = mvc.perform(get("/contato/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var esperado = dadosListagemContatoJson.write(new DadosListagemContato(contato)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(esperado);
    }

    @Test
    @DisplayName("Deveria devolver 404 ao buscar contato com ID inexistente")
    @WithMockUser
    void buscarContatoPorIdCenario2() throws Exception {
        when(repository.getReferenceById(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        var response = mvc.perform(get("/contato/99")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao atualizar contato com dados válidos")
    @WithMockUser
    void atualizarContato() throws Exception {
        var dados = new DadosAtualizacaoContato(1L, "Novo Nome", 2L, "1133344455", "1133344466",
                "19999999999", "19999999999", "novoemail@email.com",
                "novo@empresa.com", null, "Consultor", "Comercial", "Teste");

        var contato = criarContato(criarDadosContato());

        when(repository.getReferenceById(1L)).thenReturn(contato);

        var response = mvc.perform(
                put("/contato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAtualizacaoContatoJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Novo Nome", "novoemail@email.com", "Novo Nome");
    }

    @Test
    @DisplayName("Deveria devolver 204 ao excluir contato existente")
    @WithMockUser
    void excluirContato() throws Exception {
        var contato = criarContato(criarDadosContato());

        when(repository.getReferenceById(1L)).thenReturn(contato);

        var response = mvc.perform(delete("/contato/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
