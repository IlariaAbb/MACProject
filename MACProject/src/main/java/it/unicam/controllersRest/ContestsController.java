package it.unicam.controllersRest;

import it.unicam.model.GestioneContest;
import it.unicam.model.controllersGRASP.ContestController;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ContestGI;
import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import it.unicam.repositories.UtenteAutenticatoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contests")
public class ContestsController {

    @Autowired
    private ContestController contestController;
    @Autowired
    private GestioneContest gestioneContest;
    @Autowired
    private UtenteAutenticatoRepository utenteAutenticatoRepository;

    @PostMapping("/animatore/creaContest")
    public ResponseEntity<Object> creaContest(@Valid @RequestBody ContestGI c){
        this.contestController.creaContest(c);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @GetMapping("/animatore/getAllContestSuInvitoAperti")
    public ResponseEntity<Object> getAllContestSuInvitoAperti(){
        return new ResponseEntity<>(this.gestioneContest.getAllContestSuInvitoAperti(), HttpStatus.OK);
    }

    @GetMapping("/animator/getContibutor")
    public List<UtenteAutenticatoGI> getContibutor() {
        return this.contestController.selectContestContibutor();
    }

    @PostMapping("/animatore/invitareContributor")
    public ResponseEntity<Object> invitareContributor(@RequestParam("id") Long id, @RequestParam("idContributor") Long[] idContributor) {
        if(!this.gestioneContest.getContest(id).suInvito())
            return new ResponseEntity<>("Contest non su invito", HttpStatus.BAD_REQUEST);
        if(this.gestioneContest.getAllContestSuInvitoAperti().stream().filter(c -> id.equals(c.getIdContest())).toList().isEmpty())
            return new ResponseEntity<>("Utente già invitato a questo contest", HttpStatus.BAD_REQUEST);
        for (Long cId : idContributor) {
            if(this.contestController.selectContestContibutor().stream().filter(c -> cId.equals(c.getId())).toList().isEmpty())
                return new ResponseEntity<>("Non è un contributor", HttpStatus.BAD_REQUEST);
        }
        for (Long cId : idContributor) {
            this.contestController.invitareContributor(id, cId);
        }
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/contributor/getAllContest")
    public ResponseEntity<Object> getAllContest(Authentication authentication){
        Long idContributor = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId();
        return new ResponseEntity<>(this.gestioneContest.getAllContest(idContributor), HttpStatus.OK);
    }

    @PostMapping("/contributor/partecipazioneContest")
    public ResponseEntity<Object> partecipazioneContest(@RequestParam("id") Long id, @Valid @RequestPart("contenuto") ContenutoFD contenuto, @RequestParam("file") MultipartFile f, Authentication authentication) {
        Long idContributor = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId();
        if(this.gestioneContest.getContest(id).eChiuso())
            return new ResponseEntity<>("Contest chiuso", HttpStatus.BAD_REQUEST);
        if(!this.gestioneContest.getContest(id).invitoContributor(idContributor))
            return new ResponseEntity<>("Contributor non invitato o già partecipante", HttpStatus.BAD_REQUEST);
        try {
            contenuto.addFile(f.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.contestController.partecipazioneContest(id, contenuto, idContributor);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @GetMapping("/animatore/getAllContestAperti")
    public ResponseEntity<Object> getAllContestAperti(){
        return new ResponseEntity<>(this.gestioneContest.getAllContestAperti(), HttpStatus.OK);
    }


    @GetMapping("/animatore/viewContenutiContestPending")
    public ResponseEntity<Object> viewContenutiContestPending(@RequestParam("id") Long id){
        if (this.gestioneContest.getContest(id).eChiuso())
            return new ResponseEntity<>("Contest closed", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(this.contestController.viewContenutoContestPending(id), HttpStatus.OK);
    }


    @GetMapping("/animatore/selectContenutoContest")
    public ResponseEntity<Object> selectContenutoContest(@RequestParam("idContest") Long idContest, @RequestParam("id") Long id){
        ContenutoFD c = this.contestController.selectContenutoContest(idContest, id);
        if(c == null)
            return new ResponseEntity<>("Contenuto non trovato", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }


    @DeleteMapping("/animatore/deleteContenutiContestPending")
    public ResponseEntity<Object> deleteContenutoContest(@RequestParam("idContest") Long idContest, @RequestParam("id") Long id){
        if(this.contestController.selectContenutoContest(idContest, id) == null)
            return new ResponseEntity<>("Contenuto non trovato", HttpStatus.BAD_REQUEST);
        this.contestController.deleteContenutoContest(idContest, id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PutMapping ("/animatore/validazioneContenutoContest")
    public ResponseEntity<Object> validazioneContestC(@RequestParam("idContest") Long idContest, @RequestParam("id") Long id){
        if(this.contestController.selectContenutoContest(idContest, id) == null)
            return new ResponseEntity<>("Contenuto non trovato", HttpStatus.BAD_REQUEST);
        this.contestController.validazioneContestC(idContest, id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/contributor/viewSelectContenutiValidatiContest")
    public ResponseEntity<Object> viewSelectContenutiValidatiContest(@RequestParam("idContest") Long idContest){
        return new ResponseEntity<>(this.contestController.viewSelectContenutiContest(idContest), HttpStatus.OK);
    }

    @PostMapping("/animatore/selectContenutoVincitore")
    public ResponseEntity<Object> selectContenutoVincitore(@RequestParam("idContest") Long idContest, @RequestParam("id") Long id){
        if (this.contestController.viewSelectContenutiContest(idContest).isEmpty())
            return new ResponseEntity<>("Contenuti non trovati", HttpStatus.BAD_REQUEST);
        this.contestController.selectContenutoVincitore(idContest, id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/contributor/getAllContests")
    public ResponseEntity<Object> getAllContests(){
        return new ResponseEntity<>(this.gestioneContest.getAllContests(), HttpStatus.OK);
    }

    @GetMapping("/contibutor/viewSelectContenutoValidatoContest")
    public ResponseEntity<Object> viewSelectContenutoValidatoContest(@RequestParam("idContest") Long idContest, @RequestParam("id") Long idContenuto){
        ContenutoFD c = this.contestController.viewSelectContenutoContest(idContest, idContenuto);
        if(c == null)
            return new ResponseEntity<>("Contenuto non trovato", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

}
