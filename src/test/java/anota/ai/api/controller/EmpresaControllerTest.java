package anota.ai.api.controller;

import anota.ai.api.domain.empresa.*;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class EmpresaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroEmpresa> dadosCadastroEmpresaJson;

    @Autowired
    private JacksonTester<DadosAtualizacaoEmpresa> dadosAtualizacaoEmpresaJson;

    @Autowired
    private JacksonTester<DadosListagemEmpresa> dadosListagemEmpresaJson;

    @MockitoBean
    private EmpresaRepository repository;

    private DadosCadastroEmpresa criarDadosEmpresa() {
        return new DadosCadastroEmpresa(
                "Empresa XYZ",
                "XYZ Ltda",
                "12345678000199",
                "12345678",
                new DadosCadastroEndereco("Brasil", "SP", "São Paulo", "Centro", "Rua A", "100", "Apto 1", "01000000")
        );
    }

    private Empresa criarEmpresa(DadosCadastroEmpresa dados) {
        var empresa = new Empresa(dados);
        empresa.setCod_empresa(1L);
        return empresa;
    }

    @Test
    @DisplayName("Deveria devolver 400 ao tentar cadastrar com dados inválidos")
    @WithMockUser
    void cadastrarEmpresaCenarioDadosInvalidos() throws Exception {
        var response = mvc.perform(post("/empresa")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver 201 ao cadastrar com dados válidos")
    @WithMockUser
    void cadastrarEmpresaCenarioDadosValidos() throws Exception {
        var dados = criarDadosEmpresa();
        var empresa = criarEmpresa(dados);

        when(repository.save(any())).thenReturn(empresa);

        var response = mvc.perform(
                post("/empresa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroEmpresaJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).contains("/empresa/");
    }

    @Test
    @DisplayName("Deveria devolver 200 ao listar empresas")
    @WithMockUser
    void listarEmpresasCenario() throws Exception {
        var dados = criarDadosEmpresa();
        var empresa = criarEmpresa(dados);

        var empresas = new PageImpl<>(List.of(empresa));

        when(repository.findAllByAtivo(eq(1), any(Pageable.class))).thenReturn(empresas);

        var response = mvc.perform(get("/empresa")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao buscar empresa por ID existente")
    @WithMockUser
    void buscarEmpresaPorIdCenarioEmpresaExistente() throws Exception {
        var empresa = criarEmpresa(criarDadosEmpresa());

        when(repository.getReferenceById(1L)).thenReturn(empresa);

        var response = mvc.perform(get("/empresa/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var esperado = dadosListagemEmpresaJson.write(new DadosListagemEmpresa(empresa)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(esperado);
    }

    @Test
    @DisplayName("Deveria devolver 404 ao buscar empresa com ID inexistente")
    @WithMockUser
    void buscarEmpresaPorIdCenarioEmpresaInexistente() throws Exception {
        when(repository.getReferenceById(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        var response = mvc.perform(get("/empresa/99")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 ao atualizar empresa com dados válidos")
    @WithMockUser
    void atualizarEmpresa() throws Exception {
        var dados = new DadosAtualizacaoEmpresa(1L, "Nova Razão", "Nova Fantasia", "11122233344455", "87654321",
                new anota.ai.api.domain.endereco.DadosCadastroEndereco("Brasil", "RJ", "Rio de Janeiro", "Copacabana", "Rua B", "200", "Casa", "22000000"));

        var empresa = criarEmpresa(criarDadosEmpresa());

        when(repository.getReferenceById(1L)).thenReturn(empresa);

        var response = mvc.perform(
                put("/empresa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAtualizacaoEmpresaJson.write(dados).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Nova Razão", "Nova Fantasia", "11122233344455");
    }

    @Test
    @DisplayName("Deveria devolver 204 ao excluir empresa existente")
    @WithMockUser
    void excluirEmpresa() throws Exception {
        var empresa = criarEmpresa(criarDadosEmpresa());

        when(repository.getReferenceById(1L)).thenReturn(empresa);

        var response = mvc.perform(delete("/empresa/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
