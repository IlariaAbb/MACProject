package it.unicam.repositories;

import it.unicam.model.utenti.UtenteAutenticato;
import org.springframework.data.repository.CrudRepository;

public interface UtenteAutenticatoRepository extends CrudRepository<UtenteAutenticato, Long>{
    UtenteAutenticato findByUsername(String username);

}
