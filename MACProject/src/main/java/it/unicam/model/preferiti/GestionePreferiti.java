package it.unicam.model.preferiti;

import it.unicam.model.Itinerario;
import it.unicam.model.POI;
import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.util.dtos.ItinerarioGI;
import it.unicam.model.util.dtos.POIGI;
import it.unicam.repositories.ItinerariPreferitiRepository;
import it.unicam.repositories.POIPreferitiRepository;
import it.unicam.repositories.ItinerarioRepository;
import it.unicam.repositories.POIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GestionePreferiti {

    @Autowired
    private POIPreferitiRepository POIPreferitiRepository;
    @Autowired
    private ItinerariPreferitiRepository itinerariPreferitiRepository;
    @Autowired
    private POIRepository poiRepository;
    @Autowired
    private ItinerarioRepository itinerarioRepository;
    @Autowired
    private GestioneUtentiAutenticati gestioneUtentiAutenticati;

    public boolean addPOIAiPreferiti(Long id, Long idPOI, Long idComune) {
        if(!this.poiRepository.existsById(idPOI)){
            return false;
        }
        if (!this.POIPreferitiRepository.existsById(new IdPOIPreferiti(idPOI, id))) {
            this.POIPreferitiRepository.save(
                    new POIPreferiti(this.poiRepository.findById(idPOI).get(),
                                     this.gestioneUtentiAutenticati.getUtente(id)));
            return true;
        }
        return false;
    }

    public boolean addItinerarioAiPreferiti(Long id, Long idItinerario, Long idComune) {
        if (!this.itinerarioRepository.existsById(idItinerario)) {
            return false;
        }
        if (!this.itinerariPreferitiRepository.existsById(new IdItinerariPreferiti(idItinerario, id))) {
            this.itinerariPreferitiRepository.save(
                    new ItinerariPreferiti(this.itinerarioRepository.findById(idItinerario).get(),
                                           this.gestioneUtentiAutenticati.getUtente(id)));
            return true;
        }
        return false;
    }

    public List<POIGI> getAllPOIPreferiti(Long id) {
        List<POIGI> list = new ArrayList<>();
        this.POIPreferitiRepository.findAll().forEach(f -> {
            if (f.getId().getIdUtente().equals(id)) {
                list.add(f.getPoi().getInfoGeneraliPOI());
            }
        });
        return list;
    }

    public List<ItinerarioGI> getAllItinerariPreferiti(Long id) {
        List<ItinerarioGI> list = new ArrayList<>();
        this.itinerariPreferitiRepository.findAll().forEach(f -> {
            if (f.getId().getIdUtente().equals(id)) {
                list.add(f.getItinerario().getInfoGeneraliItinerario());
            }
        });
        return list;
    }

    public void deletePOI(Long id) {
        this.POIPreferitiRepository.findAll().forEach(f -> {
            if (f.getPoi().getIdPOI().equals(id)) {
                this.POIPreferitiRepository.delete(f);
            }
        });
    }

    public void deleteItinerario(Long id) {
        this.itinerariPreferitiRepository.findAll().forEach(f -> {
            if (f.getItinerario().getId().equals(id)) {
                this.itinerariPreferitiRepository.delete(f);
            }
        });
    }

    public boolean removePOIDaiPreferiti(Long idUtente, Long idPOI) {
        IdPOIPreferiti compositeKey = new IdPOIPreferiti(idPOI, idUtente);
        if (this.POIPreferitiRepository.existsById(compositeKey)) {
            this.POIPreferitiRepository.deleteById(compositeKey);
            return true;
        }
        return false;
    }

    public boolean removeItinerarioDaiPreferiti(Long idUtente, Long idItinerario) {
        IdItinerariPreferiti compositeKey = new IdItinerariPreferiti(idItinerario, idUtente);
        if (this.itinerariPreferitiRepository.existsById(compositeKey)) {
            this.itinerariPreferitiRepository.deleteById(compositeKey);
            return true;
        }
        return false;
    }
}
