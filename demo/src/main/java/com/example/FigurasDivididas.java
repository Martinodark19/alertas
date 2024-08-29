package com.example;

import javax.swing.*;
import java.awt.*;

public class FigurasDivididas {

    // Panel para dibujar un círculo
    public static class CirculoPanel extends JPanel {
        private Color color;

        public CirculoPanel(Color color) {
            this.color = color; // Establecer el color recibido como parámetro
            this.setOpaque(false); // Hacer el fondo transparente
            this.setPreferredSize(new Dimension(30, 30)); // Establecer el tamaño preferido al tamaño de la figura
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color); // Usar el color recibido
            int diameter = Math.min(getWidth(), getHeight()); // Asegura que el círculo se ajuste al panel
            g2d.fillOval(0, 0, diameter, diameter); // Dibuja el círculo desde la posición (0,0)
        }
    }

    // Panel para dibujar un cuadrado
    public static class CuadradoPanel extends JPanel {
        private Color color;

        public CuadradoPanel(Color color) {
            this.color = color; // Establecer el color recibido como parámetro
            this.setOpaque(false); // Hacer el fondo transparente
            this.setPreferredSize(new Dimension(30, 30)); // Establecer el tamaño preferido al tamaño de la figura
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color); // Usar el color recibido
            int size = Math.min(getWidth(), getHeight()); // Asegura que el cuadrado se ajuste al panel
            g2d.fillRect(0, 0, size, size); // Dibuja el cuadrado desde la posición (0,0)
        }
    }

    // Panel para dibujar un triángulo
    public static class TrianguloPanel extends JPanel {
        private Color color;

        public TrianguloPanel(Color color) {
            this.color = color; // Establecer el color recibido como parámetro
            this.setOpaque(false); // Hacer el fondo transparente
            this.setPreferredSize(new Dimension(15, 15)); // Establecer el tamaño preferido ligeramente mayor
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color); // Usar el color recibido
            int[] xPoints = {0, 7, 15}; // Ajustar las coordenadas para el tamaño mayor
            int[] yPoints = {15, 0, 15};
            g2d.fillPolygon(xPoints, yPoints, 3); // Dibuja el triángulo ocupando todo el panel
        }
    }
}
