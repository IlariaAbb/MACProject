package it.unicam.model.controllersGRASP;

import it.unicam.model.Comune;
import it.unicam.model.Itinerario;
import it.unicam.model.util.dtos.ItinerarioFD;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.POIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItinerarioController {
    @Autowired
    private POIRepository poiRepository;
    @Autowired
    private ComuneRepository comuneRepository;


    public void creaItinerario(Long idComune, ItinerarioFD i, Long[] pois) {
        Itinerario it = new Itinerario(i.getNome(), i.getDescrizione());
        if(i.getDataApertura()!=null && i.getDataChiusura()!=null){
            it.setDataApertura(i.getDataApertura());
            it.setDataChiusura(i.getDataChiusura());
        }
        for (Long poi : pois) {
            it.addPOI(this.poiRepository.findById(poi).orElse(null));
        }
        Comune c = this.comuneRepository.findById(idComune).get();
        c.insertItinerario(it);
        this.comuneRepository.save(c);
    }

    public void creaItinerarioPending(Long idComune, ItinerarioFD i, Long[] pois) {
        Itinerario it = new Itinerario(i.getNome(), i.getDescrizione());
        if(i.getDataApertura()!=null && i.getDataChiusura()!=null){
            it.setDataApertura(i.getDataApertura());
            it.setDataChiusura(i.getDataChiusura());
        }
        for (Long poi : pois) {
            it.addPOI(this.poiRepository.findById(poi).orElse(null));
        }
        Comune c = this.comuneRepository.findById(idComune).get();
        c.insertItinerarioPending(it);
        this.comuneRepository.save(c);
    }
}
