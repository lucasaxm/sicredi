package com.sicredi.assembleia.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Document(collection = "associados")
public class Associado {
    @Id
    private String id;

    @NotBlank
    private String nome;

    @NotBlank
    @Indexed(unique = true)
    private String cpf;
}
