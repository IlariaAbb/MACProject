package it.unicam.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PartecipazioneId implements Serializable {
    private Long idContenuto;
    private Long idUtente;

    public PartecipazioneId(Long idContenuto, Long idUtente) {
        this.idContenuto = idContenuto;
        this.idUtente = idUtente;
    }

    public PartecipazioneId() {

    }
}
