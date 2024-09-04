package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididas {

    public static class CirculoPanel extends JPanel {
        private Color color;
        private int borderWidth = 2; // Grosor del borde

        public CirculoPanel(Color color) 
        {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30)); // Triplicar el tamaño del panel
        }

        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int diameter = 24; // Triplicar el diámetro
            g2d.fillOval(borderWidth / 2, borderWidth / 2, diameter, diameter); // Dibuja el círculo relleno

            g2d.setColor(Color.BLACK); // Configurar el color del borde
            g2d.setStroke(new BasicStroke(borderWidth)); // Configurar el grosor del borde
            g2d.drawOval(borderWidth / 2, borderWidth / 2, diameter, diameter); // Dibuja el borde del círculo
        }
    }

    public static class CuadradoPanel extends JPanel {
        private Color color;
        private int borderWidth = 1; // Grosor del borde

        public CuadradoPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30)); // Triplicar el tamaño del panel
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int size = 24 - borderWidth; // Ajustar el tamaño del cuadrado para el grosor del borde
            g2d.fillRect(borderWidth / 2, borderWidth / 2, size, size); // Dibuja el cuadrado relleno

            g2d.setColor(Color.BLACK); // Configurar el color del borde
            g2d.setStroke(new BasicStroke(borderWidth)); // Configurar el grosor del borde
            g2d.drawRect(borderWidth / 2, borderWidth / 2, size, size); // Dibuja el borde del cuadrado
        }
    }

    public static class TrianguloPanel extends JPanel {
        private Color color;
        private int borderWidth = 1; // Grosor del borde

        public TrianguloPanel(Color color) {
            this.color = color;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30)); // Triplicar el tamaño del panel
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int[] xPoints = {borderWidth / 2, 15, 30 - borderWidth / 2}; // Ajustar las coordenadas x para el borde
            int[] yPoints = {30 - borderWidth / 2, borderWidth / 2, 30 - borderWidth / 2}; // Ajustar las coordenadas y para el borde
            g2d.fillPolygon(xPoints, yPoints, 3); // Dibuja el triángulo relleno

            g2d.setColor(Color.BLACK); // Configurar el color del borde
            g2d.setStroke(new BasicStroke(borderWidth)); // Configurar el grosor del borde
            g2d.drawPolygon(xPoints, yPoints, 3); // Dibuja el borde del triángulo
        }
    }
}
