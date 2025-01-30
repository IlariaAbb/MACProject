package it.unicam.model.utenti;

import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UtenteAutenticato implements Utente{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utente_generator")
    private Long id;
    private String username;
    private String password;
    private String email;
    private Ruolo ruolo;


    public UtenteAutenticato(String username, String password, String email, Ruolo ruolo) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.ruolo = ruolo;
    }

    public UtenteAutenticato() {

    }


    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Ruolo getRuolo() {
        return ruolo;
    }


    public UtenteAutenticatoGI getInfoGeneraliUtenteAutenticato() {
        return new UtenteAutenticatoGI(this.id, this.username, this.email, this.password, this.ruolo);
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
