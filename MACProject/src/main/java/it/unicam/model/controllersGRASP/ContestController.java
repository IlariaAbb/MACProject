package it.unicam.model.controllersGRASP;

import it.unicam.model.*;
import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ContenutoGI;
import it.unicam.model.util.dtos.ContestGI;
import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import it.unicam.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestController {
    @Autowired
    private GestioneContest gestioneContest;
    @Autowired
    private GestioneUtentiAutenticati gestioneUtentiAutenticati;
    @Autowired
    private ContestRepository contestRepository;


    public void creaContest(ContestGI c) {
        Contest contest = new Contest(c.getNome(), c.getObj());
        contest.setSuInvito(c.eSuInvito());
        this.gestioneContest.addContest(contest);
    }


    public List<UtenteAutenticatoGI> selectContestContibutor() {
        return this.gestioneUtentiAutenticati.getAllContributor();
    }

    public void invitareContributor(Long id, Long idContributor) {
        Contest contest = this.contestRepository.findById(id).get();
        contest.invitareContributor(this.gestioneUtentiAutenticati.getUtente(idContributor));
        this.contestRepository.save(contest);
    }

    public void partecipazioneContest(Long id, ContenutoFD contenuto, Long idContributor) {
        Contenuto c = new Contenuto(contenuto.getNome(), contenuto.getDescrizione(), contenuto.getFile());
        Contest contest = this.contestRepository.findById(id).get();
        contest.addContenuto(c, this.gestioneUtentiAutenticati.getUtente(idContributor));
        this.contestRepository.save(contest);
    }


    public List<ContenutoGI> viewContenutoContestPending(Long i) {
        Contest contest = this.contestRepository.findById(i).get();
        return contest.getContenutoContestPending();
    }

    public ContenutoFD selectContenutoContest(Long idContest, Long id) {
        Contest contest = this.contestRepository.findById(idContest).get();
        return contest.selectContenutoContest(id).getInfoDettagliateContenuto();
    }

    public void deleteContenutoContest(Long idContest, Long id) {
        Contest contest = this.contestRepository.findById(idContest).get();
        contest.deleteContenutoContest(contest.selectContenutoContest(id));
        this.contestRepository.save(contest);
    }

    public void validazioneContestC(Long idContest, Long id) {
        Contest contest = this.contestRepository.findById(idContest).get();
        contest.validazioneContestC(contest.selectContenutoContest(id));
        this.contestRepository.save(contest);
    }

    public void selectContenutoVincitore(Long idContest, Long id) {
        Contest contest = this.contestRepository.findById(idContest).get();
        contest.chiudiContest();
        this.contestRepository.save(contest);
        //codice per inviare email necessita di impostare le credenziali, provato con gmail attivando l'opzione app meno sicure
        //SMTPUtil.sendEmail(SMTPUtil.createSession(), this.lastContest.getAutoreContenutoEmail(i), "Vincitore contest di contribuzione", "Congratulazioni, sei il vincitore del contest di contribuzione, mostra questa mail per ritirare il premio");
    }

    public List<ContenutoGI> viewSelectContenutiContest(Long idContest) {
        return this.gestioneContest.viewSelectContenutiContest(idContest);
    }

    public ContenutoFD viewSelectContenutoContest(Long idContest, Long id) {
        Contest contest = this.contestRepository.findById(idContest).get();
        return contest.viewSelectContenutoContest(id);
    }

}
