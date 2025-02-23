package it.unicam.model.controllersGRASP;

import it.unicam.model.Comune;
import it.unicam.model.Contenuto;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContenutoController {

    @Autowired
    private ComuneRepository comuneRepository;

    public void insertContenutoPOI(Long idComune, Long idPOI, ContenutoFD c) {
        Contenuto contenuto = new Contenuto(c.getNome(), c.getDescrizione(), c.getFile());
        Comune com = this.comuneRepository.findById(idComune).get();
        com.getPOI(idPOI).addContenuto(contenuto);
        this.comuneRepository.save(com);
    }

    public void insertContenutoPendingPOI(Long idComune, Long idPOI, ContenutoFD c) {
        Contenuto contenuto = new Contenuto(c.getNome(), c.getDescrizione(), c.getFile());
        Comune com = this.comuneRepository.findById(idComune).get();
        com.getPOI(idPOI).addContenutoPending(contenuto);
        this.comuneRepository.save(com);
    }
}
