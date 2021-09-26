package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.repositories.PautaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PautaRepository pautaRepository;

    @BeforeEach
    public void databaseSeed() {
        pautaRepository.saveAll(List.of(
                new Pauta(null, "TestTitulo1", "TestDescricao1", null),
                new Pauta(null, "TestTitulo2", "TestDescricao2", null),
                new Pauta(null, "TestTitulo3", "TestDescricao3", null),
                new Pauta(null, "TestTitulo4", "TestDescricao4", null)
        ));
    }

    @AfterEach
    public void databasePurge() {
        pautaRepository.deleteAll();
    }

    @Test
    void testTestGetAllPautas() throws Exception {
        mockMvc.perform(get("/pautas")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[*].id").value(everyItem(notNullValue())))
                .andExpect(content().string(containsString("TestTitulo")));
    }

    @Test
    void testNewPauta() throws Exception {
        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"titulo\": \"Test insert titulo\",\n" +
                                "  \"descricao\": \"Test insert descricao\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void testGetPautaById() throws Exception {
        Pauta pauta = pautaRepository.findAll().get(0);

        mockMvc.perform(get("/pautas/{id}", pauta.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pauta.getId()))
                .andExpect(jsonPath("$.titulo").value(pauta.getTitulo()))
                .andExpect(jsonPath("$.descricao").value(pauta.getDescricao()));
    }

    @Test
    void testDeletePautaById() throws Exception {
        Pauta pauta = pautaRepository.findAll().get(0);

        mockMvc.perform(delete("/pautas/{id}", pauta.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(pautaRepository.findById(pauta.getId()).isPresent());
    }

    @Test
    void testReplacePautaById() throws Exception {
        Pauta pauta = pautaRepository.findAll().get(0);

        mockMvc.perform(put("/pautas/{id}", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"titulo\": \"Test replace titulo\",\n" +
                                "  \"descricao\": \"Test replace descricao\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("location"));

        Optional<Pauta> replaced = pautaRepository.findById(pauta.getId());

        assertTrue(replaced.isPresent());
        assertEquals(pauta.getId(), replaced.get().getId());
        assertEquals("Test replace titulo", replaced.get().getTitulo());
        assertEquals("Test replace descricao", replaced.get().getDescricao());
    }

    @Test
    void testUpdatePautaById() throws Exception {
        Pauta pauta = pautaRepository.findAll().get(0);

        mockMvc.perform(patch("/pautas/{id}", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"descricao\": \"Test update descricao\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("location"));

        Optional<Pauta> updated = pautaRepository.findById(pauta.getId());

        assertTrue(updated.isPresent());
        assertEquals(pauta.getId(), updated.get().getId());
        assertEquals(pauta.getTitulo(), updated.get().getTitulo());
        assertEquals("Test update descricao", updated.get().getDescricao());
    }

    @Test
    void testSearchPauta() throws Exception {
        mockMvc.perform(get("/pautas/busca")
                        .param("titulo","TestTitulo1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].titulo").value("TestTitulo1"));

        mockMvc.perform(get("/pautas/busca")
                        .param("descricao","TestDescricao2"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].descricao").value("TestDescricao2"));

        mockMvc.perform(get("/pautas/busca")
                        .param("titulo","TestTitulo3")
                        .param("descricao","TestDescricao3"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].titulo").value("TestTitulo3"))
                .andExpect(jsonPath("$[:1].descricao").value("TestDescricao3"));

        mockMvc.perform(get("/pautas/busca")
                        .param("titulo","TestTitulo"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}