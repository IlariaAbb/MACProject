package it.unicam.model;

import it.unicam.model.utenti.Ruolo;
import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ItinerarioFD;
import it.unicam.model.util.dtos.ItinerarioGI;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.model.util.dtos.POIGI;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comune {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comune_generator")
    private Long idComune;

    private Coordinate coordinata;
    private String nome;

    @OneToOne
    private UtenteAutenticato curatore;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POI> ValidazionePOI = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<POI> PendingPOI = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Itinerario> itinerari = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Itinerario> itinerariPending = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Itinerario> itinerariPendingModifica = new ArrayList<>();

    public Comune() {
    }

    public Comune(String nome, Coordinate coordinata, UtenteAutenticato curatore) {
        if(!curatore.getRuolo().equals(Ruolo.CURATORE)){
            throw new IllegalArgumentException("L'utente non è un curatore");
        }
        this.curatore = curatore;
        this.nome = nome;
        this.coordinata = coordinata;

        POILuogo comune = new POILuogo(coordinata);
        comune.insertInfoPOI(nome, "Questo è il punto di interesse logico del Comune di " + nome);
        this.insertPOI(comune);
    }

    public Long getIdComune() {
        return idComune;
    }

    public Coordinate getCoordinata() {
        return coordinata;
    }

    public String getNome() {
        return nome;
    }

    public UtenteAutenticato getCuratore() {
        return curatore;
    }

    public void insertPOI(POI p) {
        this.ValidazionePOI.add(p);
    }

    public POI getPOI(Long i) {
        return this.ValidazionePOI.stream()
                .filter(p -> p.getIdPOI().equals(i))
                .findFirst()
                .orElse(null);
    }

    public void deletePOI(Long id) {
        this.itinerari.forEach(it -> it.getPOIs().removeIf(p -> p.getIdPOI().equals(id)));
        this.itinerariPending.forEach(it -> it.getPOIs().removeIf(p -> p.getIdPOI().equals(id)));
        this.ValidazionePOI.removeIf(poi -> poi.getIdPOI().equals(id));
    }

    public boolean esistonoPOI(Coordinate c) {
        for (POI p : this.ValidazionePOI) {
            if (p.getCoordinata().equals(c)) return true;
        }
        for (POI p : this.PendingPOI) {
            if (p.getCoordinata().equals(c)) return true;
        }
        return false;
    }

    public void insertPendingPOI(POI p) {
        this.PendingPOI.add(p);
    }

    public void deletePendingPOI(Long IdPOI) {
        this.PendingPOI.removeIf(poi -> poi.getIdPOI().equals(IdPOI));
    }

    public void validazioneSelectPOI(Long IdPOI) {
        POI p = this.PendingPOI.stream()
                .filter(poi -> poi.getIdPOI().equals(IdPOI))
                .findFirst()
                .orElse(null);
        if(p != null){
            this.insertPOI(p);
            this.PendingPOI.remove(p);
        }
    }

    public POIFD selectPOIPending(Long i) {
        POI poi = this.PendingPOI.stream()
                .filter(pp -> pp.getIdPOI().equals(i))
                .findFirst()
                .orElse(null);
        return (poi == null) ? null : poi.getInfoDettagliatePOI();
    }

    public POI getPendingPOI(Long idPending){
        return this.PendingPOI.stream()
                .filter(pp -> pp.getIdPOI().equals(idPending))
                .findFirst()
                .orElse(null);
    }

    public List<POIFD> getAllPOIPending() {
        return this.PendingPOI.stream()
                .map(poi -> poi.getInfoDettagliatePOI())
                .toList();
    }

    public List<POIGI> getAllPOI() {
        List<POIGI> pois = new ArrayList<>();
        for (POI p : this.ValidazionePOI) {
            pois.add(p.getInfoGeneraliPOI());
        }
        return pois;
    }

    public POIFD viewSelectPOI(Long IdPOI) {
        POI p = this.getPOI(IdPOI);
        return (p == null) ? null : p.getInfoDettagliatePOI();
    }

    public ContenutoFD viewContenuto(Long IdPOI, Long idContenuto) {
        POI p = this.getPOI(IdPOI);
        if(p == null) return null;
        Contenuto c = p.getContenuti().stream()
                .filter(ct -> ct.getIdContenuto().equals(idContenuto))
                .findFirst()
                .orElse(null);
        return (c == null) ? null : c.getInfoDettagliateContenuto();
    }

    public void deleteContenuto(Long IdPOI, Long idContenuto) {
        POI p = this.getPOI(IdPOI);
        if(p != null) {
            p.deleteContenuto(idContenuto);
        }
    }

    public ContenutoFD viewContenutoPendingPOI(Long IdPOI, Long idContenuto){
        POI p = this.getPOI(IdPOI);
        if(p == null) return null;
        Contenuto c = p.getContenutiPending().stream()
                .filter(cc -> cc.getIdContenuto().equals(idContenuto))
                .findFirst()
                .orElse(null);
        return (c == null) ? null : c.getInfoDettagliateContenuto();
    }

    public void deleteContenutoPending(Long IdPOI, Long idContenuto) {
        POI p = this.getPOI(IdPOI);
        if(p != null) {
            p.deleteContenutoPending(idContenuto);
        }
    }

    public void validazioneSelectContenuto(Long IdPOI, Long idContenuto) {
        POI p = this.getPOI(IdPOI);
        if(p != null){
            p.validazioneContenuto(idContenuto);
        }
    }

    public List<POIGI> getAllContenutoPendingPOI() {
        return this.ValidazionePOI.stream()
                .filter(p -> !p.getContenutiPending().isEmpty())
                .map(p -> p.getInfoGeneraliPOI())
                .toList();
    }

    public ContenutoFD selectContenutoPending(Long IdPOI, Long idContenuto) {
        POI p = this.getPOI(IdPOI);
        if (p == null) return null;
        Contenuto c = p.getContenutiPending().stream()
                .filter(cc -> cc.getIdContenuto().equals(idContenuto))
                .findFirst()
                .orElse(null);
        return (c == null) ? null : c.getInfoDettagliateContenuto();
    }

    public void insertItinerarioPending(Itinerario itinerario) {
        this.itinerariPending.add(itinerario);
    }

    public void insertItinerario(Itinerario itinerario) {
        this.itinerari.add(itinerario);
    }

    public List<ItinerarioGI> getAllItinerario() {
        return this.itinerari.stream()
                .map(Itinerario::getInfoGeneraliItinerario)
                .toList();
    }

    public ItinerarioFD selectItinerario(Long id) {
        Itinerario it = this.itinerari.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
        return (it == null) ? null : it.getInfoDettagliateItinerario();
    }

    public List<ItinerarioGI> getAllItinerarioPending() {
        return this.itinerariPending.stream()
                .map(Itinerario::getInfoGeneraliItinerario)
                .toList();
    }

    public ItinerarioFD selectItinerarioPending(Long id) {
        Itinerario it = this.itinerariPending.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
        return (it == null) ? null : it.getInfoDettagliateItinerario();
    }

    public void validazioneSelectItinerario(Long idItinerario) {
        Itinerario it = this.itinerariPending.stream()
                .filter(i -> i.getId().equals(idItinerario))
                .findFirst()
                .orElse(null);
        if(it != null){
            this.itinerari.add(it);
            this.itinerariPending.remove(it);
        }
    }

    public void deleteItinerarioPending(Long id) {
        this.itinerariPending.removeIf(it -> it.getId().equals(id));
    }

    public Itinerario getItinerario(Long idItinerario) {
        return this.itinerari.stream()
                .filter(i -> i.getId().equals(idItinerario))
                .findFirst()
                .orElse(null);
    }

    public void deleteItinerario(Long id) {
        this.itinerari.removeIf(it -> it.getId().equals(id));
    }

    public List<Long> notItinerario(){
        return this.itinerari.stream()
                .filter(i -> i.getPOIs().size() < 2)
                .map(Itinerario::getId)
                .toList();
    }

    public void deleteNotItinerario(){
        this.itinerari.removeIf(i -> i.getPOIs().size() < 2);
        this.itinerariPending.removeIf(i -> i.getPOIs().size() < 2);
    }

    public void insertItinerarioModifica(Itinerario it) {
        this.itinerariPendingModifica.add(it);
    }

    public Itinerario selectItinerarioPendingModifica(Long id) {
        return this.itinerariPendingModifica.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void approvaItinerarioModifica(Itinerario pendingUpdate, Long idOriginale) {
        Itinerario original = this.itinerari.stream()
                .filter(i -> i.getId().equals(idOriginale))
                .findFirst()
                .orElse(null);
        if(original == null) return;
        original.setNome(pendingUpdate.getNome());
        original.setDescrizione(pendingUpdate.getDescrizione());
        original.setDataApertura(pendingUpdate.getDataApertura());
        original.setDataChiusura(pendingUpdate.getDataChiusura());
        original.setPOIs(pendingUpdate.getPOIs());

        this.itinerariPendingModifica.remove(pendingUpdate);
    }

    public void rifiutaItinerarioModifica(Itinerario pendingUpdate) {
        this.itinerariPendingModifica.remove(pendingUpdate);
    }

    // METODO MANCANTE PER RECUPERARE LA LISTA DI TUTTI GLI ITINERARI PENDING MODIFICA
    public List<Itinerario> getItinerariPendingModifica() {
        return this.itinerariPendingModifica;
    }

    public boolean inComune(Coordinate coord) {
        return true;
    }

    public List<POI> eventiPOIConclusi(){
        return this.ValidazionePOI.stream()
                .filter(p -> p instanceof POIEvento e && e.getDataChiusura().isBefore(LocalDateTime.now()))
                .toList();
    }

    public List<Itinerario> itinerariConclusi(){
        return this.itinerari.stream()
                .filter(i -> i.getDataChiusura() != null && i.getDataChiusura().isBefore(LocalDateTime.now()))
                .toList();
    }
}
