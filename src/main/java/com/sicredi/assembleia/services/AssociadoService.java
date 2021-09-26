package com.sicredi.assembleia.services;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;

import java.util.List;

public interface AssociadoService {
    List<Associado> findAll();

    Associado newAssociado(Associado associado);

    Associado replaceAssociado(String id, Associado newAssociado) throws DataNotFoundException;

    void deleteAssociado(String id) throws DataNotFoundException;

    List<Associado> searchAssociado(String cpf, String nome) throws NoSearchParametersException;

    Associado findById(String id) throws DataNotFoundException;

    Associado updateAssociado(String id, Associado associado) throws DataNotFoundException, NotUniqueException;

    List<Pauta> findPautasByAssociadoId(String id) throws DataNotFoundException;
}
