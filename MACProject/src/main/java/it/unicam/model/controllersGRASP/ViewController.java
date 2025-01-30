package it.unicam.model.controllersGRASP;

import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ItinerarioFD;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewController {
    @Autowired
    private ComuneRepository comuneRepository;

    public POIFD viewSelectPOI(Long idComune, Long idPOI) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().viewSelectPOI(idPOI);
    }

    public ContenutoFD viewContenuto(Long idComune, Long idPOI, Long idContenuto) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().viewContenuto(idPOI, idContenuto);
    }

    public ItinerarioFD selectItinerario(Long idComune, Long idItinerario) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().selectItinerario(idItinerario);
    }


    public ContenutoFD viewContenutoPendingPOI(Long idComune, Long idPOI, Long idContenuto){
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().viewContenutoPendingPOI(idPOI, idContenuto);
    }


    public POIFD selectPOIPending(Long idComune, Long id) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().selectPOIPending(id);
    }

    public ContenutoFD selectContenutoPending(Long idComune, Long idPOI, Long idContenuto) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().selectContenutoPending(idPOI, idContenuto);
    }

    public ItinerarioFD selectItinerarioPending(Long idComune, Long id) {
        if (this.comuneRepository.findById(idComune).isEmpty())
            return null;
        else
            return this.comuneRepository.findById(idComune).get().selectItinerarioPending(id);
    }
}
