package com.example.header;

public class Configuracion {
    private int updateFrequency; // Frecuencia de actualización en milisegundos
    private boolean showPopup;   // Estado del checkbox para "Mostrar pop-up en pantalla"
    private boolean hideTable;   // Estado del checkbox para "Ocultar tabla"
    private String sectionCount;    // Número de secciones visibles (por ejemplo, 4 u 8)

    // Constructor por defecto con valores predeterminados
    public Configuracion() {
        this.updateFrequency = 2000; // Valor predeterminado de 2000 ms
        this.showPopup = false;      // Valor predeterminado de no mostrar pop-up
        this.hideTable = false;      // Valor predeterminado de no ocultar la tabla
        this.sectionCount = "8 secciones";       // Valor predeterminado para el número de secciones visibles
    }

    // Constructor que acepta valores para todas las propiedades
    public Configuracion(int updateFrequency, boolean showPopup, boolean hideTable, String sectionCount) {
        this.updateFrequency = updateFrequency;
        this.showPopup = showPopup;
        this.hideTable = hideTable;
        this.sectionCount = sectionCount;
    }

    // Getters y Setters
    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }

    public boolean isHideTable() {
        return hideTable;
    }

    public void setHideTable(boolean hideTable) {
        this.hideTable = hideTable;
    }

    public String getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(String sectionCount) 
    {
        this.sectionCount = sectionCount;
    }
}
