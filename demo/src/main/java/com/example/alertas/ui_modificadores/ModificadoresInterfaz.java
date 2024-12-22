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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
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

import com.example.alertas.MainSwing;
import com.example.alertas.configuracion.ConfigProperties;


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


        public static void showAlertConfigDialog(JFrame owner) 
        {

            // Crear el botón ANTES de usarlo
            MainSwing.selectedColorButtonForAlertConfigColor = new JButton("Seleccionar Color");




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
            //alertTypeComboBox.setSelectedItem(alertTypeFromProperties);

            alertTypeComboBox.setSelectedItem(MainSwing.alertasConfig.getTipoAlerta());
            // Verificar si el valor de alertTypeFromProperties está en los valores permitidos
            /* 
            if (Arrays.asList(alertTypes).contains(alertTypeFromProperties)) 
            {
                alertTypeComboBox.setSelectedItem(alertTypeFromProperties);
                // Cambiar el color del texto deshabilitado
                UIManager.put("ComboBox.disabledForeground", new Color(0, 0, 255)); // Azul

                // Actualizar la apariencia del ComboBox
                SwingUtilities.updateComponentTreeUI(alertTypeComboBox);
                alertTypeComboBox.setEnabled(true);
            } 
            else 
            {
                // Mostrar mensaje al usuario
                JOptionPane.showMessageDialog(null, 
                    "El valor configurado para 'alert.type' es inválido o no permitido: '" + alertTypeFromProperties + "'.\n" +
                    "Por favor, asegúrese de que el valor esté entre los siguientes: " + String.join(", ", alertTypes) + ".\n" +
                    "Se usará el valor por defecto: 1.", 
                    "Advertencia de Configuración", 
                    JOptionPane.WARNING_MESSAGE);

                // Usar un valor por defecto
                alertTypeComboBox.setSelectedItem("1");
            }
            */
            String alertSeverityFromProperties = MainSwing.alertasConfig.getSeveridad();

            // Convertir la primera letra a mayúscula
            if (!alertSeverityFromProperties.isEmpty()) 
            {
                alertSeverityFromProperties = alertSeverityFromProperties.substring(0, 1).toUpperCase() 
                + alertSeverityFromProperties.substring(1).toLowerCase();
            }

            // Severidad
            JLabel severityLabel = new JLabel("Severidad:");
            String[] severities = { "Critica","Alta", "Media", "Baja" };
            
            JComboBox<String> severityComboBox = new JComboBox<>(severities);
            severityComboBox.setSelectedItem(MainSwing.alertasConfig.getSeveridad());
            /* 
            if (Arrays.asList(severities).contains(alertSeverityFromProperties)) 
            {
                //verifica si el map contiene la severidad 
                if (coloresPorSeveridadMap.containsKey(severityComboBox.getSelectedItem())) 
                {
                    //obtener el color de la severidad
                    Color colorSeveridad = coloresPorSeveridadMap.get(severityComboBox.getSelectedItem());
                    //asignar el color al boton
                    selectedColorButtonForAlertConfigColor.setBackground(colorSeveridad);
                }
                else
                {
                    System.out.println("paso por el else");
                    
                }
                severityComboBox.setSelectedItem(alertSeverityFromProperties);
                //añaidr el color de la severidad al map para que en solicitudes futuras se pueda obtener el color en el map
                
                severityComboBox.setEnabled(true);
            } 
            else 
            {
                // Mostrar mensaje al usuario si el valor no es válido
                JOptionPane.showMessageDialog(null,
                    "El valor configurado para 'alert.severity' es inválido o no permitido: '" + alertSeverityFromProperties + "'.\n" +
                    "Por favor, asegúrese de que el valor esté entre los siguientes: " + String.join(", ", severities) + ".\n" +
                    "Se usará el valor predeterminado: Media.",
                    "Advertencia de Configuración",
                    JOptionPane.WARNING_MESSAGE);
            
                // Usar un valor por defecto si el valor de la propiedad no es válido
                severityComboBox.setSelectedItem("Media");
            }

            */
            //Obtener severidad para actualizar color
            final String[] selectedSeverity = {""};

            // Agregar ActionListener al JComboBox de severidades

            severityComboBox.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    // Obtener la severidad seleccionada
                    selectedSeverity[0] = (String) severityComboBox.getSelectedItem();

                    // Obtener el color actual del botón como nuevo valor para la severidad seleccionada
                    Color colorDeLaSeveridad = MainSwing.coloresPorSeveridadMap.get(selectedSeverity[0]);

                    if (colorDeLaSeveridad != null) 
                    {
                        //actualizar el mapa por que cambio la severidad
                        MainSwing.coloresPorSeveridadMap.put(selectedSeverity[0], colorDeLaSeveridad);

                        MainSwing.selectedColorButtonForAlertConfigColor.setBackground(colorDeLaSeveridad);
                    } 
                    else 
                    {
                        // Si no existe una entrada en el mapa para esta severidad, puedes poner un color por defecto
                        MainSwing.selectedColorButtonForAlertConfigColor.setBackground(Color.GRAY);
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

            /* 
            // Validar si el valor de 'alert.shape' está entre las opciones válidas
            if (Arrays.asList(shapes).contains(alertShapeFromProperties)) 
            {
                shapeComboBox.setSelectedItem(alertShapeFromProperties);
                shapeComboBox.setEnabled(true);
            } 
            else 
            {
                // Mostrar mensaje al usuario si el valor no es válido
                JOptionPane.showMessageDialog(null,
                    "El valor configurado para 'alert.shape' es inválido o no permitido: '" + alertShapeFromProperties + "'.\n" +
                    "Por favor, asegúrese de que el valor esté entre los siguientes: " + String.join(", ", shapes) + ".\n" +
                    "Se usará el valor predeterminado: Círculo.",
                    "Advertencia de Configuración",
                    JOptionPane.WARNING_MESSAGE);

                // Usar un valor por defecto si el valor de la propiedad no es válido
                shapeComboBox.setSelectedItem("Circulo");
            }

            */
            // Color
            JLabel colorLabel = new JLabel("Color:");


            //obtener color desde el map de basado en las severidades
            //Color getColorFromSeverity = severityColorMap.get(severityComboBox.getSelectedItem());


            //selectedColorButtonForAlertConfigColor.setBackground(getColorFromSeverity);

            // Usar un arreglo para almacenar severityColor

            
            final Color[] severityColor = new Color[1];    

            MainSwing.selectedColorButtonForAlertConfigColor.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                                    // Ejemplo: Actualizar un color basado en la severidad

                                    /* 
                                    switch (selectedSeverity[0]) 
                                    {
                                        case "Alta":
                                        severityColor[0] = coloresPorSeveridadMap.get("Alta");
                                            break;
                                        case "Media":
                                        severityColor[0] = coloresPorSeveridadMap.get("Media");
                                        break;
                                        case "Baja":
                                        severityColor[0] = coloresPorSeveridadMap.get("Baja");
                                        break;
                                        case "Información":
                                            System.out.println("Paso por informacion");
                                            break;
                                        default:
                                        
                                        severityColor[0] = Color.GRAY; // Por defecto
                                            break;
                                    }
                                    */
                                    // Cambiar el fondo del JFrame como ejemplo visual
                                    //frame.getContentPane().setBackground(severityColor)

                    String severidadActual = selectedSeverity[0];

                    // Llama al modal del selector de color para las alertas
                    Color selectedColor = ModificadoresInterfaz.showColorPickerModalForAlerts(owner);

                    // Si se selecciona un color (no se cierra el modal sin elegir)
                    if (selectedColor != null) {
                        // Cambiar el color de fondo del botón al color seleccionado
                        MainSwing.selectedColorButtonForAlertConfigColor.setBackground(selectedColor);
                        MainSwing.coloresPorSeveridadMap.put(severidadActual, selectedColor);

                        // Opcional: si quieres guardar este color en alguna variable de configuración
                        MainSwing.alertasConfig.setColor(selectedColor);
                    }
                }
            });

            
            // Obtener el color de la configuración
            try 
            {

                MainSwing.selectedColorButtonForAlertConfigColor.setBackground(MainSwing.alertasConfig.getColor());
                MainSwing.selectedColorButtonForAlertConfigColor.setEnabled(true);
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
                MainSwing.selectedColorButtonForAlertConfigColor.setBackground(Color.LIGHT_GRAY);
            }

            // Panel para el botón Guardar
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Guardar");

            // Acción del botón Guardar
            saveButton.addActionListener(e -> {
                // Guardar los valores seleccionados en AlertasConfig
                MainSwing.alertasConfig.setTipoAlerta((String) alertTypeComboBox.getSelectedItem());
                MainSwing.alertasConfig.setSeveridad((String) severityComboBox.getSelectedItem());
                MainSwing.alertasConfig.setForma((String) shapeComboBox.getSelectedItem());
                MainSwing.alertasConfig.setColor(MainSwing.selectedColorButtonForAlertConfigColor.getBackground());

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
            configPanel.add(MainSwing.selectedColorButtonForAlertConfigColor);

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


        public static Color showColorPickerModalForAlerts(JFrame owner) 
        {
            JDialog colorDialog = new JDialog(owner, "Seleccione un color", true);
            colorDialog.setSize(600, 400);
            colorDialog.setLayout(new BorderLayout());
    
            JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
            colorButtonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
            String[] colors = { "#FF0000", "#00FF00", "#0000FF", "#00FFFF", "#FF00FF", "#FFFF00",
                    "#000000", "#FFFFFF", "#808080", "#FFA500", "#800080", "#FFC0CB" };
    
            final Color[] selectedColor = { null }; // Array para almacenar el color seleccionado
    
            for (String color : colors) 
            {
                JButton colorButton = new JButton();
                colorButton.setBackground(Color.decode(color));
                colorButton.setPreferredSize(new Dimension(50, 50));
    
                // Evento al hacer clic en un color
                colorButton.addActionListener(e -> {
                    selectedColor[0] = Color.decode(color); // Almacenar el color seleccionado
                    colorDialog.dispose(); // Cerrar el modal una vez seleccionado
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


        public static void showColorPickerModal(JFrame owner) 
        {
            JDialog colorDialog = new JDialog(owner, "Seleccione un colorPOOO", true);
            colorDialog.setSize(600, 400);
            colorDialog.setLayout(new BorderLayout());
    
            JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
            colorButtonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
            String[] colors = { "#FF0000", "#00FF00", "#0000FF", "#00FFFF", "#FF00FF", "#FFFF00",
                    "#000000", "#FFFFFF", "#808080", "#FFA500", "#800080", "#FFC0CB" };
    
            for (String color : colors) 
            {
                JButton colorButton = ModificadoresInterfaz.createColorButton(color);
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

        public static void addSpecificSectionsFromMap(int[] sectionsToAdd) 
        {
            for (int index : sectionsToAdd) {
                JPanel sectionPanel = MainSwing.allSections.get(index);
                if (sectionPanel != null && sectionPanel.getParent() == null) {
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
}
