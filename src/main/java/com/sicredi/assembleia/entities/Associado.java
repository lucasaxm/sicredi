package com.sicredi.assembleia.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "associados")
public class Associado {
    @Id
    private String id;
    private String nome;

    @Indexed(unique = true)
    private String cpf;
}
