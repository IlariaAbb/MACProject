package it.unicam.model.util.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItinerarioGI {
    private final Long id;
    private final String nome;
    private final String descrizione;
    private final LocalDateTime dataApertura;
    private final LocalDateTime dataChiusura;

    public ItinerarioGI(Long id, String nome, String descrizione, LocalDateTime dataApertura, LocalDateTime dataChiusura) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public LocalDateTime getDataChiusura() {
        return dataChiusura;
    }

    @Override
    public String toString() {
        String s;
        if (dataApertura == null || dataChiusura == null){
            s = "Validità: Sempre attivo";
        }else{
            s = "Data di inizio: "+this.dataApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))+" - "+
                    "Data di chiusura: "+this.dataChiusura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        }
        return "Id= " + id + " Nome= " + nome +
                "\nDescrizione= " + descrizione +
                "\n"+s;
    }
}
