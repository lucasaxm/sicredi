package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.dto.NewAssociadoDTO;
import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.ErrorMessages;
import com.sicredi.assembleia.errorhandling.apierrors.ApiError;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.errorhandling.exceptions.NoSearchParametersException;
import com.sicredi.assembleia.services.AssociadoService;
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

/**
 * Controlador de operações relacionadas a manipulação de associados
 */
@RestController
@RequestMapping("/associados")
@Api(value = "Associados Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"associados"}, description = "Associados API")
public class AssociadoController {

    @Autowired
    private AssociadoService service;

    /**
     * @return Lista com todos os associados
     */
    @GetMapping
    @ApiOperation("Lista todos os associados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Associado.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public List<Associado> getAllAssociados() {
        return service.findAll();
    }

    @PostMapping
    @ApiOperation("Cadastra novo associado")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<Associado> newAssociado(
            @Valid
            @RequestBody
            @ApiParam(value = "Informações do novo Associado.", name = "Criação de Associado")
                    NewAssociadoDTO newAssociadoDTO
    ) {
        Associado saved = service.newAssociado(newAssociadoDTO);
        URI location = getAssociadoURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Exibe associado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public Associado getAssociadoById(
            @PathVariable
            @ApiParam(value = "ID do Associado.", name = "id")
                    String id
    ) throws DataNotFoundException {
        return service.findById(id);
    }

    @GetMapping(value = "/{id}/pautas")
    @ApiOperation(value = "Lista todas as pautas que pertencem ao associado", tags = { "associados", "pautas" } )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Pauta.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public List<Pauta> getPautasByAssociadoId(
            @PathVariable
            @ApiParam(value = "ID do Associado.", name = "id")
                    String id
    ) throws DataNotFoundException {
        return service.findPautasByAssociadoId(id);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Deleta associado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public ResponseEntity<?> deleteAssociadoById(
            @PathVariable
            @ApiParam(value = "ID do Associado.", name = "id")
                    String id
    ) throws DataNotFoundException {
        service.deleteAssociado(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation("Substitui completamente atributos do associado mantendo o mesmo id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> replaceAssociadoById(
            @PathVariable
            @ApiParam(value = "ID do Associado.", name = "id")
                    String id,
            @Valid
            @RequestBody
            @ApiParam(value = "Informações do novo Associado.", name = "Criação de Associado")
                    NewAssociadoDTO newAssociadoDTO
    ) throws DataNotFoundException {
        return ResponseEntity.ok().location(getAssociadoURI(service.replaceAssociado(id, newAssociadoDTO))).build();
    }

    @PatchMapping(value = "/{id}")
    @ApiOperation("Substitui parcialmente atributos do associado mantendo o mesmo id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> updateAssociadoById(
            @PathVariable
            @ApiParam(value = "ID do Associado.", name = "id")
                    String id,
            @RequestBody
            @ApiParam(value = "Informações do novo Associado.", name = "Criação de Associado")
                    NewAssociadoDTO newAssociadoDTO
    ) throws DataNotFoundException {
        return ResponseEntity.ok().location(getAssociadoURI(service.updateAssociado(id, newAssociadoDTO))).build();
    }

    @GetMapping(value = "/busca")
    @ApiOperation("Busca associados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Associado.class, responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL")}),
            @ApiResponse(code = 400, message = ErrorMessages.NO_SEARCH_PARAMS, response = ApiError.class)
    })
    public List<Associado> searchAssociado(
            @RequestParam(required = false, value = "cpf")
            @ApiParam(value = "Aceita partes do CPF e não considera letras maíusculas e minúsculas.", name = "CPF do Associado")
                    String cpf,
            @RequestParam(required = false, value = "nome")
            @ApiParam(value = "Aceita partes do nome e não considera letras maíusculas e minúsculas.", name = "Nome do Associado")
                    String nome
    ) throws NoSearchParametersException {
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
