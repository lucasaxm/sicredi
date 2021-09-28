package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Criação de Sessão")
public class SessaoDTO {

    @Min(10)
    @Max(600)
    @JsonProperty("duracao")
    @ApiModelProperty(
            value = "Duração da sessão em segundos",
            example = "300",
            dataType = "Long",
            allowableValues = "range[10, 600]")
    private Long duracao;

}
