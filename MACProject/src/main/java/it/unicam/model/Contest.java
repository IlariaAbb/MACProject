package it.unicam.model;

import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ContenutoGI;
import it.unicam.model.util.dtos.ContestGI;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contest_generator")
    private Long id;
    private String name;
    private String obj;
    private boolean suInvito;
    private boolean eChiuso;
    @ManyToMany
    private List<UtenteAutenticato> utentiInvitati = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Partecipazione> partecipazione = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Partecipazione> validazionePartecipazione = new ArrayList<>();

    public Contest(String name, String obj) {
        this.name = name;
        this.obj = obj;
        this.eChiuso = false;
        this.utentiInvitati = new ArrayList<>();
    }

    public Contest() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public boolean suInvito() {
        return suInvito;
    }

    public void setSuInvito(boolean suInvito) {
        this.suInvito = suInvito;
    }

    public boolean eChiuso() {
        return eChiuso;
    }

    public void setClosed(boolean chiuso) {
        eChiuso = chiuso;
    }

    public int numUtentiInvitati() {
        return this.utentiInvitati.size();
    }

    public ContestGI getInfoGeneraliContest() {
        return new ContestGI(this.id, this.name, this.obj, this.suInvito, this.eChiuso);
    }

    public void invitareContributor(UtenteAutenticato utente) {
        this.utentiInvitati.add(utente);
    }


    public boolean invitoContributor(Long idContributor) {
        if (suInvito()){
            for (UtenteAutenticato u : this.utentiInvitati) {
                if (u.getId() == idContributor) {
                    return (this.partecipazione.stream().filter(p -> p.getUtente().getId().equals(idContributor)).toList().size() == 0
                            && this.validazionePartecipazione.stream().filter(p -> p.getUtente().getId().equals(idContributor)).toList().size() == 0);
                }
            }
            return false;
        }else {
            return (this.partecipazione.stream().filter(p -> p.getUtente().getId().equals(idContributor)).toList().size() == 0
                    && this.validazionePartecipazione.stream().filter(p -> p.getUtente().getId().equals(idContributor)).toList().size() == 0);
        }
    }

    public void addContenuto(Contenuto contenuto, UtenteAutenticato contributor) {
        this.partecipazione.add(new Partecipazione(contenuto, contributor));
    }
    public List<ContenutoGI> getContenutoContestPending() {
        return this.partecipazione.stream().map(p -> p.getContenuto().getInfoGeneraliContenuto()).toList();
    }

    public Contenuto selectContenutoContest(Long i) {
        return this.partecipazione.stream().filter(p -> p.getContenuto().getIdContenuto().equals(i)).map(p -> p.getContenuto()).findFirst().orElse(null);
    }

    public void deleteContenutoContest(Contenuto contenuto) {
        this.partecipazione.remove(contenuto);
    }

    public void validazioneContestC(Contenuto contenuto) {
        this.validazionePartecipazione.add(this.partecipazione.stream().filter(p -> p.getContenuto().equals(contenuto)).findFirst().orElse(null));
        this.partecipazione.removeIf(p -> p.getContenuto().equals(contenuto));
    }
    public List<ContenutoGI> getContenutoContestValidato() {
       return this.validazionePartecipazione.stream().map(p -> p.getContenuto().getInfoGeneraliContenuto()).toList();
    }

    public String getContenutoEmailAutore(Long id) {
        return this.partecipazione.stream().filter(p -> p.getContenuto().getIdContenuto().equals(id)).map(p -> p.getUtente().getEmail()).findFirst().orElse(null);
    }

    public void chiudiContest() {
        this.eChiuso = true;
    }

    public ContenutoFD viewSelectContenutoContest(Long idContenuto) {
        if(this.validazionePartecipazione.stream().filter(p -> p.getContenuto().getIdContenuto().equals(idContenuto)).toList().size() == 0)
            return null;
        else
            return this.validazionePartecipazione.stream().filter(p -> p.getContenuto().getIdContenuto().equals(idContenuto)).map(p -> p.getContenuto().getInfoDettagliateContenuto()).findFirst().orElse(null);
    }

}
