package com.example.header;

import java.util.HashMap;
import java.util.Map;
import java.awt.Color; // Importa el paquete Color


public class AlertasConfig 
{


    private String tipoAlerta;
    private String severidad;
    private String forma;
    private Color color;

    // Constructor que acepta todos los parámetros
    public AlertasConfig(String tipoAlerta, String severidad, String forma, Color color) 
    {
        this.tipoAlerta = tipoAlerta;
        this.severidad = severidad;
        this.forma = forma;
        this.color = color;
    }

    // Getters
    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public String getSeveridad() {
        return severidad;
    }

    public String getForma() {
        return forma;
    }

    public Color getColor() {
        return color;
    }

    // Setters
    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
