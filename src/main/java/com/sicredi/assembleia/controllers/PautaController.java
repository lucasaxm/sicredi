package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pautas")
public class PautaController {
    @Autowired
    private PautaService service;

    @GetMapping
    public List<Pauta> getAllPautas() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Pauta> newPauta(@Valid @RequestBody Pauta pauta) {
        Pauta saved = service.newPauta(pauta);
        URI location = getPautaURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public Pauta getPautaById(@PathVariable String id) throws DataNotFoundException {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePautaById(@PathVariable String id) throws DataNotFoundException {
        service.deletePauta(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> replacePautaById(@PathVariable String id, @Valid @RequestBody Pauta pauta) throws DataNotFoundException {
        return ResponseEntity.ok().location(getPautaURI(service.replacePauta(id, pauta))).build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updatePautaById(@PathVariable String id, @RequestBody Pauta pauta) throws DataNotFoundException {
        return ResponseEntity.ok().location(getPautaURI(service.updatePauta(id, pauta))).build();
    }

    @GetMapping(value = "/busca")
    public List<Pauta> searchPauta(
            @RequestParam(required=false, value="titulo") String titulo,
            @RequestParam(required=false, value="descricao") String descricao) throws NoSearchParametersException {
        return service.searchPauta(titulo, descricao);
    }

    private URI getPautaURI(Pauta saved) {
        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/pautas/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
    }

}
