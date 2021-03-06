package com.sicredi.assembleia.entities;

import com.sicredi.assembleia.dto.NewAssociadoDTO;
import com.sicredi.assembleia.dto.NewPautaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "associados")
@ApiModel
public class Associado {
    @Id
    @ApiModelProperty(
            value = "ID do associado",
            example = "6151d4f1a8a3767186056cba")
    private String id;

    @NotBlank
    @ApiModelProperty(
            value = "Nome do associado",
            example = "Heisenberg")
    private String nome;

    @NotBlank
    @Indexed(unique = true)
    @ApiModelProperty(
            value = "CPF do Associado",
            example = "59579026084",
            dataType = "String",
            notes = "Qualquer string é aceita, mas para estar habilitado para votar é preciso de um CPF válido" +
                    " (Apenas números).")
    private String cpf;


    public Associado(NewAssociadoDTO dto){
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
    }
}
