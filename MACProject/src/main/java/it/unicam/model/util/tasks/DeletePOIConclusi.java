package it.unicam.model.util.tasks;
import it.unicam.model.POI;
import it.unicam.model.POIEvento;
import it.unicam.model.preferiti.GestionePreferiti;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.ItinerarioRepository;
import it.unicam.repositories.POIRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeletePOIConclusi {

    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    ItinerarioRepository itinerarioRepository;
    @Autowired
    private GestionePreferiti gestionePreferiti;


    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deletePOIConclusi()  {
        this.comuneRepository.findAll().forEach(c -> {
            List<POI> pois = c.eventiPOIConclusi();
            for (POI p : pois) {
                this.gestionePreferiti.deletePOI(p.getIdPOI());
                c.deletePOI(p.getIdPOI());
            }
            for(Long i : c.notItinerario()){
                this.gestionePreferiti.deleteItinerario(i);
            }
            c.deleteNotItinerario();
            this.comuneRepository.save(c);
            this.itinerarioRepository.findAll().forEach(i -> {
                if(i.getPOIs().size() < 2)
                    this.itinerarioRepository.deleteById(i.getId());
            });
        });

    }
}
