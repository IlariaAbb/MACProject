package it.unicam.model.utenti;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RichiestaRuolo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "richiestaRuolo_seq")
    private Long id;
    private Long idUtente;

    public RichiestaRuolo(Long id) {
        this.idUtente = id;
    }

    public RichiestaRuolo() {

    }

    public Long getIdUtente() {
        return this.idUtente;
    }
}
