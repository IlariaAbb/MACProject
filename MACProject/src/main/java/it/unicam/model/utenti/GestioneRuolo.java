package it.unicam.model.utenti;

import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import it.unicam.repositories.RichiestaRuoloRepository;
import it.unicam.repositories.UtenteAutenticatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestioneRuolo {

    @Autowired
    private RichiestaRuoloRepository richiestaRuoloRepository;
    @Autowired
    private GestioneUtentiAutenticati gestioneUtentiAutenticati;
    @Autowired
    private UtenteAutenticatoRepository utenteAutenticatoRepository;

    public GestioneRuolo() {
    }

    public void richiestaCambioRuolo(Long id) {
        this.richiestaRuoloRepository.save(new RichiestaRuolo(id));
    }

    public List<UtenteAutenticatoGI> viewRichiesteCambioRuolo() {
        List<UtenteAutenticatoGI> utenti = new ArrayList<>();
        this.richiestaRuoloRepository.findAll().forEach(richiestaRuolo -> utenti.add(this.gestioneUtentiAutenticati.getUtenteGI(richiestaRuolo.getIdUtente())));
        return utenti;
    }

    public void disapprovazioneRichiesta(Long id) {
        this.richiestaRuoloRepository.findAll().forEach(richiestaRuolo -> {
            if(richiestaRuolo.getIdUtente().equals(id))
                this.richiestaRuoloRepository.delete(richiestaRuolo);
        });
    }

    public void approvazioneRichiesta(Long id) {
    UtenteAutenticato u = this.gestioneUtentiAutenticati.getUtente(id);
        switch (u.getRuolo()){
            case CONTRIBUTOR -> u.setRuolo(Ruolo.CONTRIBUTORAUTORIZZATO);
            case TURISTAUTENTICATO -> u.setRuolo(Ruolo.CONTRIBUTOR);
            default -> throw new IllegalArgumentException("Unexpected value: " + u.getRuolo());
        }
        this.utenteAutenticatoRepository.save(u);
        this.richiestaRuoloRepository.findAll().forEach(richiestaRuolo -> {
            if(richiestaRuolo.getIdUtente().equals(id))
                this.richiestaRuoloRepository.delete(richiestaRuolo);
        });
    }
}
