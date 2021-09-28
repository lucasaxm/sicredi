package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Criação de Associado")
public class NewAssociadoDTO {
    @NotBlank
    @JsonProperty("nome")
    @ApiModelProperty(
            value = "Nome do associado",
            example = "Heisenberg")
    private String nome;

    @NotBlank
    @JsonProperty("cpf")
    @ApiModelProperty(
            value = "CPF do Associado",
            example = "59579026084",
            dataType = "String",
            notes = "Qualquer string é aceita, mas para estar habilitado para votar é preciso de um CPF válido" +
                    " (Apenas números).")
    private String cpf;
}
