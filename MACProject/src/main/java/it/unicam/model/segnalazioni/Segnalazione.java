package it.unicam.model.segnalazioni;

import it.unicam.model.utenti.UtenteAutenticato;
import jakarta.persistence.*;

@Entity
public class Segnalazione {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "segnalazione_seq")
    private Long id;

    private Long idPOI;
    private Long idContenuto;
    private String motivazione;

    @ManyToOne
    private UtenteAutenticato utenteSegnalante;

    @Enumerated(EnumType.STRING)
    private StatoSegnalazione stato;

    public Segnalazione() {
    }

    public Segnalazione(Long idPOI,
                        Long idContenuto,
                        String motivazione,
                        UtenteAutenticato utenteSegnalante) {
        this.idPOI = idPOI;
        this.idContenuto = idContenuto;
        this.motivazione = motivazione;
        this.utenteSegnalante = utenteSegnalante;
        this.stato = StatoSegnalazione.APERTA;
    }

    public Long getId() {
        return id;
    }

    public Long getIdPOI() {
        return idPOI;
    }

    public Long getIdContenuto() {
        return idContenuto;
    }

    public String getMotivazione() {
        return motivazione;
    }

    public UtenteAutenticato getUtenteSegnalante() {
        return utenteSegnalante;
    }

    public StatoSegnalazione getStato() {
        return stato;
    }

    public void setStato(StatoSegnalazione stato) {
        this.stato = stato;
    }
}
