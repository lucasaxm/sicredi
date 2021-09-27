package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.repositories.AssociadoRepository;
import com.sicredi.assembleia.repositories.PautaRepository;
import com.sicredi.assembleia.repositories.SessaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    SessaoRepository sessaoRepository;

    @Autowired
    AssociadoRepository associadoRepository;

    List<Pauta> pautas;
    Map<Pauta, List<Sessao>> sessoes;
    Associado associado;

    @BeforeEach
    public void databaseSeed() {
        this.associado = associadoRepository.save(new Associado(null, "TestNome1", "90988398001"));

        this.pautas = pautaRepository.saveAll(List.of(
                new Pauta(null, "TestTitulo1", "TestDescricao1", associado),
                new Pauta(null, "TestTitulo2", "TestDescricao2", associado)
        ));

        SessaoDTO sessaoDTO = new SessaoDTO();
        SessaoDTO sessaoDTO30 = new SessaoDTO(30L);

        this.sessoes = new HashMap<>();

        this.sessoes.put(this.pautas.get(0), sessaoRepository.saveAll(List.of(
                new Sessao(pautas.get(0), sessaoDTO),
                new Sessao(pautas.get(0), sessaoDTO30)
        )));

        this.sessoes.put(this.pautas.get(1), sessaoRepository.saveAll(List.of(
                new Sessao(pautas.get(1), sessaoDTO),
                new Sessao(pautas.get(1), sessaoDTO30),
                new Sessao(pautas.get(1), sessaoDTO)
        )));
    }

    @AfterEach
    public void databasePurge() {
        pautaRepository.deleteAll();
        sessaoRepository.deleteAll();
        associadoRepository.deleteAll();
    }

    @Test
    void testCreateSessao() throws Exception {
        mockMvc.perform(post("/pautas/{pautaId}/sessoes", this.pautas.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"duracao\": 300\n" +
                                "}"))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void testGetSessoes() throws Exception {
        mockMvc.perform(get("/pautas/{pautaId}/sessoes", this.pautas.get(0).getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].id").value(everyItem(notNullValue())));

        mockMvc.perform(get("/pautas/{pautaId}/sessoes", this.pautas.get(1).getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].id").value(everyItem(notNullValue())));
    }

    @Test
    void testGetSessaoStatus() throws Exception {
        Pauta pauta = this.pautas.get(0);
        Sessao sessao = this.sessoes.get(pauta).get(0);
        mockMvc.perform(get("/pautas/{pautaId}/sessoes/{sessaoId}", pauta.getId(), sessao.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessao.getId()))
                .andExpect(jsonPath("$.duracao").value(sessao.getDuracao()));
    }

    @Test
    void testVote() throws Exception {
        Pauta pauta = this.pautas.get(0);
        Sessao sessao = this.sessoes.get(pauta).get(0);

        MvcResult result = mockMvc.perform(post("/pautas/{pautaId}/sessoes/{sessaoId}", pauta.getId(), sessao.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"voto\": \"NAO\", \"associado\": { \"id\":\"%s\" }}", this.associado.getId())))
                .andDo(print())
                .andExpect(status().is(in(List.of(HttpStatus.OK.value(), HttpStatus.FORBIDDEN.value()))))
                .andReturn();
        assertNotNull(result);

        if (result.getResponse().getStatus() == HttpStatus.OK.value()){
            Optional<Sessao> updatedSessao = sessaoRepository.findById(sessao.getId());
            assertTrue(updatedSessao.isPresent());
            assertEquals(1L, updatedSessao.get().getNao());
            assertEquals(1, updatedSessao.get().getVotantes().size());
            assertEquals(associado, updatedSessao.get().getVotantes().get(0));
        }
    }

    @Test
    void testDeleteSessaoById() throws Exception {
        Pauta pauta = this.pautas.get(0);
        Sessao sessao = this.sessoes.get(pauta).get(0);

        mockMvc.perform(delete("/pautas/{pautaId}/sessoes/{sessaoId}", pauta.getId(), sessao.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(sessaoRepository.findById(sessao.getId()).isPresent());
    }

    @Test
    void testUpdateSessao() throws Exception {
        Pauta pauta = this.pautas.get(0);
        Sessao sessao = this.sessoes.get(pauta).get(0);

        mockMvc.perform(put("/pautas/{pautaId}/sessoes/{sessaoId}", pauta.getId(), sessao.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"duracao\": 300\n" +
                                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("location"));

        Optional<Sessao> updated = sessaoRepository.findById(sessao.getId());

        assertTrue(updated.isPresent());
        assertEquals(sessao.getId(), updated.get().getId());
        assertEquals(300L, updated.get().getDuracao());
    }
}