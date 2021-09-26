package com.sicredi.assembleia.services;

import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;

import java.util.List;

public interface PautaService {
    List<Pauta> findAll();

    Pauta newPauta(Pauta pauta);

    Pauta replacePauta(String id, Pauta newPauta) throws DataNotFoundException;

    void deletePauta(String id) throws DataNotFoundException;

    List<Pauta> searchPauta(String titulo, String descricao) throws NoSearchParametersException;

    Pauta findById(String id) throws DataNotFoundException;

    Pauta updatePauta(String id, Pauta pauta) throws DataNotFoundException;
}
