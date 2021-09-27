package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.services.PautaService;
import com.sicredi.assembleia.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pautas/{pautaId}/sessoes")
public class SessaoController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoService sessaoService;


    @PostMapping
    public ResponseEntity<?> createSessao(@PathVariable String pautaId, @Valid @RequestBody(required = false) SessaoDTO sessaoDto) throws DataNotFoundException {
        Pauta pauta = pautaService.findById(pautaId);
        Sessao sessao = new Sessao(pauta, sessaoDto);
        Sessao saved = sessaoService.newSessao(sessao);
        URI location = getSessaoURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<Sessao> getSessoes(@PathVariable String pautaId) throws DataNotFoundException {
        Pauta pauta = pautaService.findById(pautaId);

        return sessaoService.findByPauta(pauta);
    }

    @GetMapping(value = "/{sessaoId}")
    public Sessao getSessaoStatus(@PathVariable String sessaoId) throws DataNotFoundException {
        return sessaoService.findById(sessaoId);
    }

    @PostMapping(value = "/{sessaoId}")
    public ResponseEntity<?> vote(@PathVariable String sessaoId, @Valid @RequestBody VotoDTO votoDTO) throws Exception {
        return ResponseEntity.ok().location(getSessaoURI(sessaoService.vote(sessaoId, votoDTO))).build();
    }

    @DeleteMapping(value = "/{sessaoId}")
    public ResponseEntity<?> deleteSessaoById(@PathVariable String sessaoId) throws DataNotFoundException {
        sessaoService.deleteSessao(sessaoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{sessaoId}")
    public ResponseEntity<?> updateSessao(@PathVariable String sessaoId, @Valid @RequestBody SessaoDTO sessaoDto) throws Exception {
        return ResponseEntity.ok().location(getSessaoURI(sessaoService.updateSessao(sessaoId, sessaoDto))).build();
    }

    private URI getSessaoURI(Sessao saved) {
        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/pautas/{pautaId}/sessoes/{sessaoId}")
                .buildAndExpand(saved.getPauta().getId(), saved.getId())
                .toUri();
    }

}
