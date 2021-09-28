package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sicredi.assembleia.entities.Associado;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Criação de Pauta")
public class NewPautaDTO {
    @NotBlank
    @JsonProperty("titulo")
    @ApiModelProperty(
            value = "Título da Pauta",
            example = "Isso é um título")
    private String titulo;

    @NotBlank
    @JsonProperty("descricao")
    @ApiModelProperty(
            value = "Descrição da Pauta",
            example = "Essa é uma descrição.")
    private String descricao;

    @JsonProperty("autor")
    @ApiModelProperty("Id do Associado autor da pauta")
    private IdWrapper autor;
}
