package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididas 
{

    // Panel para dibujar un círculo
    public static class CirculoPanel extends JPanel 
    {
        private Color color;

        public CirculoPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(10, 10)); // Establecer el tamaño reducido
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int diameter = 8; // Reducir el diámetro a la mitad
            g2d.fillOval(0, 0, diameter, diameter); // Dibuja el círculo en 8x8 píxeles
        }
    }

    // Panel para dibujar un cuadrado
    public static class CuadradoPanel extends JPanel {
        private Color color;

        public CuadradoPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(8, 8)); // Establecer el tamaño reducido
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int size = 8; // Reducir el tamaño a la mitad
            g2d.fillRect(0, 0, size, size); // Dibuja el cuadrado en 8x8 píxeles
        }
    }

    // Panel para dibujar un triángulo
    public static class TrianguloPanel extends JPanel {
        private Color color;

        public TrianguloPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(8, 8)); // Establecer el tamaño reducido
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int[] xPoints = {0, 4, 8}; // Coordenadas para el triángulo de 8x8 píxeles
            int[] yPoints = {8, 0, 8};
            g2d.fillPolygon(xPoints, yPoints, 3); // Dibuja el triángulo en el espacio de 8x8 píxeles
        }
    }
}
