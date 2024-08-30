package com.example;

import javax.swing.JFrame;
import java.awt.Graphics;

public class SmallDividedSquare {

    public static void main(String[] args) {
        // Crear una ventana
        JFrame frame = new JFrame();
        frame.setSize(100, 100); // Tamaño de la ventana 100x100 píxeles
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Añadir un componente para dibujar directamente
        frame.add(new javax.swing.JComponent() {
            @Override
            public void paint(Graphics g) {
                // Dibujar cuatro cuadrados pequeños en un área de 50x50
                int size = 25; // Tamaño de cada cuadrado pequeño
                g.drawRect(0, 0, size, size);       // Cuadrante superior izquierdo
                g.drawRect(size, 0, size, size);    // Cuadrante superior derecho
                g.drawRect(0, size, size, size);    // Cuadrante inferior izquierdo
                g.drawRect(size, size, size, size); // Cuadrante inferior derecho
            }
        });

        // Mostrar la ventana
        frame.setVisible(true);
    }
}
