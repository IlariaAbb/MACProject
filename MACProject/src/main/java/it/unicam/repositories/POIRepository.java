package it.unicam.repositories;

import it.unicam.model.POI;
import org.springframework.data.repository.CrudRepository;

public interface POIRepository extends CrudRepository<POI, Long> {
}
