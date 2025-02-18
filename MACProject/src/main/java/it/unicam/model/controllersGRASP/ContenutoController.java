package it.unicam.model.controllersGRASP;

import it.unicam.model.Comune;
import it.unicam.model.Contenuto;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.UpdateContenutoFD;
import it.unicam.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContenutoController {

    @Autowired
    private ComuneRepository comuneRepository;

    public void insertContenutoPOI(Long idComune, Long id, ContenutoFD c) {
        Contenuto contenuto = new Contenuto(c.getNome(), c.getDescrizione(), c.getFile());
        Comune com = this.comuneRepository.findById(idComune).get();
        com.getPOI(id).addContenuto(contenuto);
        this.comuneRepository.save(com);
    }

    public void insertContenutoPendingPOI(Long idComune, Long id, ContenutoFD c) {
        Contenuto contenuto = new Contenuto(c.getNome(), c.getDescrizione(), c.getFile());
        Comune com = this.comuneRepository.findById(idComune).get();
        com.getPOI(id).addContenutoPending(contenuto);
        this.comuneRepository.save(com);
    }

    public void updateContenutoPendingPOI(Long idComune, Long idPOI, UpdateContenutoFD c) {
        Contenuto contenuto = new Contenuto(
                c.getNome() + " (MODIFICA DI ID=" + c.getIdOriginalContenuto() + ")",
                c.getDescrizione(),
                c.getFile()
        );
        Comune com = this.comuneRepository.findById(idComune).get();
        com.getPOI(idPOI).addContenutoPending(contenuto);
        this.comuneRepository.save(com);
    }

    public void applyUpdateContenuto(Long idComune, Long idPOI, Long idContenutoPending, Long idContenutoOriginale) {
        Comune com = this.comuneRepository.findById(idComune).get();
        Contenuto pending = com.getPOI(idPOI).getContenutiPending()
                .stream().filter(cc -> cc.getIdContenuto().equals(idContenutoPending))
                .findFirst().orElse(null);
        Contenuto original = com.getPOI(idPOI).getContenuti()
                .stream().filter(cc -> cc.getIdContenuto().equals(idContenutoOriginale))
                .findFirst().orElse(null);
        if (pending == null || original == null) return;
    
        original.setNome(pending.getNome());
        original.setDescrizione(pending.getDescrizione());
        original.setFile(pending.getFile());
    
        com.getPOI(idPOI).deleteContenutoPending(idContenutoPending);
    
        this.comuneRepository.save(com);
    }
    
}
