package com.sicredi.assembleia.services;

import com.sicredi.assembleia.dto.NewAssociadoDTO;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;

import java.util.List;

public interface AssociadoService {
    List<Associado> findAll();

    Associado newAssociado(NewAssociadoDTO newAssociado);

    Associado replaceAssociado(String id, NewAssociadoDTO newAssociadoDTO) throws DataNotFoundException;

    void deleteAssociado(String id) throws DataNotFoundException;

    List<Associado> searchAssociado(String cpf, String nome) throws NoSearchParametersException;

    Associado findById(String id) throws DataNotFoundException;

    Associado updateAssociado(String id, NewAssociadoDTO newAssociadoDTO) throws DataNotFoundException;

    List<Pauta> findPautasByAssociadoId(String id) throws DataNotFoundException;
}
