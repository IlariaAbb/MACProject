package it.unicam.model;

import it.unicam.model.utenti.UtenteAutenticato;
import jakarta.persistence.*;

@Entity
public class Partecipazione {
    @EmbeddedId
    private PartecipazioneId id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("idContenuto")
    private Contenuto contenuto;

    @ManyToOne
    @MapsId("idUtente")
    private UtenteAutenticato utente;

    public Partecipazione(Contenuto contenuto, UtenteAutenticato contributor) {
        this.contenuto = contenuto;
        this.utente = contributor;
        this.id = new PartecipazioneId(contenuto.getIdContenuto(), contributor.getId());
    }

    public Partecipazione() {

    }

    public UtenteAutenticato getUtente() {
        return utente;
    }

    public Contenuto getContenuto() {
        return contenuto;
    }
}
