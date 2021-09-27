package com.sicredi.assembleia.repositories;

import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SessaoRepository extends MongoRepository<Sessao, String> {
    List<Sessao> findByPauta(Pauta pauta);
}
