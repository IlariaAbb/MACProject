package it.unicam.repositories;

import it.unicam.model.Comune;
import it.unicam.model.utenti.UtenteAutenticato;
import org.springframework.data.repository.CrudRepository;

public interface ComuneRepository extends CrudRepository<Comune, Long>{
    Comune findByCuratore(UtenteAutenticato curatore);
    Comune findByNome(String nome);
}
