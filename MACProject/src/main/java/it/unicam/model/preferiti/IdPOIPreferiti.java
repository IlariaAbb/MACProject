package it.unicam.model.preferiti;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class IdPOIPreferiti implements Serializable {
    private Long idPOI;
    private Long idUtente;

    public IdPOIPreferiti(Long idPOI, Long idUtente) {
        this.idPOI = idPOI;
        this.idUtente = idUtente;
    }

    public IdPOIPreferiti() {

    }

    public Long getIdPOI() {
        return idPOI;
    }

    public Long getIdUtente() {
        return idUtente;
    }
}
