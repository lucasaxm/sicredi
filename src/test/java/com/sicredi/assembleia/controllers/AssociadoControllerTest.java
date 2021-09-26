package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.repositories.AssociadoRepository;
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
class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AssociadoRepository associadoRepository;

    @Autowired
    PautaRepository pautaRepository;

    @BeforeEach
    public void databaseSeed() {
        associadoRepository.saveAll(List.of(
            new Associado(null, "TestNome1", "TestCpf1"),
            new Associado(null, "TestNome2", "TestCpf2"),
            new Associado(null, "TestNome3", "TestCpf3"),
            new Associado(null, "TestNome4", "TestCpf4")
        ));
    }

    @AfterEach
    public void databasePurge() {
        associadoRepository.deleteAll();
    }

    @Test
    void testGetAllAssociados() throws Exception {
        mockMvc.perform(get("/associados")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[*].id").value(everyItem(notNullValue())))
                .andExpect(content().string(containsString("TestNome")));
    }

    @Test
    void newAssociado() throws Exception {
        mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"nome\": \"Test insert nome\",\n" +
                                "  \"cpf\": \"Test insert cpf\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void getAssociadoById() throws Exception {
        Associado associado = associadoRepository.findAll().get(0);

        mockMvc.perform(get("/associados/{id}", associado.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(associado.getId()))
                .andExpect(jsonPath("$.nome").value(associado.getNome()))
                .andExpect(jsonPath("$.cpf").value(associado.getCpf()));
    }

    @Test
    void deleteAssociadoById() throws Exception {
        Associado associado = associadoRepository.findAll().get(0);

        mockMvc.perform(delete("/associados/{id}", associado.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(associadoRepository.findById(associado.getId()).isPresent());
    }

    @Test
    void replaceAssociadoById() throws Exception {
        Associado associado = associadoRepository.findAll().get(0);

        mockMvc.perform(put("/associados/{id}", associado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"nome\": \"Test replace nome\",\n" +
                                "  \"cpf\": \"Test replace cpf\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("location"));

        Optional<Associado> replaced = associadoRepository.findById(associado.getId());

        assertTrue(replaced.isPresent());
        assertEquals(associado.getId(), replaced.get().getId());
        assertEquals("Test replace nome", replaced.get().getNome());
        assertEquals("Test replace cpf", replaced.get().getCpf());
    }

    @Test
    void updateAssociadoById() throws Exception {
        Associado associado = associadoRepository.findAll().get(0);

        mockMvc.perform(patch("/associados/{id}", associado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"cpf\": \"Test update cpf\"\n" +
                                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("location"));

        Optional<Associado> updated = associadoRepository.findById(associado.getId());

        assertTrue(updated.isPresent());
        assertEquals(associado.getId(), updated.get().getId());
        assertEquals(associado.getNome(), updated.get().getNome());
        assertEquals("Test update cpf", updated.get().getCpf());
    }

    @Test
    void searchAssociado() throws Exception {
        mockMvc.perform(get("/associados/busca")
                        .param("nome","TestNome1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].nome").value("TestNome1"));

        mockMvc.perform(get("/associados/busca")
                        .param("cpf","TestCpf2"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].cpf").value("TestCpf2"));

        mockMvc.perform(get("/associados/busca")
                        .param("nome","TestNome3")
                        .param("cpf","TestCpf3"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].nome").value("TestNome3"))
                .andExpect(jsonPath("$[:1].cpf").value("TestCpf3"));

        mockMvc.perform(get("/associados/busca")
                        .param("nome","TestNome"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void testGetPautasByAssociadoId() throws Exception {
        Associado associado = associadoRepository.findAll().get(0);
        Pauta pauta = pautaRepository.save(new Pauta(null, "testTitulo1", "testDescricao1", associado));

        mockMvc.perform(get("/associados/{id}/pautas", associado.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[:1].id").value(pauta.getId()))
                .andExpect(jsonPath("$[:1].titulo").value(pauta.getTitulo()))
                .andExpect(jsonPath("$[:1].descricao").value(pauta.getDescricao()))
                .andExpect(jsonPath("$[:1].autor.id").value(associado.getId()));
    }
}