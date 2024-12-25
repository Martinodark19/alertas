package com.example.alertas.ui_modificadores;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

import com.example.alertas.MainSwing;
import com.example.alertas.configuracion.ConfigProperties;
import com.example.alertas.figuras.FigurasDivididas;


public class ModificadoresInterfaz 
{

        private JLabel selectedSectionLabel; // Para cambiar el título de la sección


        public static void showConfigDialog(JFrame owner) 
        {
            JDialog configDialog = new JDialog(owner, "Configuración", true);
            configDialog.setSize(480, 250);
            configDialog.setLayout(new BorderLayout());

            // Panel para la configuración
            JPanel configPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 filas para incluir los nuevos inputs
            configPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Campo de frecuencia en milisegundos
            JLabel msLabel = new JLabel("Frecuencia de actualización (ms):");
            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
            JFormattedTextField msField = new JFormattedTextField(numberFormat);
            // Cambiar el color del texto a rojo
            msField.setDisabledTextColor(Color.BLUE);
            msField.setEnabled(false);

            msField.setText(ConfigProperties.getProperty("app.updateFrequency").trim());
            // Nueva opción para "Mostrar pop-up en pantalla"
            
            JLabel popupLabel = new JLabel("Mostrar pop-up en pantalla:");
            MainSwing.popupCheckBox.setEnabled(false);

            // Nueva opción para "Mostrar pop-up en pantalla"
            JLabel hideTable = new JLabel("Ocultar tabla:");
            hideTable.setAlignmentX(Component.LEFT_ALIGNMENT);
            MainSwing.hideTableCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Deshabilitar para que no sean clickeables
            MainSwing.hideTableCheckBox.setEnabled(false);


            // Nueva opción para "Secciones en pantalla"
            JLabel sectionsLabelConfiguration = new JLabel("Secciones en pantalla:");
            String[] sectionOptions = { "4", "8" };
            JComboBox<String> sectionComboBox = new JComboBox<>(sectionOptions);
            

            String sectionSelectFromProperties = ConfigProperties.getProperty("app.sections").trim();

            if (sectionSelectFromProperties.contains("4")) 
            {
                sectionComboBox.setSelectedItem(sectionSelectFromProperties);
                //removeSpecificSections(new int[] {2,4,6,8});

                sectionComboBox.setEnabled(false);
                UIManager.put("ComboBox.disabledForeground", new Color(0, 0, 255)); // Azul

                // Actualizar la apariencia del ComboBox
                SwingUtilities.updateComponentTreeUI(sectionComboBox);
            }
            else if (sectionSelectFromProperties.contains("8")) 
            {
                sectionComboBox.setSelectedItem(sectionSelectFromProperties);
                addSpecificSectionsFromMap(new int[] {2, 4, 6, 8});
                MainSwing.sectionsPanel.revalidate();
                MainSwing.sectionsPanel.repaint();
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, 
                "La configuración de 'app.sections' contiene un valor no válido: '" + sectionSelectFromProperties + "'.\n" +
                "Por favor, verifique que el archivo de configuración no contenga espacios adicionales o caracteres incorrectos.\n" +
                "Se usará el valor por defecto: 4 secciones.", 
                "Advertencia de Configuración", 
                JOptionPane.WARNING_MESSAGE);

                sectionComboBox.setSelectedItem("4"); // Valor por defecto
            }

            // Añadir los componentes al panel
            configPanel.add(msLabel); // Etiqueta de frecuencia en milisegundos
            configPanel.add(msField); // Campo para ingresar los milisegundos
            configPanel.add(popupLabel); // Etiqueta para mostrar pop-up
            configPanel.add(MainSwing.popupCheckBox); // Input para activar/desactivar el pop-up
            configPanel.add(hideTable);
            configPanel.add(MainSwing.hideTableCheckBox);
            configPanel.add(sectionsLabelConfiguration); // Etiqueta para seleccionar el número de secciones
            configPanel.add(sectionComboBox); // ComboBox para seleccionar 4 u 8 secciones

            // Añadir el panel de configuración al diálogo
            configDialog.add(configPanel, BorderLayout.CENTER);

            configDialog.setLocationRelativeTo(owner); // Centrar el diálogo
            configDialog.setVisible(true);
        }


        public static JButton selectedColorButtonForAlertConfigColor; // Para actualizar el color del botón en el diálogo de
        private static JButton saveButton = new JButton("Guardar");

        public static void showAlertConfigDialog(JFrame owner) 
        {
            colorMap.put("Rojo",    "#FF0000");
            colorMap.put("Verde",   "#00FF00");
            colorMap.put("Azul",    "#0000FF");
            colorMap.put("Cian",    "#00FFFF");
            colorMap.put("Magenta", "#FF00FF");
            colorMap.put("Amarillo","#FFFF00");
            colorMap.put("Negro",   "#000000");
            colorMap.put("Blanco",  "#FFFFFF");
            colorMap.put("Gris",    "#808080");
            colorMap.put("Naranja", "#FFA500");
            colorMap.put("Púrpura", "#800080");
            colorMap.put("Rosa",    "#FFC0CB");

            selectedColorButtonForAlertConfigColor = new JButton("Seleccionar Color");

            selectedColorButtonForAlertConfigColor.setEnabled(false); // Deshabilitado inicialmente


            // Crear el botón ANTES de usarlo

            JDialog alertDialog = new JDialog(owner, "Configurar Alerta", true);
            alertDialog.setSize(400, 300);
            alertDialog.setLayout(new BorderLayout());

            // Panel principal del diálogo
            JPanel configPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            String alertTypeFromProperties = MainSwing.alertasConfig.getTipoAlerta();
            
            // Tipo de alerta
            JLabel alertTypeLabel = new JLabel("Tipo de Alerta:");
            String[] alertTypes = { "1", "2", "3", "4" };
            JComboBox<String> alertTypeComboBox = new JComboBox<>(alertTypes);
            alertTypeComboBox.setSelectedItem(MainSwing.alertasConfig.getTipoAlerta());



            String alertSeverityFromProperties = MainSwing.alertasConfig.getSeveridad();

            // Convertir la primera letra a mayúscula
            if (!alertSeverityFromProperties.isEmpty()) 
            {
                alertSeverityFromProperties = alertSeverityFromProperties.substring(0, 1).toUpperCase() 
                + alertSeverityFromProperties.substring(1).toLowerCase();
            }

            // Severidad
            JLabel severityLabel = new JLabel("Severidad:");
            String[] severities = {"Seleccionar...","Critica","Alta", "Media", "Baja" };
            
            JComboBox<String> severityComboBox = new JComboBox<>(severities);
            severityComboBox.setSelectedItem("Seleccionar...");

            selectedColorButtonForAlertConfigColor.setEnabled(false);
            saveButton.setEnabled(false);
            
 


            //severityComboBox.setSelectedItem(MainSwing.alertasConfig.getSeveridad());
            //severityComboBox.setSelectedIndex(-1);

            //Obtener severidad para actualizar color
            final String[] selectedSeverity = {""};

            // Agregar ActionListener al JComboBox de severidades
            severityComboBox.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {   
                    // Ajustado para que reciba Map<String, String>
                    //eliminarColoresDeSeveridad(colorMap);

                    if(severityComboBox.getSelectedItem().equals("Seleccionar..."))
                    {
                        selectedColorButtonForAlertConfigColor.setEnabled(false);
                        saveButton.setEnabled(false);
                    }
                    else
                    {
                        selectedColorButtonForAlertConfigColor.setEnabled(true);
                        saveButton.setEnabled(true);
                    }

                    // Obtener la severidad seleccionada
                    selectedSeverity[0] = (String) severityComboBox.getSelectedItem();

                    // Obtener el color actual del botón como nuevo valor para la severidad seleccionada
                    Color colorDeLaSeveridad = MainSwing.coloresPorSeveridadMap.get(selectedSeverity[0]);

                    if (colorDeLaSeveridad != null) 
                    {
                        //actualizar el mapa por que cambio la severidad
                        MainSwing.coloresPorSeveridadMap.put(selectedSeverity[0], colorDeLaSeveridad);

                        selectedColorButtonForAlertConfigColor.setBackground(colorDeLaSeveridad);
                    } 
                    else 
                    {
                        // Si no existe una entrada en el mapa para esta severidad, puedes poner un color por defecto
                        selectedColorButtonForAlertConfigColor.setBackground(Color.GRAY);
                    }
                    
                    // Confirmar en consola
                    //System.out.println("Se actualizó el color de " + selectedSeverity + " a: " + selectedSeverity[0]);
                }
            });
            

            String alertShapeFromProperties = MainSwing.alertasConfig.getForma();

            // Convertir la primera letra a mayúscula
            if (!alertShapeFromProperties.isEmpty()) 
            {
                alertShapeFromProperties = alertShapeFromProperties.substring(0, 1).toUpperCase() 
                + alertShapeFromProperties.substring(1).toLowerCase();
            }

            // Forma
            JLabel shapeLabel = new JLabel("Forma:");
            String[] shapes = { "Circulo", "Triangulo", "Cuadrado" };
            JComboBox<String> shapeComboBox = new JComboBox<>(shapes);
            shapeComboBox.setSelectedItem(MainSwing.alertasConfig.getForma());

            // Color
            JLabel colorLabel = new JLabel("Color:");

            // Usar un arreglo para almacenar severityColor

            final Color[] severityColor = new Color[1];    

            selectedColorButtonForAlertConfigColor.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {

                    String severidadActual = selectedSeverity[0];

                                // Obtener los colores ya asignados
                    Map<String, Color> coloresAsignados = MainSwing.coloresPorSeveridadMap;

                    // Llama al modal del selector de color para las alertas
                    Color selectedColor = showColorPickerModalForAlerts(alertDialog, coloresAsignados);

                    // Si se selecciona un color (no se cierra el modal sin elegir)
                    if (selectedColor != null) 
                    {
                        // Cambiar el color de fondo del botón al color seleccionado
                        selectedColorButtonForAlertConfigColor.setBackground(selectedColor);
                        MainSwing.coloresPorSeveridadMap.put(severidadActual, selectedColor);

                        // Opcional: si quieres guardar este color en alguna variable de configuración
                        MainSwing.alertasConfig.setColor(selectedColor);
                    }
                }
            });

            
            // Obtener el color de la configuración
            try 
            {

                selectedColorButtonForAlertConfigColor.setBackground(MainSwing.alertasConfig.getColor());
                //selectedColorButtonForAlertConfigColor.setEnabled(true);
            } 
            catch (NumberFormatException e) 
            {
                // Si el color no es válido, muestra un mensaje de advertencia al usuario y usa un valor por defecto
                JOptionPane.showMessageDialog(
                    null,
                    "El valor configurado para 'alert.color' es inválido o no es un color soportado: '" + MainSwing.alertasConfig.getColor() + "'.\n" +
                    "Por favor, asegúrese de que sea un código hexadecimal válido (ejemplo: #FF0000).\n" +
                    "Se usará el valor por defecto: #CCCCCC.",
                    "Advertencia de Configuración",
                    JOptionPane.WARNING_MESSAGE
                );
                // Configura un color por defecto
                selectedColorButtonForAlertConfigColor.setBackground(Color.LIGHT_GRAY);
            }

            // Panel para el botón Guardar
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            //JButton saveButton = new JButton("Guardar");

            // Acción del botón Guardar
            saveButton.addActionListener(e -> {
                // Guardar los valores seleccionados en AlertasConfig
                MainSwing.alertasConfig.setTipoAlerta((String) alertTypeComboBox.getSelectedItem());
                MainSwing.alertasConfig.setSeveridad((String) severityComboBox.getSelectedItem());
                MainSwing.alertasConfig.setForma((String) shapeComboBox.getSelectedItem());
                MainSwing.alertasConfig.setColor(selectedColorButtonForAlertConfigColor.getBackground());

                JOptionPane.showMessageDialog(alertDialog, "Configuración guardada con éxito.");
                MainSwing.alertasConfig.saveConfig();
                alertDialog.dispose(); // Cerrar el diálogo después de guardar
            });

            // Agregar el botón al panel
            buttonPanel.add(saveButton);

            // Agregar componentes al panel de configuración
            configPanel.add(alertTypeLabel);
            configPanel.add(alertTypeComboBox);
            configPanel.add(severityLabel);
            configPanel.add(severityComboBox);
            configPanel.add(shapeLabel);
            configPanel.add(shapeComboBox);
            configPanel.add(colorLabel);
            configPanel.add(selectedColorButtonForAlertConfigColor);

            // Agregar el panel de configuración y el panel del botón al diálogo
            alertDialog.add(configPanel, BorderLayout.CENTER);
            alertDialog.add(buttonPanel, BorderLayout.SOUTH);

            alertDialog.setLocationRelativeTo(owner);
            alertDialog.setVisible(true);
        }




        public static void showTitleChangeModal(JFrame owner, String sectionName) 
        {
            // Crear el diálogo
            JDialog titleDialog = new JDialog(owner, "Cambiar Título", true);
        
            // Establecer un BorderLayout para colocar componentes uno debajo del otro (NORTH-CENTER-SOUTH)
            titleDialog.setLayout(new BorderLayout(10, 10));
        
            // Panel para el campo de texto (arriba)
            JTextField titleField = new JTextField();
            titleField.setPreferredSize(new Dimension(300, 40)); // Ancho de 300 píxeles y alto de 25 píxeles


            // Definir las columnas
            String[] columnNames = 
            {
                "alertaid", 
                "codalerta", 
                "nombre", 
                "sentenciaId", 
                "inicioevento", 
                "identificacionalerta", 
                "nombreActivo", 
                "proceso", 
                "latencia", 
                "tipoServicio", 
                "CI", 
                "Subtiposervicio", 
                "jitter", 
                "disponibilidad", 
                "packetlost", 
                "rssi", 
                "nsr", 
                "PLM", 
                "tipoExWa", 
                "codigoEvento", 
                "descripcionevento", 
                "Origen", 
                "tipodocumento", 
                "estado", 
                "resumen", 
                "titulo", 
                "numero", 
                "fechaestado", 
                "razonestado", 
                "gpsx", 
                "gpsy", 
                "gpsz", 
                "gpsh", 
                "radio", 
                "severidad", 
                "userid", 
                "comentario", 
                "valida", 
                "ot", 
                "ticket", 
                "fecha_reconocimiento", 
                "grupo_local"
            };
            
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
            // Obtener alertas de la sección
            List<Object[]> alertsForSection = MainSwing.databaseConnection.getAlertsByProceso(sectionName.substring(8));
            for (Object[] alertRow : alertsForSection) 
            {
                tableModel.addRow(alertRow);
            }
        
            JTable alertTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(alertTable);
        
            // Ajustar un tamaño preferido más grande para la tabla si se desea
            scrollPane.setPreferredSize(new Dimension(800, 300));

                // Habilitar el desplazamiento horizontal si es necesario
            alertTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
            // Botón para cerrar (abajo)
            JButton closeButton = new JButton("Cerrar");
            closeButton.addActionListener(e -> {
                String newTitle = titleField.getText();
                if (!newTitle.isEmpty()) {
                    MainSwing.selectedSectionLabel.setText(newTitle); // Cambiar el título de la sección
                }
                titleDialog.dispose();
            });
        
            // Añadir componentes al diálogo con BorderLayout
            titleDialog.add(titleField, BorderLayout.NORTH);
            titleDialog.add(scrollPane, BorderLayout.CENTER);
            titleDialog.add(closeButton, BorderLayout.SOUTH);
        
            // Ajustar el tamaño del diálogo a los componentes
            titleDialog.pack();
        
            // Centrar el diálogo respecto a la ventana padre
            titleDialog.setLocationRelativeTo(owner);
            titleDialog.setVisible(true);
        }


        public static JButton createColorButton(String color) 
        {
            JButton button = new JButton();
            button.setBackground(Color.decode(color));
            button.setPreferredSize(new Dimension(50, 50));
            button.addActionListener(e -> 
            {
                if (MainSwing.selectedSection != null) 
                {
                    MainSwing.selectedSection.setBackground(Color.decode(color));
                } 
                else if (MainSwing.selectedColorButton != null) 
                {
                    MainSwing.selectedColorButton.setBackground(Color.decode(color));
                }
            });
            return button;
        }


        public static Map<String, String> colorMap = new LinkedHashMap<>();

        public static Color showColorPickerModalForAlerts(JDialog owner, Map<String, Color> coloresAsignados) 
{
    // Crear una copia del colorMap para evitar modificar el mapa estático original
    Map<String, String> coloresDisponibles = new LinkedHashMap<>(colorMap);

    // Eliminar los colores ya asignados
    for (Color colorAsignado : coloresAsignados.values()) {
        coloresDisponibles.entrySet().removeIf(entry -> {
            Color color = Color.decode(entry.getValue());
            return color.equals(colorAsignado);
        });
    }

    // Crear el diálogo para seleccionar color
    JDialog colorDialog = new JDialog(owner, "Seleccione un color", true);
    colorDialog.setSize(600, 400);
    colorDialog.setLayout(new BorderLayout());

    JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
    colorButtonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    final Color[] selectedColor = { null }; // Array para almacenar el color seleccionado

    // Crear un botón por cada color disponible
    for (Map.Entry<String, String> entry : coloresDisponibles.entrySet()) 
    {
        String colorHex = entry.getValue();

        JButton colorButton = new JButton();
        colorButton.setBackground(Color.decode(colorHex));
        colorButton.setPreferredSize(new Dimension(50, 50));
        colorButton.setFocusPainted(false);
        colorButton.setBorderPainted(false); // Opcional: elimina el borde del botón
        colorButton.setOpaque(true); // Asegura que el color de fondo se muestre correctamente

        // Acción al hacer clic: se selecciona el color y se cierra el modal
        colorButton.addActionListener(e -> {
            selectedColor[0] = Color.decode(colorHex);
            colorDialog.dispose();
        });

        colorButtonsPanel.add(colorButton);
    }

    // Botón de cerrar sin seleccionar un color
    JButton closeButton = new JButton("Cerrar");
    closeButton.addActionListener(e -> colorDialog.dispose());

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(closeButton);

    colorDialog.add(colorButtonsPanel, BorderLayout.CENTER);
    colorDialog.add(bottomPanel, BorderLayout.SOUTH);

    colorDialog.setLocationRelativeTo(owner);
    colorDialog.setVisible(true);

    return selectedColor[0]; // Devolver el color seleccionado
}


        
        /**
         * Ajusta el método para recibir un Map<String, String>,
         * porque 'colorMap' usa hexadecimales.
         */
        public static void eliminarColoresDeSeveridad(Map<String, String> colorMap) {
            // Obtenemos el mapa de severidad (String -> Color)
            Map<String, Color> severidadMap = MainSwing.getColoresPorSeveridadMap();
        
            // Eliminamos las entradas de 'colorMap' cuyo valor, decodificado, coincide
            // con alguno de los valores de severidadMap.
            for (Color colorSeveridad : severidadMap.values()) {
                colorMap.entrySet().removeIf(entry -> {
                    Color decodedColor = Color.decode(entry.getValue());


                    return decodedColor.equals(colorSeveridad);
                });
            }
        }
        



        public static void addSpecificSectionsFromMap(int[] sectionsToAdd) 
        {
            for (int index : sectionsToAdd) {
                JPanel sectionPanel = MainSwing.allSections.get(index);
                if (sectionPanel != null && sectionPanel.getParent() == null) 
                {
                    // Agrega la sección en su posición original
                    MainSwing.sectionsPanel.add(sectionPanel, index - 1); // Restamos 1 si los índices comienzan en 1
                }
            }
            MainSwing.sectionsPanel.revalidate();
            MainSwing.sectionsPanel.repaint();
        }


        public static void removeSpecificSections(int[] sectionsToRemove) 
        {
            for (int index : sectionsToRemove) 
            {
                JPanel sectionPanel = MainSwing.allSections.get(index);
                if (sectionPanel != null && sectionPanel.getParent() != null) 
                {
                    MainSwing.sectionsPanel.remove(sectionPanel);
                }
            }
            MainSwing.sectionsPanel.revalidate();
            MainSwing.sectionsPanel.repaint();
        }


                    // Método para mostrar el modal del selector de color
    public static void showColorPickerModal(JFrame owner) 
    {
        JDialog colorDialog = new JDialog(owner, "Seleccione un color", true);
        colorDialog.setSize(600, 400);
        colorDialog.setLayout(new BorderLayout());

        JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        colorButtonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] colors = { "#FF0000", "#00FF00", "#0000FF", "#00FFFF", "#FF00FF", "#FFFF00",
                "#000000", "#FFFFFF", "#808080", "#FFA500", "#800080", "#FFC0CB" };

        for (String color : colors) 
        {
            JButton colorButton = createColorButton(color);
            colorButtonsPanel.add(colorButton);
        }

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> colorDialog.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);

        colorDialog.add(colorButtonsPanel, BorderLayout.CENTER);
        colorDialog.add(bottomPanel, BorderLayout.SOUTH);

        colorDialog.setLocationRelativeTo(owner);
        colorDialog.setVisible(true);
    }




            public static void openPopupWithTable(Object[][] alertData) 
            {
                List<String> listaAlertData = new ArrayList<>();

                // Recorrer filas
                for (Object[] row : alertData) 
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

                JDialog tableDialog = new JDialog((Frame) null, "Detalles de la Alerta", true); // No se pasa el owner
                tableDialog.setSize(800, 600); // Tamaño de la ventana emergente
                tableDialog.setLayout(new BorderLayout());


                // Columnas para la tabla (deberían coincidir con los datos pasados en
                // alertData)
                String[] columnNames = 
                {
                        "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", "Identificación Alerta",
                        "Nombre Activo", "Proceso", "Latencia", "Tipo Servicio", "CI", "Subtipo Servicio",
                        "Jitter", "Disponibilidad", "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa",
                        "Código Evento", "Descripción Evento", "Origen", "Tipo Documento", "Estado",
                        "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado"
                };


                // Panel principal para el formulario
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

                // Añadir un botón para cerrar el diálogo
                JButton closeButton = new JButton("Cerrar");
                closeButton.addActionListener(new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tableDialog.dispose();
                    }
                });

                // Añadir el formulario y el botón al frame
                tableDialog.setLayout(new BorderLayout());
                tableDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);

                // Mostrar el frame
                tableDialog.setLocationRelativeTo(null);
                tableDialog.setVisible(true);
            }



            public static JPanel getRandomValueFromMap(Map<Integer, JPanel> map) 
            {
                Random random = new Random();
                // Convertimos los valores del mapa a una lista
                List<JPanel> values = new ArrayList<>(map.values());
                // Seleccionamos un índice aleatorio
                int randomIndex = random.nextInt(values.size());
                // Retornamos el valor en la posición aleatoria
                return values.get(randomIndex);
            }


            public static Boolean verificarGuardadoValid = false;

            public static boolean showCommentDialogForValid(Integer alertaId, String usuario) 
            {
                // Crear el diálogo
                JDialog dialog = new JDialog((Frame) null, "Agregar Comentario", true);
                dialog.setSize(400, 200);
                dialog.setLayout(new BorderLayout());
                dialog.setLocationRelativeTo(null);
            
                // Crear el panel principal
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
                // Etiqueta para el comentario
                JLabel label = new JLabel("Ingrese un comentario (máximo 80 caracteres):");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
            
                // Campo de texto para el comentario
                JTextField commentField = new JTextField();
                commentField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            
                // Botón para guardar
                JButton saveButton = new JButton("Guardar");
                saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
                final boolean[] resultado = {false};
            
                saveButton.addActionListener(e -> {
                    String comment = commentField.getText();
                    if (comment.length() > 80) 
                    {
                        JOptionPane.showMessageDialog(dialog, "El comentario no debe exceder los 80 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                    } 
                    else 
                    {
                        if (comment.length() < 1) {
                            JOptionPane.showMessageDialog(dialog, "El comentario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
            
                        Boolean actualizarAlerta = MainSwing.databaseConnection.actualizarAlertaValid(alertaId, usuario, comment);
                        if (actualizarAlerta) 
                        {

                            resultado[0] = true;
                            dialog.dispose();
                        } 
                        else 
                        {
                            JOptionPane.showMessageDialog(
                                null,
                                "No se pudo actualizar la alerta con ID " + alertaId + ".\nVerifique los datos ingresados o contacte al administrador.",
                                "Error de Actualización",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
            
                // Añadir componentes al panel
                panel.add(label);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
                panel.add(commentField);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
                panel.add(saveButton);
            
                // Añadir panel al diálogo
                dialog.add(panel, BorderLayout.CENTER);
            
                // Mostrar el diálogo
                dialog.setVisible(true);
            
                // Retornar el resultado de la operación
                return resultado[0];
            }
            


            public static boolean eliminarFigurasLeidasInterfaz(Integer idAlertaFigura) 
            {
                boolean figuraEliminada = false;
            
                // Iterar sobre todas las secciones
                for (Map.Entry<Integer, JPanel> entry : MainSwing.allSections.entrySet()) 
                {
                    JPanel seccionPanel = entry.getValue();
            
                    Component[] componentes = seccionPanel.getComponents();
                    for (Component componente : componentes) 
                    {
                        if (componente instanceof JPanel) 
                        {
                            JPanel panel = (JPanel) componente;
            
                            // Buscar figuras dentro de este panel
                            for (Component subComponente : panel.getComponents()) 
                            {       
                                if (subComponente instanceof FigurasDivididas.CirculoPanel ||
                                    subComponente instanceof FigurasDivididas.CuadradoPanel ||
                                    subComponente instanceof FigurasDivididas.TrianguloPanel) 
                                {
                                    // Obtener el nombre de la figura
                                    String figuraId = subComponente.getName();
            
                                    if (figuraId != null && figuraId.contains(idAlertaFigura.toString())) 
                                    {
                                        String obtenerNombre = obtenerNombreDeUsuario();
            
                                        if (obtenerNombre != null) 
                                        {
                                            panel.remove(subComponente); // Eliminar la figura del panel
                                            MainSwing.sectionsPanel.revalidate();
                                            MainSwing.sectionsPanel.repaint();
                                            figuraEliminada = true;
                                            break; // Salir del bucle de subcomponentes
                                        }
                                    }
                                }
                            }
            
                            if (figuraEliminada) 
                            {
                                break; // Salir del bucle de componentes
                            }
                        }
                    }
            
                    if (figuraEliminada) 
                    {
                        break; // Salir del bucle de secciones
                    }
                }
            
                // Si no se encontró la figura, mostrar mensaje de error
                if (!figuraEliminada) 
                {
                    JOptionPane.showMessageDialog(
                        null,
                        "No se encontró la figura con el ID especificado: " + idAlertaFigura + ".\nPor favor, verifique los datos e intente nuevamente.",
                        "Error desconocido al Eliminar Figura",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            
                return figuraEliminada;
            }
            


            public static String obtenerNombreDeUsuario() 
            {
                try 
                {
                    // Obtener el nombre del usuario actual de Windows
                    String usuario = System.getProperty("user.name");
            
                    // Construir el comando para obtener la información del usuario
                    String comando = "net user " + usuario;
            
                    // Ejecutar el comando
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", comando);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();
            
                    // Leer la salida del comando
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String linea;
                    String nombreUsuario = null; // Variable para almacenar el nombre de usuario
            
                    while ((linea = reader.readLine()) != null) 
                    {
                        // Buscar la línea que contiene "Nombre de usuario"
                        if (linea.startsWith("Nombre de usuario")) 
                        {
                            // Extraer el valor después de "Nombre de usuario"
                            nombreUsuario = linea.substring("Nombre de usuario".length()).trim();
                            break; // Salimos del bucle al encontrarlo
                        }
                    }

                    reader.close();
            
                    // Retornar el nombre de usuario
                    if (nombreUsuario != null) 
                    {
                        return nombreUsuario;
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(null, "No se pudo encontrar el nombre de usuario.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                } 
                catch (Exception e) 
                {

                    JOptionPane.showMessageDialog(null, 
                    "Ha ocurrido un error inesperado al intentar obtener el nombre de usuario.\n\n"
                    + "Detalles técnicos: " + e.getMessage() + "\n\n"
                    + "Sugerencia: Verifica que el sistema operativo sea compatible y que tienes permisos adecuados.",
                    "Error Crítico", 
                    JOptionPane.ERROR_MESSAGE);
                    return null;

                }
            }
            
            


}
