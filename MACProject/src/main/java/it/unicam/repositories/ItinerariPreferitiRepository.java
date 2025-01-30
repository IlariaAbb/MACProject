package it.unicam.repositories;

import it.unicam.model.preferiti.ItinerariPreferiti;
import it.unicam.model.preferiti.IdItinerariPreferiti;
import org.springframework.data.repository.CrudRepository;

public interface ItinerariPreferitiRepository extends CrudRepository<ItinerariPreferiti, IdItinerariPreferiti> {

}
