package com.sicredi.assembleia.entities;

import com.sicredi.assembleia.dto.SessaoDTO;
import com.sicredi.assembleia.dto.VotoDTO;
import com.sicredi.assembleia.dto.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sessoes")
public class Sessao {

    @Id
    private String id;

    @NotNull
    private Boolean aberta;

    @Min(10)
    @Max(600)
    @NotNull
    private Long duracao;

    @NotNull
    private LocalDateTime inicio;

    @Min(0)
    @NotNull
    private Long sim;

    @Min(0)
    @NotNull
    private Long nao;

    @DBRef
    @NotNull
    List<Associado> votantes;

    @DBRef
    @NotNull
    Pauta pauta;

    public Sessao(Pauta pauta, SessaoDTO dto){
        this.inicio = LocalDateTime.now();
        this.sim = 0L;
        this.nao = 0L;
        this.votantes = new ArrayList<>();
        this.pauta = pauta;
        if (dto == null || dto.getDuracao() == null){
            this.duracao = 60L;
        } else {
            this.duracao = dto.getDuracao();
        }
        this.aberta = true;
    }

    public Sessao vote(VotoDTO votoDTO){
        if (votoDTO.getVoto() == VotoEnum.SIM) {
            this.sim++;
        } else if (votoDTO.getVoto() == VotoEnum.NAO) {
            this.nao++;
        }
        this.votantes.add(votoDTO.getAssociado());
        return this;
    }
}
