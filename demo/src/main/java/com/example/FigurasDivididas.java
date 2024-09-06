package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FigurasDivididas 
{

    public static class CirculoPanel extends JPanel 
    {
        private Color color;
        private Object[][] data;
        private int borderWidth = 2; // Grosor del borde


        public CirculoPanel(Color color, Object[][] data) 
        {
            this.color = color;
            this.data = data;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30));

            this.addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    openDetailWindow();
                }
            });
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

        private void openDetailWindow() {
            JFrame detailFrame = new JFrame("Detalle del Círculo");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());

            String[] columnNames = 
            {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", "Identificación Alerta",
                "Nombre Activo", "Proceso", "Latencia", "Tipo Servicio", "CI", "Subtipo Servicio", 
                "Jitter", "Disponibilidad", "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa",
                "Código Evento", "Descripción Evento", "Origen", "Tipo Documento", "Estado",
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado"
            };

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);

            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });

            // Agregar componentes a la ventana
            detailFrame.add(scrollPane, BorderLayout.CENTER);
            detailFrame.add(closeButton, BorderLayout.SOUTH);

            // Ajustar el tamaño de la ventana al contenido
            detailFrame.pack();
            detailFrame.setLocationRelativeTo(null);  // Centrar la ventana
            detailFrame.setVisible(true);
        }
    }

    public static class CuadradoPanel extends JPanel 
    {
        private Color color;
        private Object[][] data;
        private int borderWidth = 2; // Grosor del borde


        public CuadradoPanel(Color color, Object[][] data) {
            this.color = color;
            this.data = data;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30));

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openDetailWindow();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            int size = 24 - borderWidth; // Ajustar el tamaño del cuadrado para el grosor del borde
            g2d.fillRect(borderWidth / 2, borderWidth / 2, size, size); // Dibuja el cuadrado relleno

            g2d.setColor(Color.BLACK); // Configurar el color del borde
            g2d.setStroke(new BasicStroke(borderWidth)); // Configurar el grosor del borde
            g2d.drawRect(borderWidth / 2, borderWidth / 2, size, size); // Dibuja el borde del cuadrado
        }

        private void openDetailWindow() {
            JFrame detailFrame = new JFrame("Detalle del Cuadrado");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());

            String[] columnNames = {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", "Identificación Alerta",
                "Nombre Activo", "Proceso", "Latencia", "Tipo Servicio", "CI", "Subtipo Servicio", 
                "Jitter", "Disponibilidad", "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa",
                "Código Evento", "Descripción Evento", "Origen", "Tipo Documento", "Estado",
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado"
            };

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);

            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });

            // Agregar componentes a la ventana
            detailFrame.add(scrollPane, BorderLayout.CENTER);
            detailFrame.add(closeButton, BorderLayout.SOUTH);

            // Ajustar el tamaño de la ventana al contenido
            detailFrame.pack();
            detailFrame.setLocationRelativeTo(null);  // Centrar la ventana
            detailFrame.setVisible(true);
        }
    }

    public static class TrianguloPanel extends JPanel {
        private Color color;
        private Object[][] data;
        private int borderWidth = 1; // Grosor del borde


        public TrianguloPanel(Color color, Object[][] data) {
            this.color = color;
            this.data = data;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30));

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openDetailWindow();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) 
        {
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

        private void openDetailWindow() {
            JFrame detailFrame = new JFrame("Detalle del Triángulo");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());

            String[] columnNames = {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", "Identificación Alerta",
                "Nombre Activo", "Proceso", "Latencia", "Tipo Servicio", "CI", "Subtipo Servicio", 
                "Jitter", "Disponibilidad", "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa",
                "Código Evento", "Descripción Evento", "Origen", "Tipo Documento", "Estado",
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado"
            };

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);

            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });

            // Agregar componentes a la ventana
            detailFrame.add(scrollPane, BorderLayout.CENTER);
            detailFrame.add(closeButton, BorderLayout.SOUTH);

            // Ajustar el tamaño de la ventana al contenido
            detailFrame.pack();
            detailFrame.setLocationRelativeTo(null);  // Centrar la ventana
            detailFrame.setVisible(true);
        }
    }
}
