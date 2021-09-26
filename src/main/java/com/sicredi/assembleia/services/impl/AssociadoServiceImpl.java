package com.sicredi.assembleia.services.impl;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;
import com.sicredi.assembleia.helpers.Utils;
import com.sicredi.assembleia.repositories.AssociadoRepository;
import com.sicredi.assembleia.services.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    @Autowired
    private AssociadoRepository repository;

    private final Utils utils = Utils.getInstance();

    @Override
    public List<Associado> findAll() {
        return repository.findAll();
    }

    @Override
    public Associado newAssociado(Associado associado) {
        return repository.save(associado);
    }

    @Override
    public Associado replaceAssociado(String id, Associado newAssociado) throws DataNotFoundException {
        Associado current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        current.setNome(newAssociado.getNome());
        current.setCpf(newAssociado.getCpf());
        return repository.save(current);
    }

    @Override
    public void deleteAssociado(String id) throws DataNotFoundException {
        repository.findById(id).orElseThrow(new DataNotFoundException(id));
        repository.deleteById(id);
    }

    @Override
    public List<Associado> searchAssociado(String cpf, String nome) throws NoSearchParametersException {
        if (utils.isNullOrEmpty(cpf)){
            if (!utils.isNullOrEmpty(nome)){
                return repository.findByNomeContainsIgnoreCase(nome);
            }
            throw new NoSearchParametersException();
        } else if (utils.isNullOrEmpty(nome)){
            return repository.findByCpfContainsIgnoreCase(cpf);
        } else {
            return repository.findByNomeContainsIgnoreCaseAndCpfContainsIgnoreCase(nome, cpf);
        }
    }

    @Override
    public Associado findById(String id) throws DataNotFoundException {
        return repository.findById(id).orElseThrow(new DataNotFoundException(id));
    }

    @Override
    public Associado updateAssociado(String id, Associado newAssociado) throws DataNotFoundException, NotUniqueException {
        Associado current = repository.findById(id).orElseThrow(new DataNotFoundException(id));
        if (!utils.isNullOrEmpty(newAssociado.getNome())) {
            current.setNome(newAssociado.getNome());
        }
        if (!utils.isNullOrEmpty(newAssociado.getCpf())) {
            Associado found = repository.findByCpf(newAssociado.getCpf());
            if (found != null && !found.getId().equals(id)){
                throw new NotUniqueException("cpf");
            }
            current.setCpf(newAssociado.getCpf());
        }
        return repository.save(current);
    }
}
