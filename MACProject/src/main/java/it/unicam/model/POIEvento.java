package it.unicam.model;

import it.unicam.model.util.dtos.ContenutoGI;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.model.util.dtos.POIGI;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class POIEvento extends POI{

    private LocalDateTime dataApertura;
    private LocalDateTime dataChiusura;

    public POIEvento(Coordinate coordinata) {
        super(coordinata);
        this.setTipo(Tipo.EVENTO);
    }

    public POIEvento() {
        super();
    }

    @Override
    public Long getIdPOI() {
        return super.getIdPOI();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    @Override
    public Tipo getTipo() {
        return super.getTipo();
    }

    @Override
    public Coordinate getCoordinata() {
        return super.getCoordinata();
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public LocalDateTime getDataChiusura() {
        return dataChiusura;
    }

    public void insertData(LocalDateTime dataApertura, LocalDateTime dataChiusura) {
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
    }

    @Override
    public void setTipo(Tipo tipo) {
        super.setTipo(tipo);
    }

    @Override
    public List<Contenuto> getContenuti() {
        return super.getContenuti();
    }

    @Override
    public void addContenuto(Contenuto c) {
        super.addContenuto(c);
    }

    @Override
    public void insertInfoPOI (String nome, String descrizione){
        if(nome == null || descrizione == null) throw new NullPointerException("Info nulla");
        this.setNome(nome);
        this.setDescrizione(descrizione);
    }

    @Override
    public POIGI getInfoGeneraliPOI(){
        return new POIGI(this.getIdPOI(), this.getNome(), this.getDescrizione(), this.getCoordinata(), this.getTipo());
    }

    @Override
    public POIFD getInfoDettagliatePOI() {
        List<ContenutoGI> contenutiGI = this.getContenuti().stream().map(c -> c.getInfoGeneraliContenuto()).toList();
        List<ContenutoGI> contenutiPendingGI = this.getContenutiPending().stream().map(pc -> pc.getInfoGeneraliContenuto()).toList();
        return new POIFD(this.getIdPOI(), this.getNome(), this.getDescrizione(), this.getCoordinata(), this.getTipo(), this.dataApertura, this.dataChiusura, contenutiGI, contenutiPendingGI);
    }
}
