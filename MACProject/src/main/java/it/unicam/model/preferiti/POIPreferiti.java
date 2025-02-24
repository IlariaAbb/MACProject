package it.unicam.model.preferiti;

import it.unicam.model.POI;
import it.unicam.model.utenti.UtenteAutenticato;
import jakarta.persistence.*;

@Entity
public class POIPreferiti {

    @EmbeddedId
    private IdPOIPreferiti id;

    @ManyToOne
    @MapsId("idPOI")
    private POI poi;

    @ManyToOne
    @MapsId("idUtente")
    private UtenteAutenticato utente;

    public POIPreferiti(POI poi, UtenteAutenticato contributor) {
        this.poi = poi;
        this.utente = contributor;
        this.id = new IdPOIPreferiti(poi.getIdPOI(), contributor.getId());
    }

    public POIPreferiti() {
    }

    public UtenteAutenticato getUtente() {
        return utente;
    }

    public POI getPoi() {
        return poi;
    }

    public IdPOIPreferiti getId() {
        return id;
    }
}
