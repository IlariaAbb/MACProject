package it.unicam.model;

import it.unicam.model.util.dtos.POIFD;
import it.unicam.model.util.dtos.POIGI;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class POI {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poi_generator")
    private Long idPOI;
    private String nome;
    private String descrizione;
    private Tipo tipo;
    @Embedded
    private Coordinate coordinata;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contenuto> contenuti;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Contenuto> contenutiPending;

    public POI() {
        this.contenuti = new ArrayList<>();
        this.contenutiPending = new ArrayList<>();
    }
    public POI(String nome, String descrizione) {
        if(nome == null || descrizione == null) throw new NullPointerException("Parametri null");
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipo = null;
        this.coordinata = null;
        this.contenuti = new ArrayList<>();
        this.contenutiPending = new ArrayList<>();
    }
    public POI(Coordinate coordinata) {
        if(coordinata == null) throw new NullPointerException("Coordinate null");
        this.coordinata = coordinata;
        this.contenuti = new ArrayList<>();
        this.contenutiPending = new ArrayList<>();
    }


    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Coordinate getCoordinata() {
        return coordinata;
    }

    public  List<Contenuto> getContenuti(){
        return this.contenuti;
    };

    public List<Contenuto> getContenutiPending() {
        return contenutiPending;
    }

    public void addContenuto(Contenuto c){
        if(c == null) throw new NullPointerException("Contenuto null");
        this.contenuti.add(c);
    }

    public void addContenutoPending(Contenuto c){
        if(c == null) throw new NullPointerException("Contenuto null");
        this.contenutiPending.add(c);
    }
    public void setNome(String nome) {
        this.nome = nome;
    }


    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setTipo(Tipo tipo){
        this.tipo = tipo;
    }


    public Long getIdPOI() {
        return idPOI;
    }

    public abstract void insertInfoPOI(String nome, String descrizione);

    public abstract POIGI getInfoGeneraliPOI();

    public abstract POIFD getInfoDettagliatePOI();

    public void deleteContenuto(Long idContenuto) {
        this.contenuti.removeIf(c -> c.getIdContenuto().equals(idContenuto));
    }

    public void validazioneContenuto(Long id){
        this.contenuti.add(this.contenutiPending.stream().filter(c -> c.getIdContenuto().equals(id)).findFirst().get());
        this.deleteContenutoPending(id);
    }

    public void deleteContenutoPending(Long id) {
        this.contenutiPending.removeIf(c -> c.getIdContenuto().equals(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POI)) return false;
        POI poi = (POI) o;
        return getIdPOI().equals(poi.getIdPOI());
    }
}
