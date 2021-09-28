package com.sicredi.assembleia.services.impl;

import com.sicredi.assembleia.dto.NewPautaDTO;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.helpers.Utils;
import com.sicredi.assembleia.repositories.PautaRepository;
import com.sicredi.assembleia.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaServiceImpl implements PautaService {

    @Autowired
    private PautaRepository repository;

    private final Utils utils = Utils.getInstance();

    @Override
    public List<Pauta> findAll() {
        return repository.findAll();
    }

    @Override
    public Pauta newPauta(NewPautaDTO newPautaDTO) {
        return repository.save(new Pauta(newPautaDTO));
    }

    @Override
    public Pauta replacePauta(String id, NewPautaDTO newPautaDTO) throws DataNotFoundException {
        Pauta current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        Pauta newPauta = new Pauta(newPautaDTO);
        newPauta.setId(current.getId());
        return repository.save(newPauta);
    }

    @Override
    public void deletePauta(String id) throws DataNotFoundException {
        repository.findById(id).orElseThrow(new DataNotFoundException(id));
        repository.deleteById(id);
    }

    @Override
    public List<Pauta> searchPauta(String titulo, String descricao) throws NoSearchParametersException {
        if (utils.isNullOrEmpty(titulo)){
            if (!utils.isNullOrEmpty(descricao)){
                return repository.findByDescricaoContainsIgnoreCase(descricao);
            }
            throw new NoSearchParametersException();
        } else if (utils.isNullOrEmpty(descricao)){
            return repository.findByTituloContainsIgnoreCase(titulo);
        } else {
            return repository.findByTituloContainsIgnoreCaseAndDescricaoContainsIgnoreCase(titulo, descricao);
        }
    }

    @Override
    public Pauta findById(String id) throws DataNotFoundException {
        return repository.findById(id).orElseThrow(new DataNotFoundException(id));
    }

    @Override
    public Pauta updatePauta(String id, NewPautaDTO newPautaDTO) throws DataNotFoundException {
        Pauta current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        if (!utils.isNullOrEmpty(newPautaDTO.getDescricao())) {
            current.setDescricao(newPautaDTO.getDescricao());
        }
        if (!utils.isNullOrEmpty(newPautaDTO.getTitulo())) {
            current.setTitulo(newPautaDTO.getTitulo());
        }
        if (newPautaDTO.getAutor() != null) {
            current.setAutor(new Associado(newPautaDTO.getAutor().getId(), null, null));
        }
        return repository.save(current);
    }
}
