package com.example;


import javax.swing.*;
import java.awt.*;

public class PruebaFiguras 
{

    public static void main(String[] args) 
    {
        // Crear el frame
        JFrame frame = new JFrame("Prueba de Figuras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(1, 3)); // 1 fila, 3 columnas

        // Crear paneles para cada figura usando las subclases de FigurasDivididas
        FigurasDivididas.CirculoPanel circuloPanel = new FigurasDivididas.CirculoPanel();
        FigurasDivididas.CuadradoPanel cuadradoPanel = new FigurasDivididas.CuadradoPanel();
        FigurasDivididas.TrianguloPanel trianguloPanel = new FigurasDivididas.TrianguloPanel();

        // Añadir los paneles al frame
        frame.add(circuloPanel);
        frame.add(cuadradoPanel);
        frame.add(trianguloPanel);

        // Hacer visible el frame
        frame.setVisible(true);
    }
}
