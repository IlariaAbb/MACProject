package it.unicam.repositories;

import it.unicam.model.preferiti.POIPreferiti;
import it.unicam.model.preferiti.IdPOIPreferiti;
import org.springframework.data.repository.CrudRepository;

public interface POIPreferitiRepository extends CrudRepository<POIPreferiti, IdPOIPreferiti>{
}
