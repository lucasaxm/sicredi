package com.sicredi.assembleia.services;

import com.sicredi.assembleia.dto.NewPautaDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;

import java.util.List;

public interface PautaService {
    List<Pauta> findAll();

    Pauta newPauta(NewPautaDTO newPautaDTO);

    Pauta replacePauta(String id, NewPautaDTO newPautaDTO) throws DataNotFoundException;

    void deletePauta(String id) throws DataNotFoundException;

    List<Pauta> searchPauta(String titulo, String descricao) throws NoSearchParametersException;

    Pauta findById(String id) throws DataNotFoundException;

    Pauta updatePauta(String id, NewPautaDTO newPautaDTO) throws DataNotFoundException;
}
