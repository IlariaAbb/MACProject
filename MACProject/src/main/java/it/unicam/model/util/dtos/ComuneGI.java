package it.unicam.model.util.dtos;

import it.unicam.model.Coordinate;
import it.unicam.model.utenti.UtenteAutenticato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComuneGI {


    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private Coordinate coordinate;

    @NotNull
    private UtenteAutenticatoGI curatore;

    public ComuneGI(){
    }

    public String getNome() {
        return nome;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public UtenteAutenticatoGI getCuratore() {
        return curatore;
    }
}
