package it.unicam.model.util.dtos;

import it.unicam.model.Coordinate;
import it.unicam.model.Tipo;

public class POIGI {
    private final Long id;
    private final String nome;
    private final String descrizione;
    private final Coordinate coordinate;
    private final Tipo tipo;

    public POIGI(Long id, String nome, String descrizione, Coordinate coordinata, Tipo tipo) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.coordinate = coordinata;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }


    @Override
    public String toString() {
        return  "Id: "+this.id+" Nome= " + nome +
                "\nDescrizione=" + descrizione +
                "\n Coordinate= lon " + coordinate.getLatitudine() + " lon " +coordinate.getLongitudine()+
                "\n Tipo=" + tipo;
    }
}
