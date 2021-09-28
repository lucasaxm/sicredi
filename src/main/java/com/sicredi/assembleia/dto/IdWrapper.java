package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@ApiModel("Referência ID")
public class IdWrapper {
    @JsonProperty("id")
    @NotNull
    @ApiModelProperty(
            value = "id do objeto",
            example = "614f874ebbd3b06e9aa70b18")
    private String id;
}
