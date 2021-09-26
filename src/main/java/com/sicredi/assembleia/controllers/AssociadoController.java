package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.errorhandling.exceptions.NotUniqueException;
import com.sicredi.assembleia.errorhandling.validators.AssociadoValidator;
import com.sicredi.assembleia.services.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/associados")
public class AssociadoController {

    @Autowired
    AssociadoValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @Autowired
    private AssociadoService service;

    @GetMapping
    public List<Associado> getAllAssociados() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Associado> newAssociado(@Valid @RequestBody Associado associado) {
        Associado saved = service.newAssociado(associado);
        URI location = getAssociadoURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public Associado getAssociadoById(@PathVariable String id) throws DataNotFoundException {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAssociadoById(@PathVariable String id) throws DataNotFoundException {
        service.deleteAssociado(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> replaceAssociadoById(@PathVariable String id, @Valid @RequestBody Associado associado) throws DataNotFoundException {
        return ResponseEntity.ok().location(getAssociadoURI(service.replaceAssociado(id, associado))).build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateAssociadoById(@PathVariable String id, @RequestBody Associado associado) throws DataNotFoundException, NotUniqueException {
        return ResponseEntity.ok().location(getAssociadoURI(service.updateAssociado(id, associado))).build();
    }

    @GetMapping(value = "/busca")
    public List<Associado> searchAssociado(
            @RequestParam(required=false, value="cpf") String cpf,
            @RequestParam(required=false, value="nome") String nome) throws NoSearchParametersException {
        return service.searchAssociado(cpf, nome);
    }

    private URI getAssociadoURI(Associado saved) {
        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/associados/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
    }
}
