package com.example.header;

public class Configuracion {
    private int updateFrequency; // Frecuencia de actualización en milisegundos

    // Constructor por defecto con un valor predeterminado
    public Configuracion() {
        this.updateFrequency = 2000; // Valor predeterminado de 1000 ms
    }

    // Constructor que acepta un valor para la frecuencia de actualización
    public Configuracion(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    // Getter para obtener la frecuencia de actualización
    public int getUpdateFrequency() {
        return updateFrequency;
    }

    // Setter para establecer la frecuencia de actualización
    public void setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }
}
