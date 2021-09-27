package com.sicredi.assembleia.services;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.dto.VotoEnum;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.helpers.Utils;
import com.sicredi.assembleia.repositories.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class SessaoServiceTest {

    @Autowired
    private SessaoService service;

    @MockBean
    private SessaoRepository sessaoRepository;

    @MockBean
    private AssociadoService associadoService;

    @Test
    void testFindAll() {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao first = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);
        Sessao second = new Sessao("2222", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.findAll()).thenReturn(List.of(first, second));

        List<Sessao> sessoes = service.findAll();

        assertNotNull(sessoes);
        assertEquals(2, sessoes.size());
    }

    @Test
    void testNewSessao() {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao withoutId = new Sessao(null, true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);
        Sessao withId = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.save(withoutId)).thenReturn(withId);

        Sessao sessao = service.newSessao(withoutId);

        assertNotNull(sessao);
        assertEquals(withId.getId(), sessao.getId());
    }

    @Test
    void testDeleteSessao() throws DataNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao old = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.findById(old.getId())).thenReturn(Optional.of(old));

        service.deleteSessao(old.getId());

        assertTrue(true);
    }

    @Test
    void testFindById() throws DataNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao first = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.findById(first.getId())).thenReturn(Optional.of(first));

        Sessao sessao = service.findById("1111");

        assertNotNull(sessao);
        assertEquals(first, sessao);
    }

    @Test
    void testUpdateSessao() throws DataNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao old = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        SessaoDTO replaceDuracao = new SessaoDTO(300L);
        Sessao expected = new Sessao(old.getId(), true, replaceDuracao.getDuracao(), now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.findById(old.getId())).thenReturn(Optional.of(old));
        when(sessaoRepository.save(expected)).thenReturn(expected);

        Sessao sessao = service.updateSessao(old.getId(), replaceDuracao);

        assertNotNull(sessao);
        assertEquals(expected, sessao);
    }

    @Test
    void testFindByPauta() {
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao first = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);
        Sessao second = new Sessao("2222", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);

        when(sessaoRepository.findByPauta(pauta)).thenReturn(List.of(first, second));

        List<Sessao> sessoes = service.findByPauta(pauta);

        assertNotNull(sessoes);
        assertEquals(2, sessoes.size());
    }

    @Test
    void testVote() throws Exception {
        Associado associado = new Associado("1111","2222","3333");
        VotoDTO votoDTO = new VotoDTO(VotoEnum.SIM, associado);
        LocalDateTime now = LocalDateTime.now();
        Pauta pauta = new Pauta("1111", "2222", "3333", null);
        Sessao sessao = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);
        Sessao expected = new Sessao("1111", true, 60L, now, 0L, 0L, new ArrayList<>(), pauta);
        expected.vote(votoDTO);

        when(sessaoRepository.findById(sessao.getId())).thenReturn(Optional.of(sessao));
        when(associadoService.findById(votoDTO.getAssociado().getId())).thenReturn(associado);

        //noinspection ResultOfMethodCallIgnored
        Mockito.mockStatic(Utils.class);

        when(sessaoRepository.save(expected)).thenReturn(expected);

        Sessao result = service.vote(sessao.getId(), votoDTO);

        assertNotNull(result);
        assertEquals(expected, result);

    }
}