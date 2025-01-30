package it.unicam.model;

public class POILuogoConOraFactory extends POIFactory{
    @Override
    public POI creaPOI(Coordinate c) {
        return new POILuogoConOra(c);
    }
}
