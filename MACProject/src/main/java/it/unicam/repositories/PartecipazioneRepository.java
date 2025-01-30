package it.unicam.repositories;

import it.unicam.model.Partecipazione;
import it.unicam.model.PartecipazioneId;
import org.springframework.data.repository.CrudRepository;

public interface PartecipazioneRepository extends CrudRepository<Partecipazione, PartecipazioneId> {
}
