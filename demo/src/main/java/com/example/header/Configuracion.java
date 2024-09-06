package com.example.header;

public class Configuracion 
{
    private int updateFrequency; // Frecuencia de actualización en milisegundos
    private boolean showPopup;   // Estado del checkbox para "Mostrar pop-up en pantalla"
    private boolean hideTable;   // Estado del checkbox para "Ocultar tabla"

    // Constructor por defecto con valores predeterminados
    public Configuracion() 
    {
        this.updateFrequency = 2000; // Valor predeterminado de 2000 ms
        this.showPopup = false;      // Valor predeterminado de no mostrar pop-up
        this.hideTable = false;      // Valor predeterminado de no ocultar la tabla
    }

    // Constructor que acepta valores para todas las propiedades
    public Configuracion(int updateFrequency, boolean showPopup, boolean hideTable) {
        this.updateFrequency = updateFrequency;
        this.showPopup = showPopup;
        this.hideTable = hideTable;
    }

    // Getter para obtener la frecuencia de actualización
    public int getUpdateFrequency() 
    {
        return updateFrequency;
    }

    // Setter para establecer la frecuencia de actualización
    public void setUpdateFrequency(int updateFrequency) 
    {
        this.updateFrequency = updateFrequency;
    }

    // Getter para el estado de "Mostrar pop-up en pantalla"
    public boolean isShowPopup() 
    {
        return showPopup;
    }

    // Setter para el estado de "Mostrar pop-up en pantalla"
    public void setShowPopup(boolean showPopup) 
    {
        this.showPopup = showPopup;
    }

    // Getter para el estado de "Ocultar tabla"
    public boolean isHideTable() 
    {
        return hideTable;
    }

    // Setter para el estado de "Ocultar tabla"
    public void setHideTable(boolean hideTable) 
    {
        this.hideTable = hideTable;
    }
}
