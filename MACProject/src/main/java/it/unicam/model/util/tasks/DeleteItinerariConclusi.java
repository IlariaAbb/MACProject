package it.unicam.model.util.tasks;
import it.unicam.model.preferiti.GestionePreferiti;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.ItinerarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class DeleteItinerariConclusi {

    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private GestionePreferiti gestionePreferiti;


    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteItinerariConclusi(){
        this.comuneRepository.findAll().forEach(comune -> {
            comune.itinerariConclusi().forEach(itinerario -> {
              this.gestionePreferiti.deleteItinerario(itinerario.getId());
              comune.deleteItinerario(itinerario.getId());
              this.comuneRepository.save(comune);
            });
        });
    }
}
