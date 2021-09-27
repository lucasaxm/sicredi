package com.sicredi.assembleia.services;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;

import java.util.List;

public interface SessaoService {
    List<Sessao> findAll();

    Sessao newSessao(Sessao sessao);

    void deleteSessao(String id) throws DataNotFoundException;

    Sessao findById(String id) throws DataNotFoundException;

    Sessao updateSessao(String id, SessaoDTO sessaoDTO) throws DataNotFoundException;

    List<Sessao> findByPauta(Pauta pauta);

    Sessao vote(String sessaoId, VotoDTO votoDTO) throws Exception;
}
