package com.sicredi.assembleia.services.impl;

import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;
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
    public Pauta newPauta(Pauta associado) {
        return repository.save(associado);
    }

    @Override
    public Pauta replacePauta(String id, Pauta newPauta) throws DataNotFoundException {
        Pauta current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        current.setAutor(newPauta.getAutor());
        current.setDescricao(newPauta.getDescricao());
        current.setTitulo(newPauta.getTitulo());
        return repository.save(current);
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
    public Pauta updatePauta(String id, Pauta newPauta) throws DataNotFoundException {
        Pauta current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        if (!utils.isNullOrEmpty(newPauta.getDescricao())) {
            current.setDescricao(newPauta.getDescricao());
        }
        if (!utils.isNullOrEmpty(newPauta.getTitulo())) {
            current.setTitulo(newPauta.getTitulo());
        }
        if (newPauta.getAutor() != null) {
            current.setAutor(newPauta.getAutor());
        }
        return repository.save(current);
    }
}
