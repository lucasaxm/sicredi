package com.sicredi.assembleia.services;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;
import com.sicredi.assembleia.repositories.AssociadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AssociadoServiceTest {

    @Autowired
    private AssociadoService service;

    @MockBean
    private AssociadoRepository repository;

    @Test
    void testFindAll() {
        Associado first = new Associado("1111", "2222", "3333");
        Associado second = new Associado("4444", "5555", "6666");

        when(repository.findAll()).thenReturn(List.of(first, second));

        List<Associado> associados = service.findAll();

        assertNotNull(associados);
        assertEquals(2, associados.size());
    }

    @Test
    void testNewAssociado() {
        Associado withoutId = new Associado(null,"2222", "3333");
        Associado withId = new Associado("1111", "2222", "3333");

        when(repository.save(withoutId)).thenReturn(withId);

        Associado associado = service.newAssociado(withoutId);

        assertNotNull(associado);
        assertEquals(withoutId.getNome(), associado.getNome());
        assertEquals(withoutId.getCpf(), associado.getCpf());
        assertEquals(withId.getId(), associado.getId());
    }

    @Test
    void tetsReplaceAssociado() throws DataNotFoundException {
        Associado old = new Associado("1111", "2222", "3333");
        Associado replace = new Associado(null,"0000", "9999");
        Associado expected = new Associado(old.getId(),"0000", "9999");

        when(repository.findById(old.getId())).thenReturn(Optional.of(old));
        when(repository.save(expected)).thenReturn(expected);

        Associado associado = service.replaceAssociado(old.getId(), replace);

        assertNotNull(associado);
        assertEquals(expected, associado);
    }

    @Test
    void testDeleteAssociado() throws DataNotFoundException {
        Associado old = new Associado("1111", "2222", "3333");

        when(repository.findById(old.getId())).thenReturn(Optional.of(old));

        service.deleteAssociado(old.getId());

        assertTrue(true);
    }

    @Test
    void testSearchAssociado() throws NoSearchParametersException {
        Associado first = new Associado("1111", "2222", "3333");
        Associado second = new Associado("4444", "5555", "6666");
        Associado third = new Associado("7777", "5555", "3399");

        when(repository.findByNomeContainsIgnoreCaseAndCpfContainsIgnoreCase("2222", "3333")).thenReturn(List.of(first));
        when(repository.findByCpfContainsIgnoreCase("33")).thenReturn(List.of(first,third));
        when(repository.findByNomeContainsIgnoreCase("5555")).thenReturn(List.of(second,third));

        List<Associado> associados = service.searchAssociado("3333","2222");

        assertNotNull(associados);
        assertEquals(1, associados.size());
        assertEquals(first.getId(), associados.get(0).getId());

        associados = service.searchAssociado("33",null);

        assertNotNull(associados);
        assertEquals(2, associados.size());
        assertTrue(associados.stream().anyMatch(associado -> associado.getId().equals(first.getId())));
        assertTrue(associados.stream().anyMatch(associado -> associado.getId().equals(third.getId())));

        associados = service.searchAssociado(null,"5555");

        assertNotNull(associados);
        assertEquals(2, associados.size());
        assertTrue(associados.stream().anyMatch(associado -> associado.getId().equals(second.getId())));
        assertTrue(associados.stream().anyMatch(associado -> associado.getId().equals(third.getId())));

        assertThrows(NoSearchParametersException.class,
                () -> service.searchAssociado(null,null)
        );
    }

    @Test
    void testFindById() throws DataNotFoundException {
        Associado first = new Associado("1111", "2222", "3333");

        when(repository.findById(first.getId())).thenReturn(Optional.of(first));

        Associado associado = service.findById("1111");

        assertNotNull(associado);
        assertEquals(first, associado);
    }

    @Test
    void testUpdateAssociado() throws DataNotFoundException, NotUniqueException {
        Associado old = new Associado("1111", "2222", "3333");

        Associado replaceCpf = new Associado(null,null, "9999");
        Associado expected1 = new Associado(old.getId(),"2222", "9999");

        Associado replaceNome = new Associado(null,"0000", null);
        Associado expected2 = new Associado(expected1.getId(),"0000", "9999");

        when(repository.findById(old.getId())).thenReturn(Optional.of(old));
        when(repository.findByCpf(replaceCpf.getCpf())).thenReturn(null);
        when(repository.save(expected1)).thenReturn(expected1);

        Associado associado = service.updateAssociado(old.getId(), replaceCpf);

        assertNotNull(associado);
        assertEquals(expected1, associado);

        when(repository.findById(expected1.getId())).thenReturn(Optional.of(expected1));
        when(repository.save(expected2)).thenReturn(expected2);

        associado = service.updateAssociado(expected1.getId(), replaceNome);

        assertNotNull(associado);
        assertEquals(expected2, associado);

        replaceCpf.setCpf("5555");
        Associado conflict = new Associado("5555", "5555", "5555");

        when(repository.findByCpf(replaceCpf.getCpf())).thenReturn(conflict);

        assertThrows(NotUniqueException.class,
                () -> service.updateAssociado(expected2.getId(), replaceCpf)
        );

    }
}