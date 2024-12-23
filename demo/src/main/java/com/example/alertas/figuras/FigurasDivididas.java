package com.example.alertas.figuras;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


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

        private void openDetailWindow() 
        {

            List<String> listaAlertData = new ArrayList<>();

            for (Object[] row : data) 
            {
                // Recorrer columnas
                for (Object elem : row) 
                {
                    // Verificar que el elemento no sea null antes de convertirlo a String
                    if (elem != null) 
                    {
                        listaAlertData.add(elem.toString()); // Convertir a String de forma segura
                    } 
                    else 
                    {
                        listaAlertData.add(""); // Añadir una cadena vacía si el valor es null
                    }
                }
            }

            JFrame detailFrame = new JFrame("Detalle del Círculo");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());
            detailFrame.setSize(800, 600);


            
            String[] columnNames = 
            {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", 
                "Identificación Alerta", "Nombre Activo", "Proceso", "Latencia", 
                "Tipo Servicio", "CI", "Subtipo Servicio", "Jitter", "Disponibilidad", 
                "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa", "Código Evento", 
                "Descripción Evento", "Origen", "Tipo Documento", "Estado", 
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado", 
                "GPS X", "GPS Y", "GPS Z", "GPS H", "Radio", "Severidad", 
                "User ID", "Comentario", "Valida", "OT", "Ticket", 
                "Fecha Reconocimiento", "Grupo Local"
            };

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(columnNames.length, 2, 10, 10)); // Dos columnas: etiqueta y campo de texto
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Crear un JTextField para cada campo
            JTextField[] textFields = new JTextField[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) 
            {
                JLabel label = new JLabel(columnNames[i] + ":");
                JTextField textField = new JTextField();
                textFields[i] = textField; // Guardar referencia al JTextField
                textFields[i].setText(listaAlertData.get(i));

                formPanel.add(label);
                formPanel.add(textField);
            }
                        
            
            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });




            // Añadir el formulario y el botón al frame
            detailFrame.setLayout(new BorderLayout());
            detailFrame.add(new JScrollPane(formPanel), BorderLayout.CENTER);

            // Mostrar el frame
            detailFrame.setLocationRelativeTo(null);
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

        private void openDetailWindow() 
        {
            List<String> listaAlertData = new ArrayList<>();

            for (Object[] row : data) 
            {
                // Recorrer columnas
                for (Object elem : row) 
                {
                    // Verificar que el elemento no sea null antes de convertirlo a String
                    if (elem != null) 
                    {
                        listaAlertData.add(elem.toString()); // Convertir a String de forma segura
                    } 
                    else 
                    {
                        listaAlertData.add(""); // Añadir una cadena vacía si el valor es null
                    }
                }
            }

            JFrame detailFrame = new JFrame("Detalle del Cuadrado");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());
            detailFrame.setSize(800, 600);


            String[] columnNames = 
            {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", 
                "Identificación Alerta", "Nombre Activo", "Proceso", "Latencia", 
                "Tipo Servicio", "CI", "Subtipo Servicio", "Jitter", "Disponibilidad", 
                "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa", "Código Evento", 
                "Descripción Evento", "Origen", "Tipo Documento", "Estado", 
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado", 
                "GPS X", "GPS Y", "GPS Z", "GPS H", "Radio", "Severidad", 
                "User ID", "Comentario", "Valida", "OT", "Ticket", 
                "Fecha Reconocimiento", "Grupo Local"
            };



            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(columnNames.length, 2, 10, 10)); // Dos columnas: etiqueta y campo de texto
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));




                    // Crear un JTextField para cada campo
            JTextField[] textFields = new JTextField[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) 
            {
                JLabel label = new JLabel(columnNames[i] + ":");
                JTextField textField = new JTextField();
                textFields[i] = textField; // Guardar referencia al JTextField
                textFields[i].setText(listaAlertData.get(i));

                formPanel.add(label);
                formPanel.add(textField);
            }


            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });

            // Añadir el formulario y el botón al frame
            detailFrame.setLayout(new BorderLayout());
            detailFrame.add(new JScrollPane(formPanel), BorderLayout.CENTER);

            // Mostrar el frame
            detailFrame.setLocationRelativeTo(null);
            detailFrame.setVisible(true);
        }
    }

    public static class TrianguloPanel extends JPanel 
    {
        private Color color;
        private Object[][] data;
        private int borderWidth = 1; // Grosor del borde


        public TrianguloPanel(Color color, Object[][] data) 
        {
            this.color = color;
            this.data = data;
            this.setOpaque(false);
            this.setPreferredSize(new Dimension(30, 30));

            this.addMouseListener(new MouseAdapter() 
            {
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

        private void openDetailWindow() 
        {

            List<String> listaAlertData = new ArrayList<>();

            for (Object[] row : data) 
            {
                // Recorrer columnas
                for (Object elem : row) 
                {
                    // Verificar que el elemento no sea null antes de convertirlo a String
                    if (elem != null) 
                    {
                        listaAlertData.add(elem.toString()); // Convertir a String de forma segura
                    } 
                    else 
                    {
                        listaAlertData.add(""); // Añadir una cadena vacía si el valor es null
                    }
                }
            }

            JFrame detailFrame = new JFrame("Detalle del Triángulo");
            detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailFrame.setLayout(new BorderLayout());
            detailFrame.setLayout(new BorderLayout());
            detailFrame.setSize(800, 600);

            
            String[] columnNames = 
            {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", 
                "Identificación Alerta", "Nombre Activo", "Proceso", "Latencia", 
                "Tipo Servicio", "CI", "Subtipo Servicio", "Jitter", "Disponibilidad", 
                "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa", "Código Evento", 
                "Descripción Evento", "Origen", "Tipo Documento", "Estado", 
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado", 
                "GPS X", "GPS Y", "GPS Z", "GPS H", "Radio", "Severidad", 
                "User ID", "Comentario", "Valida", "OT", "Ticket", 
                "Fecha Reconocimiento", "Grupo Local"
            };


            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(columnNames.length, 2, 10, 10)); // Dos columnas: etiqueta y campo de texto
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            




            // Crear un JTextField para cada campo
            JTextField[] textFields = new JTextField[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) 
            {
                JLabel label = new JLabel(columnNames[i] + ":");
                JTextField textField = new JTextField();
                textFields[i] = textField; // Guardar referencia al JTextField
                textFields[i].setText(listaAlertData.get(i));

                formPanel.add(label);
                formPanel.add(textField);
            }

            // Botón de Cerrar
            JButton closeButton = new JButton("Cerrar Ventana");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailFrame.dispose();
                }
            });

            // Añadir el formulario y el botón al frame
            detailFrame.setLayout(new BorderLayout());
            detailFrame.add(new JScrollPane(formPanel), BorderLayout.CENTER);

            // Mostrar el frame
            detailFrame.setLocationRelativeTo(null);
            detailFrame.setVisible(true);
        }
    }
}
