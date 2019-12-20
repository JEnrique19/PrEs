package com.example.pres;

import android.app.Application;

public class GlobalClass extends Application {
    private String textoPrueba;
    private Double lat;
    private Double lon;
    private String grados;
    private Float bearing;

    public String getTextoPrueba() {
        return textoPrueba;
    }

    public void setTextoPrueba(String textoPrueba) {
        this.textoPrueba = textoPrueba;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Float getBearing() {
        return bearing;
    }

    public void setBearing(Float bearing) {
        this.bearing = bearing;
    }

    public String getGrados() {
        return grados;
    }

    public void setGrados(String grados) {
        this.grados = grados;
    }
}
