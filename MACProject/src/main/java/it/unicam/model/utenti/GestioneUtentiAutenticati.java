package it.unicam.model.utenti;

import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import it.unicam.repositories.UtenteAutenticatoRepository;
import jakarta.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GestioneUtentiAutenticati {

    @Autowired
    private UtenteAutenticatoRepository utenteAutenticatoRepository;
    @OneToMany
    private List<UtenteAutenticato> utenti = new ArrayList<>();
    @OneToMany
    private List<UtenteAutenticato> registrazioniUtenti = new ArrayList<>();

    public void addUtente(UtenteAutenticato utenteAutenticato) {
        this.utenti.add(utenteAutenticato);
    }
    public UtenteAutenticato getUtente(Long id){
        return this.utenti.stream().filter(u -> u.getId().equals(id)).findFirst().get();
    }
    public List<UtenteAutenticatoGI> getAllContributor() {
        return this.utenti.stream().filter(u -> u.getRuolo().equals(Ruolo.CONTRIBUTOR) || u.getRuolo().equals(Ruolo.CONTRIBUTORAUTORIZZATO) || u.getRuolo().equals(Ruolo.CURATORE)).map(UtenteAutenticato::getInfoGeneraliUtenteAutenticato).toList();
    }

    public UtenteAutenticatoGI getUtenteGI(Long id) {
        return this.utenteAutenticatoRepository.findById(id).get().getInfoGeneraliUtenteAutenticato();
    }

    public List<UtenteAutenticatoGI> viewAllUtenti() {
        return this.utenti.stream().filter(u -> !u.getRuolo().equals(Ruolo.GESTORE)).map(UtenteAutenticato::getInfoGeneraliUtenteAutenticato).toList();
    }

    public void cambiaRuolo(Long id, Ruolo ruolo) {
        UtenteAutenticato utente = this.utenti.stream().filter(u -> u.getId().equals(id)).findFirst().get();
        utente.setRuolo(ruolo);
        this.utenteAutenticatoRepository.save(utente);
    }

    public void addRegistrazioneUtente(UtenteAutenticato utente) {
        this.registrazioniUtenti.add(utente);
        this.utenteAutenticatoRepository.save(utente);
    }

    public List<UtenteAutenticatoGI> viewRegistrazioneUtenti() {
        return this.registrazioniUtenti.stream().map(UtenteAutenticato::getInfoGeneraliUtenteAutenticato).toList();
    }


    public void approvazioneRegistrazione(Long id) {
        this.utenti.add(this.registrazioniUtenti.stream().filter(u -> u.getId().equals(id)).findFirst().get());
        this.registrazioniUtenti.removeIf(u -> u.getId().equals(id));
    }

    public void registrazioneRespinta(Long id) {
        this.registrazioniUtenti.removeIf(u -> u.getId().equals(id));
        this.utenteAutenticatoRepository.deleteById(id);
    }

    public boolean contieneUtenti(String email, String username) {
        return (this.utenti.stream().anyMatch(u -> u.getEmail().equals(email) || u.getUsername().equals(username)) || this.registrazioniUtenti.stream().anyMatch(u -> u.getEmail().equals(email) || u.getUsername().equals(username)));
    }
}
