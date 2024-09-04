package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididasTest {

    public static void main(String[] args) {
        // Crear una nueva ventana (JFrame)
        JFrame frame = new JFrame("Visualización de Figuras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Crear un panel principal para contener las figuras
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Espaciado entre figuras

        // Datos de prueba para la tabla en cada figura
        Object[][] testData = {
            {"1", "A001", "Alerta Prueba 1", "123", "2023-09-04 12:00:00", "ID001", "Activo 1", "Proceso 1", "10.5", "Servicio 1", "CI001", "Subtipo 1", "5.1", "99.5", "0.1", "50", "1.0", "0.1", "ExWa1", "Evento001", "Descripción 1", "Origen 1", "TipoDoc 1", "Activo", "Resumen 1", "Título 1", "100", "2023-09-04 13:00:00", "Razón 1"}
        };

        // Crear instancias de cada figura con un color específico y los datos de prueba
        FigurasDivididas.CirculoPanel circulo = new FigurasDivididas.CirculoPanel(Color.RED, testData);
        FigurasDivididas.CuadradoPanel cuadrado = new FigurasDivididas.CuadradoPanel(Color.BLUE, testData);
        FigurasDivididas.TrianguloPanel triangulo = new FigurasDivididas.TrianguloPanel(Color.GREEN, testData);

        // Añadir las figuras al panel principal
        mainPanel.add(circulo);
        mainPanel.add(cuadrado);
        mainPanel.add(triangulo);

        // Añadir el panel principal al marco
        frame.add(mainPanel);

        // Hacer visible la ventana
        frame.setVisible(true);
    }
}
