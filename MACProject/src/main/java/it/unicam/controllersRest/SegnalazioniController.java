package it.unicam.controllersRest;

import it.unicam.model.Comune;
import it.unicam.model.segnalazioni.Segnalazione;
import it.unicam.model.segnalazioni.StatoSegnalazione;
import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.ContenutoRepository;
import it.unicam.repositories.SegnalazioneRepository;
import it.unicam.repositories.UtenteAutenticatoRepository;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.controllersGRASP.ViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segnalazioni")
public class SegnalazioniController {

    @Autowired
    private SegnalazioneRepository segnalazioneRepository;
    @Autowired
    private UtenteAutenticatoRepository utenteAutenticatoRepository;
    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private ContenutoRepository contenutoRepository;
    @Autowired
    private ViewController viewController;

  
    @PostMapping("/atourist/reportContenuto")
    public ResponseEntity<Object> reportContenuto(Authentication authentication,
                                                  @RequestParam("idComune") Long idComune,
                                                  @RequestParam("idPOI") Long idPOI,
                                                  @RequestParam("idContenuto") Long idContenuto,
                                                  @RequestParam(value = "motivo", required = false) String motivo) {
        if (this.comuneRepository.findById(idComune).isEmpty()) {
            return new ResponseEntity<>("Comune non trovato", HttpStatus.BAD_REQUEST);
        }
        Comune comune = this.comuneRepository.findById(idComune).get();
        ContenutoFD contenutoFD = viewController.viewContenuto(idComune, idPOI, idContenuto);
        if (contenutoFD == null) {
            return new ResponseEntity<>("Contenuto non trovato o non appartiene a questo comune", HttpStatus.BAD_REQUEST);
        }

        UtenteAutenticato utenteSegnalante = this.utenteAutenticatoRepository
                .findByUsername(authentication.getName());
        if (utenteSegnalante == null) {
            return new ResponseEntity<>("Utente non trovato in repository", HttpStatus.BAD_REQUEST);
        }

        Segnalazione s = new Segnalazione(idPOI, idContenuto, motivo, utenteSegnalante);
        this.segnalazioneRepository.save(s);

        return new ResponseEntity<>("Segnalazione creata con id=" + s.getId(), HttpStatus.OK);
    }


    @GetMapping("/curatore/listSegnalazioni")
    public ResponseEntity<Object> listSegnalazioniAperte(Authentication authentication) {
        Comune c = this.comuneRepository.findByCuratore(
                this.utenteAutenticatoRepository.findByUsername(authentication.getName()));
        if (c == null) {
            return new ResponseEntity<>("Errore: Non sei curatore di alcun comune", HttpStatus.BAD_REQUEST);
        }
        Iterable<Segnalazione> all = this.segnalazioneRepository.findAll();
        var result = new java.util.ArrayList<Segnalazione>();
        all.forEach(s -> {
            if (s.getStato() == StatoSegnalazione.APERTA) {
                ContenutoFD cFD = viewController.viewContenuto(c.getIdComune(), s.getIdPOI(), s.getIdContenuto());
                if (cFD != null) {
                    result.add(s);
                }
            }
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/curatore/viewSegnalazione")
    public ResponseEntity<Object> viewSegnalazione(Authentication authentication,
                                                   @RequestParam("idSegnalazione") Long idSegnalazione) {
        Comune c = this.comuneRepository.findByCuratore(
                this.utenteAutenticatoRepository.findByUsername(authentication.getName()));
        if (c == null) {
            return new ResponseEntity<>("Errore: Non sei curatore di alcun comune", HttpStatus.BAD_REQUEST);
        }
        Segnalazione s = this.segnalazioneRepository.findById(idSegnalazione).orElse(null);
        if (s == null) {
            return new ResponseEntity<>("Segnalazione non trovata", HttpStatus.BAD_REQUEST);
        }
        ContenutoFD cFD = this.viewController.viewContenuto(c.getIdComune(), s.getIdPOI(), s.getIdContenuto());
        if (cFD == null) {
            return new ResponseEntity<>("Il contenuto segnalato non fa parte di questo comune", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PutMapping("/curatore/respingiSegnalazione")
    public ResponseEntity<Object> respingiSegnalazione(Authentication authentication,
                                                       @RequestParam("idSegnalazione") Long idSegnalazione) {
        Comune c = this.comuneRepository.findByCuratore(
                this.utenteAutenticatoRepository.findByUsername(authentication.getName()));
        if (c == null) {
            return new ResponseEntity<>("Errore: Non sei curatore di alcun comune", HttpStatus.BAD_REQUEST);
        }

        Segnalazione s = this.segnalazioneRepository.findById(idSegnalazione).orElse(null);
        if (s == null) {
            return new ResponseEntity<>("Segnalazione inesistente", HttpStatus.BAD_REQUEST);
        }
        ContenutoFD cFD = this.viewController.viewContenuto(c.getIdComune(), s.getIdPOI(), s.getIdContenuto());
        if (cFD == null) {
            return new ResponseEntity<>("Il contenuto segnalato non fa parte di questo comune", HttpStatus.BAD_REQUEST);
        }

        s.setStato(StatoSegnalazione.RESPINTA);
        this.segnalazioneRepository.save(s);
        return new ResponseEntity<>("Segnalazione respinta", HttpStatus.OK);
    }

    @PutMapping("/curatore/accettaSegnalazione")
    public ResponseEntity<Object> accettaSegnalazione(Authentication authentication,
                                                      @RequestParam("idSegnalazione") Long idSegnalazione) {
        Comune c = this.comuneRepository.findByCuratore(
                this.utenteAutenticatoRepository.findByUsername(authentication.getName()));
        if (c == null) {
            return new ResponseEntity<>("Errore: Non sei curatore di alcun comune", HttpStatus.BAD_REQUEST);
        }

        Segnalazione s = this.segnalazioneRepository.findById(idSegnalazione).orElse(null);
        if (s == null) {
            return new ResponseEntity<>("Segnalazione inesistente", HttpStatus.BAD_REQUEST);
        }

        ContenutoFD cFD = this.viewController.viewContenuto(c.getIdComune(), s.getIdPOI(), s.getIdContenuto());
        if (cFD == null) {
            return new ResponseEntity<>("Il contenuto segnalato non fa parte di questo comune", HttpStatus.BAD_REQUEST);
        }

        s.setStato(StatoSegnalazione.ACCETTATA);
        this.segnalazioneRepository.save(s);

        c.deleteContenuto(s.getIdPOI(), s.getIdContenuto());
        this.comuneRepository.save(c);
        this.contenutoRepository.deleteById(s.getIdContenuto());

        return new ResponseEntity<>("Segnalazione accettata e contenuto eliminato", HttpStatus.OK);
    }
}
