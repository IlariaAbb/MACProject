package it.unicam.model;

public class POILuogoFactory extends POIFactory{
    @Override
    public POI creaPOI(Coordinate c) {
        return new POILuogo(c);
    }
}
