package it.unicam.model.preferiti;

import it.unicam.model.Itinerario;
import it.unicam.model.utenti.UtenteAutenticato;
import jakarta.persistence.*;

@Entity
public class ItinerariPreferiti {
    @EmbeddedId
    private IdItinerariPreferiti id;

    @OneToOne
    @MapsId("idItinerario")
    private Itinerario itinerario;

    @ManyToOne
    @MapsId("idUtente")
    private UtenteAutenticato utente;


    public ItinerariPreferiti(Itinerario itinerario, UtenteAutenticato contributor) {
        this.itinerario = itinerario;
        this.utente = contributor;
        this.id = new IdItinerariPreferiti(itinerario.getId(), contributor.getId());
    }

    public ItinerariPreferiti() {

    }

    public UtenteAutenticato getUtente() {
        return utente;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public IdItinerariPreferiti getId() {
        return id;
    }
}



