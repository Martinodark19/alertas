package com.example.alertas.configuracion;


import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.example.alertas.MainSwing;

public class AlertasConfig 
{
    private String tipoAlerta;
    private String severidad;
    private String forma;
    private Color color;
    


   // // Constructor que acepta todos los parámetros
   public AlertasConfig() 
   {
    
    this.tipoAlerta = "1"; // Predeterminado: 1
    this.severidad = "Media"; // Predeterminado: Media
    this.forma = "circulo"; // Predeterminado: Circulo

    try 
    {
        this.color = MainSwing.getColoresPorSeveridadMap().get("Media");
    } 
    catch (NumberFormatException e) 
    {
        //System.err.println("Color inválido: " + colorHex + ". Usando predeterminado.");
        this.color = Color.LIGHT_GRAY;
    }

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


    // Método para guardar las configuraciones en el archivo de propiedades
    public void saveConfig() 
    {
        Properties properties = new Properties();

        properties.setProperty("alert.type", getTipoAlerta());
        properties.setProperty("alert.severity", getSeveridad());
        properties.setProperty("alert.shape", getForma());

        // Convertir el color a formato hexadecimal
        String colorHex = String.format("#%02x%02x%02x", this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        properties.setProperty("alert.color", colorHex);

        try (FileOutputStream output = new FileOutputStream("config.properties")) 
        {
            properties.store(output, "Configuraciones de Alerta");
        } catch (IOException e) {
            System.err.println("Error al guardar las configuraciones: " + e.getMessage());
        }
    }

}
