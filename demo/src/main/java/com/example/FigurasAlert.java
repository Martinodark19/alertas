package com.example;

import javax.swing.*;
import java.awt.*;

// Clase FigurasAlert que puede dibujar diferentes formas
public class FigurasAlert extends JPanel 
{

    private Color color;

    public FigurasAlert() {
        // Constructor vacío para instanciar el panel con un color por defecto
        this.color = Color.GRAY; // Color por defecto
    }

    // Método para establecer el color y solicitar el redibujado
    public void setColor(Color color) 
    {
        this.color = color;
        repaint(); // Solicitar que el panel se redibuje con el nuevo color
    }

    // Dibujo del círculo con el color almacenado
    private void drawCircle(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval(50, 50, 25, 25); // Dibujar círculo
    }

    // Dibujo del cuadrado con el color almacenado
    private void drawSquare(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRect(50, 50, 25, 25); // Dibujar cuadrado
    }

    // Dibujo del triángulo con el color almacenado
    private void drawTriangle(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        int[] xPoints = {50, 62, 75};
        int[] yPoints = {75, 50, 75};
        g2d.fillPolygon(xPoints, yPoints, 3); // Dibujar triángulo
    }

    @FunctionalInterface
    public interface Drawable 
    {
        void draw(Graphics g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Aquí puedes elegir qué forma dibujar
        // Para el ejemplo, digamos que quieres dibujar un círculo:
        drawCircle(g);

        // Otras formas posibles:
        // drawSquare(g);
        // drawTriangle(g);
    }
}
