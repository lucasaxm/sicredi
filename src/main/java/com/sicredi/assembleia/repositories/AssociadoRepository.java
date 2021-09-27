package com.sicredi.assembleia.repositories;

import com.sicredi.assembleia.entities.Associado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssociadoRepository extends MongoRepository<Associado, String> {

    List<Associado> findByNomeContainsIgnoreCase(@Param("nome") String nome);

    Associado findByCpf(String cpf);
    List<Associado> findByCpfContainsIgnoreCase(String cpf);

    List<Associado> findByNomeContainsIgnoreCaseAndCpfContainsIgnoreCase(String nome, String cpf);
}
