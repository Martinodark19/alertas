package com.example.alertas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingTimerExample {

    public static void main(String[] args) {
        // Crear una instancia del Timer que se ejecuta cada 2 segundos (2000 ms)
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí va el código que quieres ejecutar cada 2 segundos
                System.out.println("Tarea ejecutada: " + System.currentTimeMillis());
            }
        });

        // Iniciar el temporizador
        timer.start();

        // Mantener el programa corriendo para observar el Timer
        JOptionPane.showMessageDialog(null, "Cierra este mensaje para detener el programa");
    }
}
