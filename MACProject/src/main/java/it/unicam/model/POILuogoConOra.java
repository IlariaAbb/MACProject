package it.unicam.model;

import it.unicam.model.util.dtos.ContenutoGI;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.model.util.dtos.POIGI;
import jakarta.persistence.Entity;

import java.time.LocalTime;
import java.util.List;

@Entity
public class POILuogoConOra extends POI{

    private LocalTime[] oraApertura = new LocalTime[7];
    private LocalTime[] oraChiusura = new LocalTime[7];

    public POILuogoConOra(Coordinate coordinata) {
        super(coordinata);
        this.setTipo(Tipo.LUOGOCONORA);
    }

    public POILuogoConOra() {
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
    
    public LocalTime[] getOraApertura() {
        return oraApertura;
    }

    public LocalTime[] getOraChiusura() {
        return oraChiusura;
    }

    public void insertTempo(LocalTime[] oraApertura, LocalTime[] oraChiusura) {
        this.oraApertura = oraApertura;
        this.oraChiusura = oraChiusura;
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
    public void insertInfoPOI(String nome, String descrizione){
        if(nome == null || descrizione == null) throw new NullPointerException("Info nulla");
        this.setNome(nome);
        this.setDescrizione(descrizione);
    }

    @Override
    public POIGI getInfoGeneraliPOI(){
        return new POIGI(this.getIdPOI(), this.getNome(), this.getDescrizione(), this.getCoordinata(), this.getTipo());
    }
    
    @Override
    public POIFD getInfoDettagliatePOI(){
        List<ContenutoGI> contenutiGI = this.getContenuti().stream().map(c -> c.getInfoGeneraliContenuto()).toList();
        List<ContenutoGI> contenutiPendingGI = this.getContenutiPending().stream().map(pc -> pc.getInfoGeneraliContenuto()).toList();
        return new POIFD(this.getIdPOI(), this.getNome(), this.getDescrizione(), this.getCoordinata(), this.getTipo(), this.oraApertura, this.oraChiusura, contenutiGI, contenutiPendingGI);
    }

}
