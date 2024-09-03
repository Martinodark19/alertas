package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididas 
{

    public static class CirculoPanel extends JPanel {
        private Color color;
    
        public CirculoPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30)); // Triplicar el tamaño del panel
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int diameter = 24; // Triplicar el diámetro
            g2d.fillOval(0, 0, diameter, diameter); // Dibuja el círculo en 24x24 píxeles
        }
    }
    

    public static class CuadradoPanel extends JPanel {
        private Color color;
    
        public CuadradoPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(24, 24)); // Triplicar el tamaño del panel
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int size = 24; // Triplicar el tamaño del cuadrado
            g2d.fillRect(0, 0, size, size); // Dibuja el cuadrado en 24x24 píxeles
        }
    }
    

    public static class TrianguloPanel extends JPanel 
    {
        private Color color;
    
        public TrianguloPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(24, 24)); // Triplicar el tamaño del panel
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int[] xPoints = {0, 12, 24}; // Triplicar las coordenadas x del triángulo
            int[] yPoints = {24, 0, 24}; // Triplicar las coordenadas y del triángulo
            g2d.fillPolygon(xPoints, yPoints, 3); // Dibuja el triángulo en el espacio de 24x24 píxeles
        }
    }
    
}
