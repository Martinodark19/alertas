package com.example.alertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.FigurasAlert;
import com.example.header.AlertasConfig;
import com.example.header.Configuracion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

public class MainSwing {
    private JPanel selectedSection;
    private JLabel selectedSectionLabel; // Para cambiar el título de la sección
    private JButton selectedColorButton; // Para actualizar el color del botón en el diálogo de Configurar Alerta
    private Object[] lastAlert = new Object[30]; // Inicializada con un array de 30 elementos
    private DefaultTableModel alertTableModel; // Modelo de la tabla

    private DatabaseConnection databaseConnection;

    // instancia para almacenar configurar alerta del header
    AlertasConfig alertaConfig = new AlertasConfig();

    // instancia para almacenar configuracion del header
    Configuracion configuracion = new Configuracion();

    // instancia de las figuras
    FigurasAlert figurasAlert = new FigurasAlert();

    // instancia para guardar

    public MainSwing(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public static void main(String[] args) {
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

        // Configuración de las secciones para que ocupen menos espacio en la pantalla
        JPanel sectionsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        sectionsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        for (int i = 1; i <= 8; i++) {
            JPanel sectionPanel = new JPanel(new BorderLayout());
            sectionPanel.setBackground(Color.decode("#cccccc"));
            sectionPanel.setBorder(new EmptyBorder(7, 7, 7, 7)); // Reducimos los bordes internos

            // Contenido de la sección
            JLabel sectionLabel = new JLabel("Section " + i, SwingConstants.CENTER);
            sectionLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducimos la fuente

            JLabel alertLabel = new JLabel("Alert " + (char) (64 + i), SwingConstants.CENTER);
            alertLabel.setFont(new Font("Arial", Font.PLAIN, 10)); // Reducimos la fuente

            JPanel labelsPanel = new JPanel(new GridLayout(2, 1));
            labelsPanel.setOpaque(false); // Mantener el fondo de la sección
            labelsPanel.add(sectionLabel);
            labelsPanel.add(alertLabel);

            // Botón de Cambiar Color
            JButton changeColorButton = new JButton("Cambiar Color");
            changeColorButton.setBackground(Color.decode("#009dad"));
            changeColorButton.setForeground(Color.WHITE);
            changeColorButton.setFont(new Font("Arial", Font.PLAIN, 8));
            changeColorButton.setMargin(new Insets(0, 0, 0, 0));
            changeColorButton.setBorderPainted(false);
            changeColorButton.setFocusPainted(false);
            changeColorButton.setPreferredSize(new Dimension(68, 25)); // Reducimos el tamaño del botón

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
            changeTitleButton.setFont(new Font("Arial", Font.PLAIN, 8));
            changeTitleButton.setMargin(new Insets(0, 0, 0, 0));
            changeTitleButton.setBorderPainted(false);
            changeTitleButton.setFocusPainted(false);
            changeTitleButton.setPreferredSize(new Dimension(65, 25)); // Reducimos el tamaño del botón

            changeTitleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedSectionLabel = sectionLabel;
                    showTitleChangeModal(frame);
                }
            });

            // Panel para los botones, utilizando BoxLayout para colocarlos uno encima del
            // otro
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Disposición vertical
            buttonPanel.setOpaque(false); // Mantener el fondo del panel transparente

            // Alinear los botones a la derecha dentro del panel vertical
            buttonPanel.add(changeColorButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre los botones
            buttonPanel.add(changeTitleButton);

            // Crear un panel envolvente que colocará los botones alineados a la derecha
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false); // Mantener el fondo del panel transparente
            wrapperPanel.add(buttonPanel, BorderLayout.EAST); // Colocar los botones a la derecha

            // Añadir el contenido central y el wrapperPanel en la sección
            sectionPanel.add(labelsPanel, BorderLayout.CENTER);
            sectionPanel.add(wrapperPanel, BorderLayout.SOUTH); // Colocar los botones en la parte inferior derecha

            sectionsPanel.add(sectionPanel);

        }

        // Añadir secciones al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.25; // Las secciones ocupan ahora el 25% del espacio vertical
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(sectionsPanel, gbc);

        // Configuración de las tablas para que ocupen más espacio en la pantalla
        JPanel tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Tabla de alertas con scroll horizontal
        String[] alertColumns = {
                "Alerta ID", // alertaid
                "Código Alerta", // codalerta
                "Nombre", // nombre
                "Sentencia ID", // sentenciaId
                "Inicio del Evento", // inicioevento
                "Identificación Alerta", // identificacionalerta
                "Nombre Activo", // nombreActivo
                "Proceso", // proceso
                "Latencia", // latencia
                "Tipo de Servicio", // tipoServicio
                "CI", // CI
                "Subtipo Servicio", // Subtiposervicio
                "Jitter", // jitter
                "Disponibilidad", // disponibilidad
                "Packet Lost", // packetlost
                "RSSI", // rssi
                "NSR", // nsr
                "PLM", // PLM
                "Tipo ExWa", // tipoExWa
                "Código Evento", // codigoEvento
                "Descripción Evento", // descripcionevento
                "Origen", // Origen
                "Tipo Documento", // tipodocumento
                "Estado", // estado
                "Resumen", // resumen
                "Título", // titulo
                "Número", // numero
                "Fecha Estado", // fechaestado
                "Razón Estado" // razonestado
        };

        alertTableModel = new DefaultTableModel(alertColumns, 0);
        JTable alertTable = new JTable(alertTableModel);
        JScrollPane alertScrollPane = new JScrollPane(alertTable);

        // aqui ira la llamada a la base de datos para la tabla alertas

        // Llenar la tabla con los valores iniciales
        alertTableModel.addRow(lastAlert);

        // Crear una instancia del Timer
        Timer timer = new Timer(2000, new ActionListener() {
            private int lastProcessedId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object[]> newAlerts = databaseConnection.fetchAlertsAfterId(lastProcessedId);

                if (!newAlerts.isEmpty()) {
                    for (Object[] alert : newAlerts) {
                        alertTableModel.insertRow(0, alert); // Inserta en la primera posición

                        // logica para cambiar el color de la alerta segun su configuracion
                        // figurasAlert.get
                        // alertaConfig.getColor()

                    }

                    // Actualizar el último alertaId procesado
                    lastProcessedId = (int) newAlerts.get(newAlerts.size() - 1)[0];

                    alertTableModel.fireTableDataChanged();
                }
            }
        });

        // Iniciar el temporizador
        timer.start();

        // Habilitar el scroll horizontal
        alertScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        alertTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Fijar el tamaño inicial de las columnas que se mostrarán al principio
        alertTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        alertTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Tipo de Ventana
        alertTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Predicción
        alertTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Evento

        JPanel alertTablePanel = new JPanel(new BorderLayout());
        alertTablePanel.add(new JLabel("Alertas", JLabel.CENTER), BorderLayout.NORTH);
        alertTablePanel.add(alertScrollPane, BorderLayout.CENTER);

        // Tabla de eventos anteriores con scroll horizontal
        String[] eventColumns = {
                "#", "First", "Last", "Handle", "Evento", "Descripción", "Fecha", "Estado"
        };
        Object[][] eventData = {
                { 1, "Mark", "Otto", "@mdo", "Evento X", "Descripción Evento X", "2024-08-23", "Activo" },
                { 2, "Jacob", "Thornton", "@fat", "Evento Y", "Descripción Evento Y", "2024-08-22", "Inactivo" }
        };
        JTable previousEventTable = new JTable(new DefaultTableModel(eventData, eventColumns));
        JScrollPane previousEventScrollPane = new JScrollPane(previousEventTable);

        // Habilitar el scroll horizontal
        previousEventScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        previousEventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Fijar el tamaño inicial de las columnas que se mostrarán al principio
        previousEventTable.getColumnModel().getColumn(0).setPreferredWidth(50); // #
        previousEventTable.getColumnModel().getColumn(1).setPreferredWidth(100); // First
        previousEventTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Last
        previousEventTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Handle

        JPanel previousEventTablePanel = new JPanel(new BorderLayout());
        previousEventTablePanel.add(new JLabel("Eventos Anteriores", JLabel.CENTER), BorderLayout.NORTH);
        previousEventTablePanel.add(previousEventScrollPane, BorderLayout.CENTER);

        // Tabla de eventos siguientes con scroll horizontal
        JTable nextEventTable = new JTable(new DefaultTableModel(eventData, eventColumns));
        JScrollPane nextEventScrollPane = new JScrollPane(nextEventTable);

        // Habilitar el scroll horizontal
        nextEventScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        nextEventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Fijar el tamaño inicial de las columnas que se mostrarán al principio
        nextEventTable.getColumnModel().getColumn(0).setPreferredWidth(50); // #
        nextEventTable.getColumnModel().getColumn(1).setPreferredWidth(100); // First
        nextEventTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Last
        nextEventTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Handle

        JPanel nextEventTablePanel = new JPanel(new BorderLayout());
        nextEventTablePanel.add(new JLabel("Eventos Siguientes", JLabel.CENTER), BorderLayout.NORTH);
        nextEventTablePanel.add(nextEventScrollPane, BorderLayout.CENTER);

        // Añadir tablas al panel principal
        tablesPanel.add(alertTablePanel);
        tablesPanel.add(previousEventTablePanel);
        tablesPanel.add(nextEventTablePanel);

        // Añadir tablas al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.75; // Las tablas ahora ocupan el 75% del espacio vertical
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
        msField.setText(Integer.toString(configuracion.getUpdateFrequency()));

        // Ajustar el tamaño visible del campo de entrada
        // msField.setColumns(4); // Ajustar para que solo se puedan ver 4 dígitos como
        // máximo

        // Establecer el tamaño preferido para hacerlo más pequeño
        // msField.setPreferredSize(new Dimension(50, 20)); // Ancho 50 píxeles, Alto 20
        // píxeles

        JButton applyButton = new JButton("Guardar");

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // obtener milisegundos limpio
                Number value = (Number) msField.getValue();

                if (value != null) {
                    int updateFrequency = value.intValue();

                    configuracion.setUpdateFrequency(updateFrequency);
                    configDialog.dispose(); // Cierra el diálogo después de guardar

                    // Mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(owner, "Configuración guardada con éxito.");

                } else {
                    configDialog.dispose(); // Cierra el diálogo después de guardar un valor vacio

                    JOptionPane.showMessageDialog(owner,
                            "El campo no puede quedar vacío. Por favor, reintente con un número válido.");

                }

            }
        });

        // obtener los getters para guardar configuraciones en memoria
        // configuracion.getUpdateFrequency();

        // Añadir los componentes al panel de configuración
        configPanel.add(msLabel);
        configPanel.add(msField);

        // Añadir el panel de configuración y el botón de guardar al diálogo
        configDialog.add(configPanel, BorderLayout.CENTER);
        configDialog.add(applyButton, BorderLayout.SOUTH);

        configDialog.setLocationRelativeTo(owner); // Centrar el diálogo
        configDialog.setVisible(true);
    }

    // Método para mostrar el diálogo de configuración de alerta
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

                alertaConfig.setTipoAlerta(tipoAlerta); // Establecer el tipo de alerta
                alertaConfig.setSeveridad(severidad); // Establecer la severidad
                alertaConfig.setForma(forma); // Establecer la forma
                alertaConfig.setColor(color); // Establecer el color

                // alertConfigMap.put(tipoAlerta, alertaConfig);

                alertDialog.dispose(); // Cierra el diálogo después de guardar

                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(owner, "Configuración guardada con éxito.");
                // alertConfigMap.put(tipoAlerta, saveInstanceAlerts); // Almacenar en memoria

            }
        });

        // String tipoAlertaSeleccionada = (String) alertTypeComboBox.getSelectedItem();

        alertTypeComboBox.setSelectedItem(alertaConfig.getTipoAlerta());
        severityComboBox.setSelectedItem(alertaConfig.getSeveridad());
        shapeComboBox.setSelectedItem(alertaConfig.getForma());
        selectedColorButton.setBackground(alertaConfig.getColor());

        // Lógica de verificación para configurar la figura basada en las
        // configuraciones de las alertas

        // private variableParaGuardarFigura;
        if ("Círculo".equals(alertaConfig.getForma())) {
            figurasAlert.setColor(alertaConfig.getColor());
            System.out.println("La forma es un Círculo.");
        } else if (alertaConfig.getForma() == "Triángulo") {
            System.out.println("La forma es un Triángulo.");
        } else if (alertaConfig.getForma() == "Cuadrado") {
            System.out.println("La forma es un Cuadrado.");
        }

        // Agregar componentes al panel de configuración
        configPanel.add(alertTypeLabel);
        configPanel.add(alertTypeComboBox);
        configPanel.add(severityLabel);
        configPanel.add(severityComboBox);
        configPanel.add(shapeLabel);
        configPanel.add(shapeComboBox);
        configPanel.add(colorLabel);
        configPanel.add(selectedColorButton);

        alertDialog.add(configPanel, BorderLayout.CENTER);
        alertDialog.add(saveButton, BorderLayout.SOUTH);

        alertDialog.setLocationRelativeTo(owner);
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
