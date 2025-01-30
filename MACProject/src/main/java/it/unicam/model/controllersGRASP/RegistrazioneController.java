package it.unicam.model.controllersGRASP;

import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.utenti.Ruolo;
import it.unicam.model.utenti.UtenteAutenticato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrazioneController {

    @Autowired
    private GestioneUtentiAutenticati gestioneUtentiAutenticati;

    public RegistrazioneController() {
    }

    public boolean registrazioneUtente(String email, String username, String password, Ruolo ruolo) {
        if(this.gestioneUtentiAutenticati.contieneUtenti(email, username)){
            return false;
        }
        if(ruolo.equals(Ruolo.TURISTAUTENTICATO) || ruolo.equals(Ruolo.CONTRIBUTOR)){
            this.gestioneUtentiAutenticati.addRegistrazioneUtente(new UtenteAutenticato(username, password, email, ruolo));
            return true;
        } else {
            return false;
        }

    }

    public void registrazioneRespinta(Long id) {
        this.gestioneUtentiAutenticati.registrazioneRespinta(id);
    }

    public void approvazioneRegistrazione(Long id) {
        this.gestioneUtentiAutenticati.approvazioneRegistrazione(id);
        //sono richieste le credenziali
        //SMTPUtil.sendEmail(SMTPUtil.createSession(), this.lastUtente.getEmail(), "Registrazione approvata", "La tua registrazione è stata approvata");
   }
}
