package com.sicredi.assembleia.services.impl;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.SessionClosedException;
import com.sicredi.assembleia.errorhandling.exceptions.VotingAgainException;
import com.sicredi.assembleia.helpers.Utils;
import com.sicredi.assembleia.repositories.SessaoRepository;
import com.sicredi.assembleia.services.AssociadoService;
import com.sicredi.assembleia.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessaoServiceImpl implements SessaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private AssociadoService associadoService;

    @Override
    public List<Sessao> findAll() {
        return sessaoRepository.findAll();
    }

    @Override
    public Sessao newSessao(Sessao sessao) {
        return sessaoRepository.save(sessao);
    }

    @Override
    public void deleteSessao(String id) throws DataNotFoundException {
        sessaoRepository.findById(id).orElseThrow(new DataNotFoundException(id));
        sessaoRepository.deleteById(id);
    }

    @Override
    public Sessao findById(String id) throws DataNotFoundException {
        return updateStatus(sessaoRepository.findById(id).orElseThrow(new DataNotFoundException(id)));
    }

    @Override
    public Sessao updateSessao(String id, SessaoDTO sessaoDTO) throws DataNotFoundException {
        Sessao current = updateStatus(sessaoRepository.findById(id).orElseThrow(new DataNotFoundException(id)));
        if (sessaoDTO.getDuracao() != null) {
            current.setDuracao(sessaoDTO.getDuracao());
        }
        return sessaoRepository.save(current);
    }

    @Override
    public List<Sessao> findByPauta(Pauta pauta) {
        return sessaoRepository.findByPauta(pauta).stream().map(this::updateStatus).collect(Collectors.toList());
    }

    @Override
    public Sessao vote(String sessaoId, VotoDTO votoDTO) throws Exception {
        Sessao sessao = this.findById(sessaoId);
        if (!sessao.getAberta()) throw new SessionClosedException(sessao.getId());

        votoDTO.setAssociado(associadoService.findById(votoDTO.getAssociado().getId()));

        if (sessao.getVotantes().contains(votoDTO.getAssociado())) throw new VotingAgainException(votoDTO.getAssociado().getId());
        Utils.cpfAbleToVote(votoDTO.getAssociado().getCpf());
        return sessaoRepository.save(sessao.vote(votoDTO));
    }

    private Sessao updateStatus(Sessao sessao) {
        if (sessao.getAberta()){
            long difference = Duration.between(sessao.getInicio(), LocalDateTime.now()).getSeconds();
            if (difference > sessao.getDuracao()){
                sessao.setAberta(false);
                sessao = sessaoRepository.save(sessao);
            }
        }
        return sessao;
    }
}
