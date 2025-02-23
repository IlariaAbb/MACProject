package it.unicam.model.controllersGRASP;

import it.unicam.model.Coordinate;
import it.unicam.model.Comune;
import it.unicam.model.POI;
import it.unicam.model.POIFactory;
import it.unicam.model.POILuogoConOra;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class POIController {

    @Autowired
    private ComuneRepository comuneRepository;

    public boolean selectPunto(Long idComune, Coordinate c){
        return true; 
    }

    public void insertPOI(Long idComune, POIFactory pf, POIFD p) {
        POI poi = pf.creaPOI(p.getCoordinate());
        poi.insertInfoPOI(p.getNome(), p.getDescrizione());
        if (poi instanceof POILuogoConOra plo) {
            plo.insertTempo(p.getOraApertura(), p.getOraChiusura());
        }
        if (poi instanceof it.unicam.model.POIEvento pe) {
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
        if (poi instanceof it.unicam.model.POIEvento pe) {
            pe.insertData(p.getDataApertura(), p.getDataChiusura());
        }
        Comune c = this.comuneRepository.findById(idComune).get();
        c.insertPendingPOI(poi);
        this.comuneRepository.save(c);
    }
}
