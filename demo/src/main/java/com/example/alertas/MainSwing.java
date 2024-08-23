package com.example.alertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class MainSwing 
{
    private JPanel selectedSection;
    private JLabel selectedSectionLabel; // Para cambiar el título de la sección
    private Map<Integer, AlertaConfig> alertaConfigs = new HashMap<>(); // Mapa para almacenar configuraciones de alertas
    private JButton selectedColorButton; // Para actualizar el color del botón en el diálogo de Configurar Alerta

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(MainSwing::new);
    }

    public MainSwing() {
        JFrame frame = new JFrame("Mi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa

        // Crear el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header con título y botones
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        header.setBackground(Color.decode("#161618"));

        JLabel titleLabel = new JLabel("Mi App");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton configureWindowButton = new JButton("Configuracion");
        JButton configureAlertButton = new JButton("Configurar Alerta");

        header.add(titleLabel);
        header.add(configureWindowButton);
        header.add(configureAlertButton);

        // Panel para secciones y tablas, usando GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Configuración de las secciones para que ocupen una parte menor de la pantalla
        JPanel sectionsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        sectionsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        for (int i = 1; i <= 8; i++) {
            JPanel sectionPanel = new JPanel(new BorderLayout());
            sectionPanel.setBackground(Color.decode("#cccccc"));
            sectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espaciado interno

            // Contenido de la sección
            JLabel sectionLabel = new JLabel("Section " + i, SwingConstants.CENTER);
            sectionLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Letra más pequeña

            JLabel alertLabel = new JLabel("Alert " + (char) (64 + i), SwingConstants.CENTER);
            alertLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Letra más pequeña

            JPanel labelsPanel = new JPanel(new GridLayout(2, 1));
            labelsPanel.setOpaque(false); // Mantener el fondo de la sección
            labelsPanel.add(sectionLabel);
            labelsPanel.add(alertLabel);

            // Botón de Cambiar Color
            JButton changeColorButton = new JButton("Cambiar Color");
            changeColorButton.setBackground(Color.decode("#009dad"));
            changeColorButton.setForeground(Color.WHITE);
            changeColorButton.setFont(new Font("Arial", Font.PLAIN, 12));
            changeColorButton.setMargin(new Insets(0, 0, 0, 0));
            changeColorButton.setBorderPainted(false);
            changeColorButton.setFocusPainted(false);
            changeColorButton.setPreferredSize(new Dimension(120, 30));

            changeColorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedSection = sectionPanel;
                    showColorPickerModal(frame);
                }
            });

            // Botón de Cambiar Título
            JButton changeTitleButton = new JButton("Cambiar Título");
            changeTitleButton.setBackground(Color.decode("#f39c12"));
            changeTitleButton.setForeground(Color.WHITE);
            changeTitleButton.setFont(new Font("Arial", Font.PLAIN, 12));
            changeTitleButton.setMargin(new Insets(0, 0, 0, 0));
            changeTitleButton.setBorderPainted(false);
            changeTitleButton.setFocusPainted(false);
            changeTitleButton.setPreferredSize(new Dimension(120, 30));

            changeTitleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedSectionLabel = sectionLabel;
                    showTitleChangeModal(frame);
                }
            });

            // Panel para los botones
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false); // Mantener el fondo del panel transparente
            buttonPanel.add(changeColorButton);
            buttonPanel.add(changeTitleButton);

            // Añadir el contenido y el botón al panel de la sección
            sectionPanel.add(labelsPanel, BorderLayout.CENTER);
            sectionPanel.add(buttonPanel, BorderLayout.SOUTH);

            sectionsPanel.add(sectionPanel);
        }

        // Añadir secciones al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.4; // Las secciones ocupan el 40% del espacio vertical
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(sectionsPanel, gbc);

        // Configuración de las tablas para que ocupen más espacio en la pantalla
        JPanel tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Tabla de alertas
        String[] alertColumns = { "ID", "Tipo de Ventana", "Predicción", "Evento", "Causa" };
        Object[][] alertData = {
                { 1, "Ventana A", "Alta", "Evento X", "Causa 1" },
                { 2, "Ventana B", "Media", "Evento Y", "Causa 2" }
        };
        JTable alertTable = new JTable(new DefaultTableModel(alertData, alertColumns));
        JScrollPane alertScrollPane = new JScrollPane(alertTable);
        JPanel alertTablePanel = new JPanel(new BorderLayout());
        alertTablePanel.add(new JLabel("Alertas", JLabel.CENTER), BorderLayout.NORTH);
        alertTablePanel.add(alertScrollPane, BorderLayout.CENTER);

        // Tabla de eventos anteriores
        String[] eventColumns = { "#", "First", "Last", "Handle" };
        Object[][] eventData = {
                { 1, "Mark", "Otto", "@mdo" },
                { 2, "Jacob", "Thornton", "@fat" }
        };
        JTable previousEventTable = new JTable(new DefaultTableModel(eventData, eventColumns));
        JScrollPane previousEventScrollPane = new JScrollPane(previousEventTable);
        JPanel previousEventTablePanel = new JPanel(new BorderLayout());
        previousEventTablePanel.add(new JLabel("Eventos Anteriores", JLabel.CENTER), BorderLayout.NORTH);
        previousEventTablePanel.add(previousEventScrollPane, BorderLayout.CENTER);

        // Tabla de eventos siguientes
        JTable nextEventTable = new JTable(new DefaultTableModel(eventData, eventColumns));
        JScrollPane nextEventScrollPane = new JScrollPane(nextEventTable);
        JPanel nextEventTablePanel = new JPanel(new BorderLayout());
        nextEventTablePanel.add(new JLabel("Eventos Siguientes", JLabel.CENTER), BorderLayout.NORTH);
        nextEventTablePanel.add(nextEventScrollPane, BorderLayout.CENTER);

        tablesPanel.add(alertTablePanel);
        tablesPanel.add(previousEventTablePanel);
        tablesPanel.add(nextEventTablePanel);

        // Añadir tablas al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.6; // Las tablas ocupan el 60% del espacio vertical
        contentPanel.add(tablesPanel, gbc);

        // Añadir el panel principal y el contenido al frame
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Evento para abrir el diálogo de configuración de alerta
        configureAlertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlertConfigDialog(frame);
            }
        });

        // Evento para abrir el diálogo de configuración
        configureWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfigDialog(frame);
            }
        });
    }

    // Método para mostrar el diálogo de configuración de milisegundos
    private void showConfigDialog(JFrame owner) {
        JDialog configDialog = new JDialog(owner, "Configuración", true);
        configDialog.setSize(300, 200);
        configDialog.setLayout(new BorderLayout());

        // Panel para la configuración
        JPanel configPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        configPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel msLabel = new JLabel("Frecuencia de actualización (ms):");

        // Usar un JFormattedTextField para asegurarse de que solo se ingresen números
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        JFormattedTextField msField = new JFormattedTextField(numberFormat);
        msField.setValue(1000); // Valor predeterminado
        msField.setColumns(10);

        JButton applyButton = new JButton("Guardar");

        // Añadir los componentes al panel de configuración
        configPanel.add(msLabel);
        configPanel.add(msField);

        // Añadir el panel de configuración y el botón de guardar al diálogo
        configDialog.add(configPanel, BorderLayout.CENTER);
        configDialog.add(applyButton, BorderLayout.SOUTH);

        configDialog.setLocationRelativeTo(owner);  // Centrar el diálogo
        configDialog.setVisible(true);
    }

    // Método para mostrar el diálogo de configuración de alerta (dejado como estaba)
    private void showAlertConfigDialog(JFrame owner) {
        JDialog alertDialog = new JDialog(owner, "Configurar Alerta", true);
        alertDialog.setSize(400, 300);
        alertDialog.setLayout(new BorderLayout());

        // Panel principal del diálogo
        JPanel configPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tipo de alerta
        JLabel alertTypeLabel = new JLabel("Tipo de Alerta:");
        String[] alertTypes = { "1", "2", "3", "4" };
        JComboBox<String> alertTypeComboBox = new JComboBox<>(alertTypes);

        // Severidad
        JLabel severityLabel = new JLabel("Severidad:");
        String[] severities = { "Alta", "Media", "Baja" };
        JComboBox<String> severityComboBox = new JComboBox<>(severities);

        // Forma
        JLabel shapeLabel = new JLabel("Forma:");
        String[] shapes = { "Círculo", "Triángulo", "Cuadrado" };
        JComboBox<String> shapeComboBox = new JComboBox<>(shapes);

        // Color
        JLabel colorLabel = new JLabel("Color:");
        selectedColorButton = new JButton("Seleccionar Color");
        selectedColorButton.setBackground(Color.LIGHT_GRAY);

        selectedColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Usar el modal que ya tenemos para la selección de color
                showColorPickerModal(owner);
            }
        });

        // Botón para guardar
        JButton saveButton = new JButton("Guardar");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoAlerta = (String) alertTypeComboBox.getSelectedItem();
                String severidad = (String) severityComboBox.getSelectedItem();
                String forma = (String) shapeComboBox.getSelectedItem();
                Color color = selectedColorButton.getBackground();

                // Aquí puedes guardar las configuraciones en una estructura de datos o
                // aplicarlas directamente
                int tipoAlertaInt = Integer.parseInt(tipoAlerta);
                alertaConfigs.put(tipoAlertaInt, new AlertaConfig(tipoAlertaInt, severidad, forma, color));

                alertDialog.dispose(); // Cierra el diálogo después de guardar
            }
        });

        // Agregar componentes al panel de configuración
        configPanel.add(alertTypeLabel);
        configPanel.add(alertTypeComboBox);
        configPanel.add(severityLabel);
        configPanel.add(severityComboBox);
        configPanel.add(shapeLabel);
        configPanel.add(shapeComboBox);
        configPanel.add(colorLabel);
        configPanel.add(selectedColorButton);

        // Agregar el panel de configuración y el botón de guardar al diálogo
        alertDialog.add(configPanel, BorderLayout.CENTER);
        alertDialog.add(saveButton, BorderLayout.SOUTH);

        alertDialog.setLocationRelativeTo(owner); // Centrar el diálogo
        alertDialog.setVisible(true);
    }

    // Método para mostrar el modal del selector de color
    private void showColorPickerModal(JFrame owner) {
        JDialog colorDialog = new JDialog(owner, "Seleccione un color", true);
        colorDialog.setSize(600, 400);
        colorDialog.setLayout(new BorderLayout());

        JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        colorButtonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] colors = { "#FF0000", "#00FF00", "#0000FF", "#00FFFF", "#FF00FF", "#FFFF00",
                "#000000", "#FFFFFF", "#808080", "#FFA500", "#800080", "#FFC0CB" };

        for (String color : colors) {
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

    // Método para mostrar el modal de cambio de título
    private void showTitleChangeModal(JFrame owner) {
        JDialog titleDialog = new JDialog(owner, "Cambiar Título", true);
        titleDialog.setSize(400, 200);
        titleDialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField titleField = new JTextField();
        inputPanel.add(titleField);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> {
            String newTitle = titleField.getText();
            if (!newTitle.isEmpty()) {
                selectedSectionLabel.setText(newTitle); // Cambiar el título de la sección
            }
            titleDialog.dispose();
        });

        inputPanel.add(closeButton);

        titleDialog.add(inputPanel, BorderLayout.CENTER);

        titleDialog.setLocationRelativeTo(owner);
        titleDialog.setVisible(true);
    }

    private JButton createColorButton(String color) {
        JButton button = new JButton();
        button.setBackground(Color.decode(color));
        button.setPreferredSize(new Dimension(50, 50));
        button.addActionListener(e -> {
            if (selectedSection != null) {
                selectedSection.setBackground(Color.decode(color));
            } else if (selectedColorButton != null) {
                selectedColorButton.setBackground(Color.decode(color));
            }
        });
        return button;
    }
}

// Clase para almacenar la configuración de cada alerta
class AlertaConfig {
    int tipoAlerta;
    String severidad;
    String forma;
    Color color;

    public AlertaConfig(int tipoAlerta, String severidad, String forma, Color color) {
        this.tipoAlerta = tipoAlerta;
        this.severidad = severidad;
        this.forma = forma;
        this.color = color;
    }

    // Getters y Setters si es necesario
}
