package com.sicredi.assembleia.controllers;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.entities.Pauta;
import com.sicredi.assembleia.entities.Sessao;
import com.sicredi.assembleia.errorhandling.ErrorMessages;
import com.sicredi.assembleia.errorhandling.apierrors.ApiError;
import com.sicredi.assembleia.errorhandling.exceptions.DataNotFoundException;
import com.sicredi.assembleia.services.PautaService;
import com.sicredi.assembleia.services.SessaoService;
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
@RequestMapping("/pautas/{pautaId}/sessoes")
@Api(value = "Sessões Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"sessões"}, description = "Sessões API")
public class SessaoController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoService sessaoService;


    @PostMapping
    @ApiOperation(value = "Abre uma sessão para uma pauta", tags = {"sessões", "pautas"})
    @ApiResponses({
            @ApiResponse(code = 201, message = "Sessão aberta com sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL da Sessão")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> createSessao(
            @PathVariable
            @ApiParam(value = "ID da Pauta a ser aberta.", name = "ID da Pauta")
                    String pautaId,
            @Valid
            @RequestBody(required = false)
            @ApiParam(value = "Duração da sessão em segundos.\nCaso não seja informada a duração será de 60 segundos.", name = "Duração")
                    SessaoDTO sessaoDto
    ) throws DataNotFoundException {
        Pauta pauta = pautaService.findById(pautaId);
        Sessao sessao = new Sessao(pauta, sessaoDto);
        Sessao saved = sessaoService.newSessao(sessao);
        URI location = getSessaoURI(saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @ApiOperation(value = "Lista status de todas as sessões de uma pauta", tags = {"sessões", "pautas"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public List<Sessao> getSessoes(
            @ApiParam(value = "ID da Pauta.", name = "ID da Pauta")
            @PathVariable
                    String pautaId
    ) throws DataNotFoundException {
        Pauta pauta = pautaService.findById(pautaId);

        return sessaoService.findByPauta(pauta);
    }

    @GetMapping(value = "/{sessaoId}")
    @ApiOperation(value = "Mostra o status de uma sessão de uma pauta", tags = {"sessões", "pautas"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = Sessao.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public Sessao getSessaoStatus(
            @PathVariable
            @ApiParam(value = "ID da Pauta da Sessão.", name = "ID da Pauta")
                    String pautaId,
            @PathVariable
            @ApiParam(value = "ID da sessão.", name = "ID da Sessão")
                    String sessaoId
    ) throws DataNotFoundException {
        return sessaoService.findById(sessaoId);
    }

    @PostMapping(value = "/{sessaoId}")
    @ApiOperation(value = "Registra voto de um Associado em uma sessão de uma pauta", tags = {"sessões", "pautas", "associados"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL da Sessão")}),
            @ApiResponse(code = 403, message = ErrorMessages.CPF_UNABLE_TO_VOTE, response = ApiError.class),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 406, message = ErrorMessages.SESSION_CLOSED + " | " + ErrorMessages.INVALID_CPF, response = ApiError.class),
            @ApiResponse(code = 409, message = ErrorMessages.VOTING_AGAIN, response = ApiError.class)
    })
    public ResponseEntity<?> vote(
            @PathVariable
            @ApiParam(value = "ID da Pauta da Sessão.", name = "ID da Pauta")
                    String pautaId,
            @PathVariable
            @ApiParam(value = "ID da sessão no qual o voto está sendo registrado.", name = "ID da Sessão")
                    String sessaoId,
            @Valid
            @RequestBody
            @ApiParam(value = "Voto e ID do Associado que está votando.\nApenas um voto permitido por associado.", name = "Voto")
                    VotoDTO votoDTO
    ) throws Exception {
        return ResponseEntity.ok().location(getSessaoURI(sessaoService.vote(sessaoId, votoDTO))).build();
    }

    @DeleteMapping(value = "/{sessaoId}")
    @ApiOperation(value = "Deleta uma sessão de uma pauta", tags = {"sessões", "pautas"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL da Sessão")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class)
    })
    public ResponseEntity<?> deleteSessaoById(
            @PathVariable
            @ApiParam(value = "ID da Pauta da Sessão.", name = "ID da Pauta")
                    String pautaId,
            @PathVariable
            @ApiParam(value = "ID da Sessão a ser deletada.", name = "ID da Sessão")
                    String sessaoId
    ) throws DataNotFoundException {
        sessaoService.deleteSessao(sessaoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{sessaoId}")
    @ApiOperation(value = "Altera a duração de uma sessão de uma pauta", tags = {"sessões", "pautas"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", responseHeaders = {@ResponseHeader(name = "location", response = String.class, description = "URL da Sessão")}),
            @ApiResponse(code = 404, message = ErrorMessages.DATA_NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ErrorMessages.VALIDATION_ERROR, response = ApiError.class)
    })
    public ResponseEntity<?> updateSessao(
            @PathVariable
            @ApiParam(value = "ID da Sessão a ser aberta.", name = "ID da Sessão")
                    String sessaoId,
            @Valid
            @RequestBody
            @ApiParam(value = "Duração da sessão em segundos.", name = "Duração")
                    SessaoDTO sessaoDto
    ) throws Exception {
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
