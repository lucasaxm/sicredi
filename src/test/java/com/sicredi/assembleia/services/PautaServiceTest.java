package com.sicredi.assembleia.services;

import com.sicredi.assembleia.dto.IdWrapper;
import com.sicredi.assembleia.dto.NewPautaDTO;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.repositories.PautaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PautaServiceTest {

    @Autowired
    private PautaService service;

    @MockBean
    private PautaRepository pautaRepository;

    @Test
    void testFindAll() {
        Pauta first = new Pauta("1111", "2222", "3333", null);
        Pauta second = new Pauta("4444", "5555", "6666", null);

        when(pautaRepository.findAll()).thenReturn(List.of(first, second));

        List<Pauta> pautas = service.findAll();

        assertNotNull(pautas);
        assertEquals(2, pautas.size());
    }

    @Test
    void testNewPauta() {
        NewPautaDTO newPautaDTO = new NewPautaDTO("2222", "3333", null);
        Pauta beforeSave = new Pauta(newPautaDTO);
        Pauta afterSave = new Pauta(newPautaDTO);
        afterSave.setId("1111");

        when(pautaRepository.save(beforeSave)).thenReturn(afterSave);

        Pauta pauta = service.newPauta(newPautaDTO);

        assertNotNull(pauta);
        assertEquals(beforeSave.getTitulo(), pauta.getTitulo());
        assertEquals(beforeSave.getDescricao(), pauta.getDescricao());
        assertEquals(afterSave.getId(), pauta.getId());
    }

    @Test
    void testReplacePauta() throws DataNotFoundException {
        Pauta old = new Pauta("1111", "2222", "3333", null);
        NewPautaDTO replace = new NewPautaDTO("0000", "9999", null);
        Pauta expected = new Pauta("1111", "0000", "9999", null);

        when(pautaRepository.findById(old.getId())).thenReturn(Optional.of(old));
        when(pautaRepository.save(expected)).thenReturn(expected);

        Pauta pauta = service.replacePauta(old.getId(), replace);

        assertNotNull(pauta);
        assertEquals(expected, pauta);
    }

    @Test
    void testDeletePauta() throws DataNotFoundException {
        Pauta old = new Pauta("1111", "2222", "3333", null);

        when(pautaRepository.findById(old.getId())).thenReturn(Optional.of(old));

        service.deletePauta(old.getId());

        assertTrue(true);
    }

    @Test
    void searchPauta() throws NoSearchParametersException {
        Pauta first = new Pauta("1111", "2222", "3333", null);
        Pauta second = new Pauta("4444", "5555", "6666", null);
        Pauta third = new Pauta("7777", "5555", "3399", null);

        when(pautaRepository.findByTituloContainsIgnoreCaseAndDescricaoContainsIgnoreCase("2222", "3333")).thenReturn(List.of(first));
        when(pautaRepository.findByDescricaoContainsIgnoreCase("33")).thenReturn(List.of(first,third));
        when(pautaRepository.findByTituloContainsIgnoreCase("5555")).thenReturn(List.of(second,third));

        List<Pauta> pautas = service.searchPauta("2222","3333");

        assertNotNull(pautas);
        assertEquals(1, pautas.size());
        assertEquals(first.getId(), pautas.get(0).getId());

        pautas = service.searchPauta(null,"33");

        assertNotNull(pautas);
        assertEquals(2, pautas.size());
        assertTrue(pautas.stream().anyMatch(pauta -> pauta.getId().equals(first.getId())));
        assertTrue(pautas.stream().anyMatch(pauta -> pauta.getId().equals(third.getId())));

        pautas = service.searchPauta("5555",null);

        assertNotNull(pautas);
        assertEquals(2, pautas.size());
        assertTrue(pautas.stream().anyMatch(pauta -> pauta.getId().equals(second.getId())));
        assertTrue(pautas.stream().anyMatch(pauta -> pauta.getId().equals(third.getId())));

        assertThrows(NoSearchParametersException.class,
                () -> service.searchPauta(null,null)
        );
    }

    @Test
    void testFindById() throws DataNotFoundException {
        Pauta first = new Pauta("1111", "2222", "3333", null);

        when(pautaRepository.findById(first.getId())).thenReturn(Optional.of(first));

        Pauta pauta = service.findById("1111");

        assertNotNull(pauta);
        assertEquals(first, pauta);
    }

    @Test
    void testUpdatePauta() throws DataNotFoundException {
        Pauta old = new Pauta("1111", "2222", "3333", null);

        NewPautaDTO replaceDescricao = new NewPautaDTO(null, "9999", null);
        Pauta expected1 = new Pauta(old.getId(),"2222", "9999", null);

        NewPautaDTO replaceTitulo = new NewPautaDTO("0000", null, null);
        Pauta expected2 = new Pauta(expected1.getId(),"0000", "9999", null);

        IdWrapper autorIdWrapper = new IdWrapper("1111");
        NewPautaDTO replaceAutor = new NewPautaDTO(null, null, autorIdWrapper);
        Pauta replaceAutorPauta = new Pauta(expected1.getId(),"0000", "9999", new Associado(autorIdWrapper.getId(), null, null));
        Associado autor = new Associado("1111", "2222", "333");
        Pauta expected3 = new Pauta(expected1.getId(),"0000", "9999", autor);

        when(pautaRepository.findById(old.getId())).thenReturn(Optional.of(old));
        when(pautaRepository.save(expected1)).thenReturn(expected1);

        Pauta pauta = service.updatePauta(old.getId(), replaceDescricao);

        assertNotNull(pauta);
        assertEquals(expected1, pauta);

        when(pautaRepository.findById(expected1.getId())).thenReturn(Optional.of(expected1));
        when(pautaRepository.save(expected2)).thenReturn(expected2);

        pauta = service.updatePauta(expected1.getId(), replaceTitulo);

        assertNotNull(pauta);
        assertEquals(expected2, pauta);

        when(pautaRepository.findById(expected1.getId())).thenReturn(Optional.of(expected2));
        when(pautaRepository.save(replaceAutorPauta)).thenReturn(expected3);

        pauta = service.updatePauta(expected1.getId(), replaceAutor);

        assertNotNull(pauta);
        assertEquals(expected3, pauta);
    }
}