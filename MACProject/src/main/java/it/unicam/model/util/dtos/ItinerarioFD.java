package it.unicam.model.util.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ItinerarioFD {
    private final Long id;
    @NotNull
    @NotBlank
    private final String nome;
    @NotNull
    @NotBlank
    private final String descrizione;
    private final List<POIGI> POIGIs;
    private final LocalDateTime dataApertura;
    private final LocalDateTime dataChiusura;


    public ItinerarioFD(Long id, String nome, String descrizione, LocalDateTime dataApertura, LocalDateTime dataChiusura, List<POIGI> POIGIs) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.POIGIs = POIGIs;
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<POIGI> getPOIGIs() {
        return POIGIs;
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
                "\n"+s+
                "\n"+"POI:\n"+POIGIs.stream().map(poigi -> poigi.toString()).collect(Collectors.joining("\n"));
    }
}
