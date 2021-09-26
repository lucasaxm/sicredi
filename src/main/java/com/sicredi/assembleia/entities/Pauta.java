package com.sicredi.assembleia.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Document(collection = "pautas")
public class Pauta {
    @Id
    private String id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @DBRef
    private Associado autor;
}
