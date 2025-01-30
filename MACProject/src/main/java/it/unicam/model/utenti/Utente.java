package it.unicam.model.utenti;

import it.unicam.controllersRest.ComuneController;

public interface Utente {

    public Ruolo getRuolo();
    public String getUsername();
    public String getPassword();
}
