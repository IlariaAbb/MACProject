package it.unicam.repositories;

import it.unicam.model.segnalazioni.Segnalazione;
import org.springframework.data.repository.CrudRepository;

public interface SegnalazioneRepository extends CrudRepository<Segnalazione, Long> {
}
