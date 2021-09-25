package com.sicredi.assembleia.repositories;

import com.sicredi.assembleia.entities.Associado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "associados", path = "associados")
public interface AssociadoRepository extends MongoRepository<Associado, String> {
    List<Associado> findByNome(@Param("nome") String nome);

    @RestResource(exported = false)
    List<Associado> findByCpf(String cpf);
}
