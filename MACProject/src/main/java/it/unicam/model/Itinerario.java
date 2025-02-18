package it.unicam.model;

import it.unicam.model.util.dtos.ItinerarioFD;
import it.unicam.model.util.dtos.ItinerarioGI;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Itinerario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itinerary_id_seq")
    private Long id;

    private String nome;
    private String descrizione;
    private LocalDateTime dataApertura = null;
    private LocalDateTime dataChiusura = null;

    @ManyToMany
    private List<POI> POIs;

    public Itinerario(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.POIs = new ArrayList<>();
    }

    public Itinerario() {
        this.POIs = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(LocalDateTime dataApertura) {
        this.dataApertura = dataApertura;
    }

    public LocalDateTime getDataChiusura() {
        return dataChiusura;
    }

    public void setDataChiusura(LocalDateTime dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public List<POI> getPOIs() {
        return POIs;
    }

    public void setPOIs(List<POI> POIs) {
        this.POIs = POIs;
    }

    public ItinerarioGI getInfoGeneraliItinerario(){
        return new ItinerarioGI(
                this.id,
                this.nome,
                this.descrizione,
                this.dataApertura,
                this.dataChiusura
        );
    }

    public ItinerarioFD getInfoDettagliateItinerario(){
        return new ItinerarioFD(
                this.id,
                this.nome,
                this.descrizione,
                this.dataApertura,
                this.dataChiusura,
                this.POIs.stream()
                         .map(poi -> poi.getInfoGeneraliPOI())
                         .toList()
        );
    }

    public void addPOI(POI p){
        this.POIs.add(p);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Itinerario)) return false;
        Itinerario itinerario = (Itinerario) obj;
        return getId().equals(itinerario.getId());
    }
}
