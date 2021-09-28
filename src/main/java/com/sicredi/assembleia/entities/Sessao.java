package com.sicredi.assembleia.entities;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.dto.VotoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sessoes")
public class Sessao {

    @Id
    @ApiModelProperty(
            value = "ID da Pauta",
            example = "6151422fff86a42085b80c3b")
    private String id;

    @NotNull
    @ApiModelProperty(
            value = "Status da Sessão",
            notes = "True se a Sessão está aberta, False se fechada",
            example = "true",
            dataType = "boolean")
    private Boolean aberta;

    @Min(10)
    @Max(600)
    @NotNull
    @ApiModelProperty(
            value = "Duração da sessão em segundos",
            example = "300",
            dataType = "Long",
            allowableValues = "range[10, 600]")
    private Long duracao;

    @NotNull
    @ApiModelProperty(
            value = "Data e hora de início da Sessão")
    private LocalDateTime inicio;

    @Min(0)
    @NotNull
    @ApiModelProperty(
            value = "Contagem de votos SIM",
            example = "7",
            dataType = "Long")
    private Long sim;

    @Min(0)
    @NotNull
    @ApiModelProperty(
            value = "Contagem de votos NÃO",
            example = "1",
            dataType = "Long")
    private Long nao;

    @DBRef
    @NotNull
    @ApiModelProperty(
            value = "Lista de Associados que já votaram",
            notes = "Estes associados não podem votar novamente")
    List<Associado> votantes;

    @DBRef
    @NotNull
    @ApiModelProperty(
            value = "Pauta que está sendo votada")
    Pauta pauta;

    public Sessao(Pauta pauta, SessaoDTO dto) {
        this.inicio = LocalDateTime.now();
        this.sim = 0L;
        this.nao = 0L;
        this.votantes = new ArrayList<>();
        this.pauta = pauta;
        if (dto == null || dto.getDuracao() == null) {
            this.duracao = 60L;
        } else {
            this.duracao = dto.getDuracao();
        }
        this.aberta = true;
    }

    public Sessao vote(VotoEnum voto, Associado associado) {
        if (voto == VotoEnum.SIM) {
            this.sim++;
        } else if (voto == VotoEnum.NAO) {
            this.nao++;
        }
        this.votantes.add(associado);
        return this;
    }
}
