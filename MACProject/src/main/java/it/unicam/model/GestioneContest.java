package it.unicam.model;

import it.unicam.model.util.dtos.ContenutoGI;
import it.unicam.model.util.dtos.ContestGI;
import it.unicam.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GestioneContest {

    @Autowired
    private ContestRepository contestRepository;

    public void addContest(Contest constest){
        this.contestRepository.save(constest);
    }

    public Contest getContest(Long id){
        return this.contestRepository.findById(id).get();
    }
    public List<ContestGI> getAllContestSuInvitoAperti() {
        List <ContestGI> contests = new ArrayList<ContestGI>();
         this.contestRepository.findAll().forEach(contest -> {
            if(contest.suInvito() && (!contest.eChiuso()) && (contest.numUtentiInvitati() == 0)){
                contests.add(contest.getInfoGeneraliContest());
            }
    });
        return contests;
    }

    public List<ContestGI> getAllContest(Long idContributor) {
        List <ContestGI> contests = new ArrayList<ContestGI>();
        this.contestRepository.findAll().forEach(contest -> {
            if((!contest.eChiuso()) && contest.invitoContributor(idContributor)){
                contests.add(contest.getInfoGeneraliContest());
            }
        });
        return contests;
    }

    public List<ContestGI> getAllContestAperti() {
        List <ContestGI> contests = new ArrayList<ContestGI>();
        this.contestRepository.findAll().forEach(contest -> {
            if(!contest.eChiuso()){
                contests.add(contest.getInfoGeneraliContest());
            }
        });
        return contests;
    }


    public List<ContestGI> getAllContests() {
        List <ContestGI> contests = new ArrayList<ContestGI>();
        this.contestRepository.findAll().forEach(contest -> {
            contest.getInfoGeneraliContest();
            contests.add(contest.getInfoGeneraliContest());
        });
        return contests;
    }

    public List<ContenutoGI> viewSelectContenutiContest(Long idContest) {
        return this.contestRepository.findById(idContest).get().getContenutoContestValidato();
    }

}
