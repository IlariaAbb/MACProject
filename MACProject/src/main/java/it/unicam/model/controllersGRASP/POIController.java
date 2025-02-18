package it.unicam.model.controllersGRASP;

import it.unicam.model.*;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.model.util.dtos.UpdatePOIFD;
import it.unicam.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class POIController {

    @Autowired
    private ComuneRepository comuneRepository;

    public boolean selectPunto(Long idComune, Coordinate c){
        // return this.comuneRepository.findById(idComune).get().inComune(c)
        //        && !this.comuneRepository.findById(idComune).get().esistonoPOI(c);
        return true; // semplificato
    }

    public void insertPOI(Long idComune, POIFactory pf, POIFD p) {
        POI poi = pf.creaPOI(p.getCoordinate());
        poi.insertInfoPOI(p.getNome(), p.getDescrizione());

        if (poi instanceof POILuogoConOra plo) {
            plo.insertTempo(p.getOraApertura(), p.getOraChiusura());
        }
        if (poi instanceof POIEvento pe) {
            pe.insertData(p.getDataApertura(), p.getDataChiusura());
        }

        Comune c = this.comuneRepository.findById(idComune).get();
        c.insertPOI(poi);
        this.comuneRepository.save(c);
    }

    public void insertPendingPOI(Long idComune, POIFactory pf, POIFD p) {
        POI poi = pf.creaPOI(p.getCoordinate());
        poi.insertInfoPOI(p.getNome(), p.getDescrizione());

        if (poi instanceof POILuogoConOra plo) {
            plo.insertTempo(p.getOraApertura(), p.getOraChiusura());
        }
        if (poi instanceof POIEvento pe) {
            pe.insertData(p.getDataApertura(), p.getDataChiusura());
        }
        Comune c = this.comuneRepository.findById(idComune).get();
        c.insertPendingPOI(poi);
        this.comuneRepository.save(c);
    }

    public void updatePendingPOI(Long idComune, UpdatePOIFD update) {
        POIFactory pf;
        switch (update.getTipo()) {
            case LUOGO -> pf = new POILuogoFactory();
            case EVENTO -> pf = new POIEventoFactory();
            case LUOGOCONORA -> pf = new POILuogoConOraFactory();
            default -> throw new RuntimeException("Tipo POI non gestito");
        }
        POI poi = pf.creaPOI(update.getCoordinate());
        String newName = update.getNome() + " (MODIFICA DI ID=" + update.getIdOriginalPOI() + ")";
        poi.insertInfoPOI(newName, update.getDescrizione());

        if (poi instanceof POILuogoConOra plo) {
            plo.insertTempo(update.getOraApertura(), update.getOraChiusura());
        }
        if (poi instanceof POIEvento pe) {
            pe.insertData(update.getDataApertura(), update.getDataChiusura());
        }

        Comune com = this.comuneRepository.findById(idComune).get();
        com.insertPendingPOI(poi);
        this.comuneRepository.save(com);
    }

    public void applyUpdatePOI(Long idComune, Long idPOIPending, Long idPOIOriginal) {
        Comune c = this.comuneRepository.findById(idComune).orElse(null);
        if (c == null) return;

        POI pending = c.getPendingPOI(idPOIPending);
        POI original = c.getPOI(idPOIOriginal);
        if (pending == null || original == null) return;

        original.setNome(pending.getNome());
        original.setDescrizione(pending.getDescrizione());
        if (original instanceof POILuogoConOra oLuogo && pending instanceof POILuogoConOra pLuogo) {
            oLuogo.insertTempo(pLuogo.getOraApertura(), pLuogo.getOraChiusura());
        }
        if (original instanceof POIEvento oEvento && pending instanceof POIEvento pEvento) {
            oEvento.insertData(pEvento.getDataApertura(), pEvento.getDataChiusura());
        }

        c.deletePendingPOI(idPOIPending);

        this.comuneRepository.save(c);
    }

}
