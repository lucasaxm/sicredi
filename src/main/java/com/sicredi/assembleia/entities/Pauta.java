package com.sicredi.assembleia.entities;

import com.sicredi.assembleia.dto.NewPautaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pautas")
public class Pauta {
    @Id
    @ApiModelProperty(
            value = "ID da Pauta",
            example = "61536b52dd01cb65599c3352")
    private String id;

    @NotBlank
    @ApiModelProperty(
            value = "Título da Pauta",
            example = "Isso é um título")
    private String titulo;

    @NotBlank
    @ApiModelProperty(
            value = "Descrição da Pauta",
            example = "Essa é uma descrição.")
    private String descricao;

    @DBRef
    @ApiModelProperty(
            value = "Autor da Pauta")
    private Associado autor;

    public Pauta(NewPautaDTO dto) {
        this.titulo = dto.getTitulo();
        this.descricao = dto.getDescricao();
        if (dto.getAutor() != null) {
            this.autor = new Associado(dto.getAutor().getId(), null, null);
        }
    }
}
