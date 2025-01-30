package it.unicam.model.util.dtos;

import it.unicam.model.utenti.Ruolo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UtenteAutenticatoGI {

    private Long id;
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    private Ruolo ruolo;

    public UtenteAutenticatoGI(Long id, String username, String email, String password, Ruolo ruolo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }
    public UtenteAutenticatoGI(){

    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public String password() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UtenteAutenticato id = " + id + " - Ruolo = "+this.ruolo +
                "\nusername = " + username +
                " - email = " + email;
    }
}
