package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sicredi.assembleia.entities.Associado;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Voto")
public class VotoDTO {

    @JsonProperty("voto")
    @NotNull
    @ApiModelProperty(
            value = "Sim ou Nao (sem acento)",
            allowableValues = "SIM, NAO")
    private VotoEnum voto;

    @JsonProperty("associado")
    @NotNull
    @ApiModelProperty("Id do votante")
    private IdWrapper associadoIdWrapper;
}
