package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.dto.NewPautaDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.ErrorMessages;
import com.sicredi.assembleia.errorhandling.apierrors.ApiError;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.services.PautaService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pautas")
@Api(value = "Pautas Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"pautas"}, description = "Pautas API")
public class PautaController {
    @Autowired
    private PautaService service;

    @GetMapping
    @ApiOperation("Lista todas as pautas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Pauta.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public List<Pauta> getAllPautas() {
        return service.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Cadastra nova pauta", tags = {"pautas", "associados"})
    @ApiResponses({
            @ApiResponse(code = 201, message = "Sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<Pauta> newPauta(
            @Valid
            @RequestBody
            @ApiParam(value = "Informações da nova Pauta.", name = "Criação de Pauta")
                    NewPautaDTO newPautaDTO
    ) {
        Pauta saved = service.newPauta(newPautaDTO);
        URI location = getPautaURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Exibe pauta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public Pauta getPautaById(
            @PathVariable
            @ApiParam(value = "ID da Pauta.", name = "id")
                    String id
    ) throws DataNotFoundException {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Deleta pauta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public ResponseEntity<?> deletePautaById(
            @PathVariable
            @ApiParam(value = "ID da Pauta.", name = "id")
                    String id
    ) throws DataNotFoundException {
        service.deletePauta(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Substitui completamente atributos da pauta mantendo o mesmo id", tags = {"pautas", "associados"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> replacePautaById(
            @PathVariable
                    String id,
            @Valid
            @RequestBody
            @ApiParam(value = "Informações da nova Pauta.", name = "Criação de Pauta")
                    NewPautaDTO newPautaDTO
    ) throws DataNotFoundException {
        return ResponseEntity.ok().location(getPautaURI(service.replacePauta(id, newPautaDTO))).build();
    }

    @PatchMapping(value = "/{id}")
    @ApiOperation(value = "Substitui parcialmente atributos da pauta mantendo o mesmo id", tags = {"pautas", "associados"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> updatePautaById(
            @PathVariable
                    String id,
            @RequestBody
            @ApiParam(value = "Informações da nova Pauta.", name = "Criação de Pauta")
                    NewPautaDTO newPautaDTO
    ) throws DataNotFoundException {
        return ResponseEntity.ok().location(getPautaURI(service.updatePauta(id, newPautaDTO))).build();
    }

    @GetMapping(value = "/busca")
    @ApiOperation("Busca pautas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 400, message = ErrorMessages.NO_SEARCH_PARAMS, response = ApiError.class)
    })
    public List<Pauta> searchPauta(
            @RequestParam(required = false, value = "titulo")
            @ApiParam(value = "Aceita partes do titulo e não considera letras maíusculas e minúsculas.", name = "Titulo da Pauta")
                    String titulo,
            @RequestParam(required = false, value = "descricao")
            @ApiParam(value = "Aceita partes da descrição e não considera letras maíusculas e minúsculas.", name = "Descrição da Pauta")
                    String descricao) throws NoSearchParametersException {
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
