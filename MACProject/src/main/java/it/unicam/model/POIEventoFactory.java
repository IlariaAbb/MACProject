package it.unicam.model;

public class POIEventoFactory extends POIFactory{
    @Override
    public POI creaPOI(Coordinate c) {
        return new POIEvento(c);
    }
}
