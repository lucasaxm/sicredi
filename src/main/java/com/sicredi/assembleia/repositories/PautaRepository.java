package com.sicredi.assembleia.repositories;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PautaRepository extends MongoRepository<Pauta, String> {
    List<Pauta> findByDescricaoContainsIgnoreCase(String descricao);

    List<Pauta> findByTituloContainsIgnoreCase(String titulo);

    List<Pauta> findByTituloContainsIgnoreCaseAndDescricaoContainsIgnoreCase(String titulo, String descricao);

    List<Pauta> findByDescricao(String descricao);

    List<Pauta> findByAutor(Associado autor);
}
