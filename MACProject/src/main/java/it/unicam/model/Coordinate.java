package it.unicam.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;


@Embeddable
public class Coordinate {

    private double latitudine;
    private double longitudine;

    public Coordinate(double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Coordinate() {

    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }
}
