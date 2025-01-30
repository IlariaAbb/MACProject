package it.unicam.model.preferiti;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class IdItinerariPreferiti implements Serializable {

    private Long idItinerario;
    private Long idUtente;

    public IdItinerariPreferiti(Long idItinerario, Long idUtente) {
        this.idItinerario = idItinerario;
        this.idUtente = idUtente;
    }

    public IdItinerariPreferiti() {

    }

    public Long getIdItinerario() {
        return idItinerario;
    }

    public Long getIdUtente() {
        return idUtente;
    }
}
