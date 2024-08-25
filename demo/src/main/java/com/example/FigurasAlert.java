package com.example;

import javax.swing.*;
import java.awt.*;

// Clase ShapePanel que puede dibujar diferentes formas
public class FigurasAlert extends JPanel {

    public FigurasAlert() {
        // Constructor vacío para instanciar el panel sin definir la figura aún
    }

    // Método para dibujar un círculo
    public void drawCircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillOval(50, 50, 25, 25); // Dibujar círculo
    }

    // Método para dibujar un cuadrado
    public void drawSquare(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fillRect(50, 50, 25, 25); // Dibujar cuadrado
    }

    // Método para dibujar un triángulo
    public void drawTriangle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        int[] xPoints = {50, 62, 75};
        int[] yPoints = {75, 50, 75};
        g2d.fillPolygon(xPoints, yPoints, 3); // Dibujar triángulo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Puedes elegir dibujar una forma llamando a uno de los métodos
        // drawCircle(g);
        // drawSquare(g);
        // drawTriangle(g);
    }
}
