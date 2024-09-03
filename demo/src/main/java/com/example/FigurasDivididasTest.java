package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididasTest {

    public static void main(String[] args) {
        // Crear una nueva ventana (JFrame)
        JFrame frame = new JFrame("Visualización de Figuras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        
        // Crear un panel principal para contener las figuras
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Espaciado entre figuras

        // Crear instancias de cada figura con un color específico
        FigurasDivididas.CirculoPanel circulo = new FigurasDivididas.CirculoPanel(Color.RED);
        FigurasDivididas.CuadradoPanel cuadrado = new FigurasDivididas.CuadradoPanel(Color.BLUE);
        FigurasDivididas.TrianguloPanel triangulo = new FigurasDivididas.TrianguloPanel(Color.GREEN);

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
