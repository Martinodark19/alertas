package com.example.alertas.figuras;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ShapePanel extends JPanel {

    private String shapeType; // Tipo de figura: "circulo", "cuadrado", "triangulo"
    private Color color;
    private int borderWidth;

    // Constructor
    public ShapePanel(String shapeType, Color color, int borderWidth) {
        this.shapeType = shapeType;
        this.color = color;
        this.borderWidth = borderWidth;
        this.setPreferredSize(new Dimension(25, 25)); // Tamaño reducido
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Tamaño reducido de la figura (50% del tamaño disponible)
        int shapeSize = Math.min(panelWidth, panelHeight) / 1;
        int offsetX = (panelWidth - shapeSize) / 2;
        int offsetY = (panelHeight - shapeSize) / 2;

        switch (shapeType.toLowerCase()) {
            case "circulo":
                // Dibujar círculo reducido
                g2d.setColor(color);
                g2d.fillOval(offsetX, offsetY, shapeSize, shapeSize); // Figura
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(borderWidth)); // Grosor del borde
                g2d.drawOval(offsetX, offsetY, shapeSize, shapeSize); // Borde
                break;

            case "cuadrado":
                // Dibujar cuadrado reducido
                g2d.setColor(color);
                g2d.fillRect(offsetX, offsetY, shapeSize, shapeSize); // Figura
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(borderWidth)); // Grosor del borde
                g2d.drawRect(offsetX, offsetY, shapeSize, shapeSize); // Borde
                break;

            case "triangulo":
                // Dibujar triángulo reducido
                g2d.setColor(color);
                int[] xPoints = {offsetX + shapeSize / 2, offsetX, offsetX + shapeSize};
                int[] yPoints = {offsetY, offsetY + shapeSize, offsetY + shapeSize};
                g2d.fillPolygon(xPoints, yPoints, 3); // Figura
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(borderWidth)); // Grosor del borde
                g2d.drawPolygon(xPoints, yPoints, 3); // Borde
                break;

            default:
                g2d.drawString("Figura no reconocida", 10, 20);
                break;
        }
    }

    // Método estático para crear un ShapePanel
    public static ShapePanel createShapePanel(String shapeType, Color color, int borderWidth) 
    {
        return new ShapePanel(shapeType, color, borderWidth);
    }



    private JPanel selectedSection;
    private JButton selectedColorButton;


        private JButton createColorButton(String color) 
    {
        JButton button = new JButton();
        button.setBackground(Color.decode(color));
        button.setPreferredSize(new Dimension(50, 50));
        button.addActionListener(e -> 
        {
            if (selectedSection != null) {
                selectedSection.setBackground(Color.decode(color));
            } else if (selectedColorButton != null) {
                selectedColorButton.setBackground(Color.decode(color));
            }
        });
        return button;
    }



    public static JPanel createFigureTipoServicioPanel() 
    {
    // Crear paneles con figuras directamente
    ShapePanel circulo = new ShapePanel("circulo", Color.RED, 0);
    ShapePanel cuadrado = new ShapePanel("cuadrado", Color.BLUE, 0);
    ShapePanel triangulo = new ShapePanel("triangulo", Color.GREEN, 0);
        // Crear el panel principal para el significado
        JPanel legendPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Significado de las Figuras"));
    
        // Crear componentes para cada tipo de servicio y figura
        JPanel tipo1Panel = new JPanel(new BorderLayout());
        tipo1Panel.add(circulo, BorderLayout.CENTER);
        tipo1Panel.add(new JLabel("Tipo 1 → Círculo", SwingConstants.CENTER), BorderLayout.SOUTH);
    
        JPanel tipo2Panel = new JPanel(new BorderLayout());
        tipo2Panel.add(cuadrado, BorderLayout.CENTER);
        tipo2Panel.add(new JLabel("Tipo 2 → Cuadrado", SwingConstants.CENTER), BorderLayout.SOUTH);
    
        JPanel tipo3Panel = new JPanel(new BorderLayout());
        tipo3Panel.add(triangulo, BorderLayout.CENTER);
        tipo3Panel.add(new JLabel("Tipo 3 → Triángulo", SwingConstants.CENTER), BorderLayout.SOUTH);
    
        // Añadir los paneles individuales al panel principal
        legendPanel.add(tipo1Panel);
        legendPanel.add(tipo2Panel);
        legendPanel.add(tipo3Panel);
    
        return legendPanel;
    }


    


}