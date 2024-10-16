package com.example.alertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.FigurasDivididas;
import com.example.header.AlertasConfig;
import com.example.header.Configuracion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainSwing 
{
    private JPanel selectedSection;
    private JLabel selectedSectionLabel; // Para cambiar el título de la sección
    JPanel sectionsPanel;

    private JButton selectedColorButton; // Para actualizar el color del botón en el diálogo de Configurar Alerta
    private JButton selectedColorButtonForAlertConfigColor; // Para actualizar el color del botón en el diálogo de
                                                            // Configurar Alerta
    private Object[] lastAlert = new Object[30]; // Inicializada con un array de 30 elementos

    // variable que contendra la informacion de la ultima alerta ingresada para el
    // pop-up
    private Object[] lastAlertForPopup;

    Object[] alert; // Inicializa la variable `alert` con datos válidos
    private DefaultTableModel alertTableModel; // Modelo de la tabla

    // inicializacion de variable para ocultar las 3 tablas
    JPanel tablesPanel;

    private Boolean showAlertsToSection = false;
    // inicializacion de paneles
    private JPanel figuresPanel;
    private JPanel figuresPanelLeft;
    private JPanel labelsPanel;

    // timer para la actualizacion de la tabla basada en los MS
    private Timer timer;

    private JFrame frame;

    // variable encargada de la logica para tomar decision en el timmer
    private Boolean verifySaveMs = false;

    // variable contendra los ms para timming
    private Integer timeForTimmerUpdated;

    private DatabaseConnection databaseConnection;

    // instancia para almacenar configurar alerta del header
    AlertasConfig alertaConfig = new AlertasConfig();

    // instancia para almacenar configuracion del header
    Configuracion configuracion = new Configuracion();

    // checkbox del boton de Activado y desactivdo de pop-up
    JCheckBox popupCheckBox = new JCheckBox("Mostrar/Ocultar", configuracion.isShowPopup()); // Inicializa marcado
    // checkbox del boton de Activado y desactivdo de visualizar tablas
    JCheckBox hideTableCheckBox = new JCheckBox("Mostrar/Ocultar", configuracion.isHideTable());

    // variable que contendra la frecuencia en ms
    int updateFrequency = configuracion.getUpdateFrequency();

    // instancia de las figuras
    FigurasDivididas figurasDivididas = new FigurasDivididas();

    public MainSwing(DatabaseConnection databaseConnection) 
    {
        this.databaseConnection = databaseConnection;
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(MainSwing::new);
    }

    // Método para configurar y dibujar la figura según la configuración de la
    // alerta

    public MainSwing() 
    {

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
        sectionsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        sectionsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        for (int i = 1; i <= 8; i++) {
            JPanel sectionPanel = new JPanel(new BorderLayout());
            sectionPanel.setBackground(Color.decode("#cccccc"));
            sectionPanel.setBorder(new EmptyBorder(7, 7, 7, 7)); // Reducimos los bordes internos
            sectionPanel.setName("Section-" + i);

            // Contenido de la sección
            JLabel sectionLabel = new JLabel("Section " + i, SwingConstants.CENTER);
            sectionLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducimos la fuente

            // Panel para la figura
            labelsPanel = new JPanel(new GridLayout(2, 1));
            labelsPanel.setOpaque(false); // Mantener el fondo de la sección
            labelsPanel.add(sectionLabel);

            // Panel dedicado a las figuras, separado del contenido principal
            figuresPanel = new JPanel(); // Empezamos con las figuras a la derecha
            figuresPanel.setOpaque(false); // Para mantener el fondo del panel principal
            figuresPanel.setLayout(new BoxLayout(figuresPanel, BoxLayout.Y_AXIS));
            figuresPanel.setLayout(new BoxLayout(figuresPanel, BoxLayout.X_AXIS)); // Alinear las figuras
                                                                                   // horizontalmente
            figuresPanel.setMinimumSize(new Dimension(36, 36)); // Tamaño mínimo de las figuras
            figuresPanel.setMaximumSize(new Dimension(36, 36)); // Tamaño máximo de las figuras

            // Panel dedicado a las figuras, separado del contenido principal
            figuresPanelLeft = new JPanel(); // Empezamos con las figuras a la derecha
            figuresPanelLeft.setOpaque(false); // Para mantener el fondo del panel principal
            figuresPanelLeft.add(figuresPanel);
            figuresPanelLeft.setLayout(new BoxLayout(figuresPanelLeft, BoxLayout.Y_AXIS));
            figuresPanelLeft.setLayout(new BoxLayout(figuresPanelLeft, BoxLayout.X_AXIS)); // Alinear las figuras
                                                                                           // horizontalmente
            figuresPanelLeft.setMinimumSize(new Dimension(39, 39)); // Tamaño mínimo de las figuras
            figuresPanelLeft.setMaximumSize(new Dimension(39, 39)); // Tamaño máximo de las figuras

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
            sectionPanel.add(figuresPanel, BorderLayout.EAST);
            sectionPanel.add(figuresPanelLeft, BorderLayout.WEST);

            sectionsPanel.add(sectionPanel);
        }

        // Actualizar el panel para mostrar la nueva configuración
        sectionsPanel.revalidate();
        sectionsPanel.repaint();

        // Añadir secciones al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.25; // Las secciones ocupan ahora el 25% del espacio vertical
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(sectionsPanel, gbc);

        // Configuración de las tablas para que ocupen más espacio en la pantalla
        tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Tabla de alertas con scroll horizontal
        String[] alertColumns = 
        {
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

        // Llenar la tabla con los valores iniciales
        alertTableModel.addRow(lastAlert);

        timeForTimmerUpdated = configuracion.getUpdateFrequency();

        // Crear un nuevo Timer con la nueva frecuencia
        Timer timerForUpdateConfigMs = new Timer(1000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();

                if (verifySaveMs == true) {
                    timeForTimmerUpdated = updateFrequency;

                    if (timer.isRunning() == true) {
                        timer.stop();
                        timer.setDelay(timeForTimmerUpdated);
                        timer.start();
                    }

                }
                verifySaveMs = false;

            }
        });

        // Iniciar el Timer
        timerForUpdateConfigMs.start();

        // timer para ejecutar las consultas esporadicas a la base de datos
        timer = new Timer(timeForTimmerUpdated, new ActionListener() 
        {
            private int lastProcessedId = 0;

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                List<Object[]> newAlerts = databaseConnection.fetchAlertsAfterId(lastProcessedId);
            
                // Convert List<Object[]> to Object[][]
                Object[][] alertsArray = new Object[newAlerts.size()][];
                alertsArray = newAlerts.toArray(alertsArray);

                List<Object[]> alertasConPermisosAd = DatabaseConnection.filtrarAlertasConPermiso(alertsArray);

                // aqui le debo enviar ese array a otro metodo para que nos retorne el array 

                if (!alertasConPermisosAd.isEmpty()) 
                {
                    for (Object[] alert : alertasConPermisosAd) 
                    {
                        alertTableModel.insertRow(0, alert); // Inserta en la primera posición
                        lastAlertForPopup = alert;

                        // Lógica para mostrar la figura en la sección correspondiente
                        showAlertsToSection = true;

                        if (showAlertsToSection) 
                        {
                            // Secciones disponibles
                            int[] seccionesDisponibles = { 1, 3, 5, 7 };
                            // Selección aleatoria de una sección disponible
                            int randomIndex = (int) (Math.random() * seccionesDisponibles.length);
                            int sectionIndex = seccionesDisponibles[randomIndex];

                            // Ahora `sectionIndex` será 1, 3, 5, o 7
                            JPanel sectionPanel = (JPanel) sectionsPanel.getComponent(sectionIndex - 1);

                            // Obtén el `labelsPanel` de esa sección para añadir la figura
                            JPanel labelsPanel = (JPanel) sectionPanel.getComponent(2); // Obtén el primer
                                                                                        // componente que debería
                                                                                        // ser labelsPanel
                                                                                        
                            // Seleccionar la figura basada en la configuración de la alerta
                            JPanel figuraPanel;
                            switch (alertaConfig.getForma()) 
                            {
                                case "Círculo":
                                    figuraPanel = new FigurasDivididas.CirculoPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }
                                    break;
                                case "Cuadrado":
                                    figuraPanel = new FigurasDivididas.CuadradoPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }

                                    break;
                                case "Triángulo":
                                    figuraPanel = new FigurasDivididas.TrianguloPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }
                                    break;
                                default:
                                    figuraPanel = new JPanel(); // En caso de error o forma no reconocida
                                    break;
                            }

                            // Añadir la figura al `labelsPanel` en la sección específica
                            labelsPanel.add(figuraPanel);
                            labelsPanel.revalidate();
                            labelsPanel.repaint();

                            JPanel labelsPanelLeft = (JPanel) sectionPanel.getComponent(3); // Obtén el primer
                                                                                            // componente,

                            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                            executor.schedule(() -> {
                                // Código a ejecutar después del delay
                                labelsPanel.removeAll();
                                labelsPanelLeft.add(figuraPanel);
                                labelsPanelLeft.revalidate();
                                labelsPanelLeft.repaint();
                            }, 2, TimeUnit.SECONDS);
                            executor.shutdown();
                        }
                    }

                    // Actualizar el último alertId procesado
                    lastProcessedId = (int) newAlerts.get(newAlerts.size() - 1)[0];

                    alertTableModel.fireTableDataChanged();
                }
                // este sera el caso de que no tenga los permisos
                else
                {
                    if (!newAlerts.isEmpty()) 
                    {
                        for (Object[] alert : newAlerts) 
                    {
                        alertTableModel.insertRow(0, alert); // Inserta en la primera posición
                        lastAlertForPopup = alert;

                        // Lógica para mostrar la figura en la sección correspondiente
                        showAlertsToSection = true;

                        if (showAlertsToSection) 
                        {
                            // Secciones disponibles
                            int[] seccionesDisponibles = { 1, 3, 5, 7 };
                            // Selección aleatoria de una sección disponible
                            int randomIndex = (int) (Math.random() * seccionesDisponibles.length);
                            int sectionIndex = seccionesDisponibles[randomIndex];

                            // Ahora `sectionIndex` será 1, 3, 5, o 7
                            JPanel sectionPanel = (JPanel) sectionsPanel.getComponent(sectionIndex - 1);

                            // Obtén el `labelsPanel` de esa sección para añadir la figura
                            JPanel labelsPanel = (JPanel) sectionPanel.getComponent(2); // Obtén el primer
                                                                                        // componente que debería
                                                                                        // ser labelsPanel
                                                                                        
                            // Seleccionar la figura basada en la configuración de la alerta
                            JPanel figuraPanel;
                            switch (alertaConfig.getForma()) 
                            {
                                case "Círculo":
                                    figuraPanel = new FigurasDivididas.CirculoPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }
                                    break;
                                case "Cuadrado":
                                    figuraPanel = new FigurasDivididas.CuadradoPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }

                                    break;
                                case "Triángulo":
                                    figuraPanel = new FigurasDivididas.TrianguloPanel(alertaConfig.getColor(),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) {
                                        openPopupWithTable(new Object[][] { alert });
                                    }
                                    break;
                                default:
                                    figuraPanel = new JPanel(); // En caso de error o forma no reconocida
                                    break;
                            }

                            // Añadir la figura al `labelsPanel` en la sección específica
                            labelsPanel.add(figuraPanel);
                            labelsPanel.revalidate();
                            labelsPanel.repaint();

                            JPanel labelsPanelLeft = (JPanel) sectionPanel.getComponent(3); // Obtén el primer
                                                                                            // componente,

                            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                            executor.schedule(() -> {
                                // Código a ejecutar después del delay
                                labelsPanel.removeAll();
                                labelsPanelLeft.add(figuraPanel);
                                labelsPanelLeft.revalidate();
                                labelsPanelLeft.repaint();
                            }, 2, TimeUnit.SECONDS);
                            executor.shutdown();
                        }
                    }

                    // Actualizar el último alertId procesado
                    lastProcessedId = (int) newAlerts.get(newAlerts.size() - 1)[0];

                    alertTableModel.fireTableDataChanged();

                    }
                    
                }
            }
        });

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
        configureAlertButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlertConfigDialog(frame);
            }
        });

        // Evento para abrir el diálogo de configuración
        configureWindowButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfigDialog(frame);
            }
        });
    }

    // Método para mostrar el diálogo de configuración de milisegundos y opción de
    // pop-up
    private void showConfigDialog(JFrame owner) 
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
        msField.setText(Integer.toString(configuracion.getUpdateFrequency()));

        // Nueva opción para "Mostrar pop-up en pantalla"
        JLabel popupLabel = new JLabel("Mostrar pop-up en pantalla:");

        // Nueva opción para "Mostrar pop-up en pantalla"
        JLabel hideTable = new JLabel("Ocultar tabla:");
        hideTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        hideTableCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Nueva opción para "Secciones en pantalla"
        JLabel sectionsLabelConfiguration = new JLabel("Secciones en pantalla:");
        String[] sectionOptions = { "4 secciones", "8 secciones" };
        JComboBox<String> sectionComboBox = new JComboBox<>(sectionOptions);
        sectionComboBox.setSelectedItem(configuracion.getSectionCount());


        // Botón para guardar los cambios
        JButton applyButton = new JButton("Guardar");

        applyButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Obtener el valor de milisegundos limpio y aplicar su logica
                Number value = (Number) msField.getValue();

                if (value != null) 
                {
                    updateFrequency = value.intValue();
                    configuracion.setUpdateFrequency(updateFrequency);
                    configDialog.dispose(); // Cierra el diálogo después de guardar

                    // Mostrar un mensaje de éxito
                    verifySaveMs = true;
                    JOptionPane.showMessageDialog(owner, "Configuración guardada con éxito.");
                } 
                else 
                {
                    configDialog.dispose(); // Cierra el diálogo después de guardar un valor vacío
                    JOptionPane.showMessageDialog(owner, "El campo no puede quedar vacío. Por favor, reintente con un número válido.");
                }
                // Lógica para ocultar tablas y mostrarlas
                if (hideTableCheckBox.isSelected()) 
                {
                    // Oculta la tabla si el JCheckBox está marcado
                    configuracion.setHideTable(true);
                    tablesPanel.setVisible(false);
                }
                else 
                {
                    tablesPanel.setVisible(true);
                }
                if (sectionComboBox.getSelectedItem().equals("4 secciones")) 
                {
                    configuracion.setSectionCount("4 secciones");
                    // Eliminar secciones específicas
                    removeSpecificSections(new int[] { 2, 4, 6, 8 });
                } 
                else 
                {

                    addSpecificSectionsFromMap();
                    sectionsPanel.revalidate();
                    sectionsPanel.repaint();
                    configuracion.setSectionCount("8 secciones");

                }
            }
        });
        // logica para almacenar en memoria ocultar tabla
        configuracion.isHideTable();
        configuracion.getSectionCount();

        // Añadir los componentes al panel
        configPanel.add(msLabel); // Etiqueta de frecuencia en milisegundos
        configPanel.add(msField); // Campo para ingresar los milisegundos
        configPanel.add(popupLabel); // Etiqueta para mostrar pop-up
        configPanel.add(popupCheckBox); // Input para activar/desactivar el pop-up
        configPanel.add(hideTable);
        configPanel.add(hideTableCheckBox);
        configPanel.add(sectionsLabelConfiguration); // Etiqueta para seleccionar el número de secciones
        configPanel.add(sectionComboBox); // ComboBox para seleccionar 4 u 8 secciones

        // Añadir el panel de configuración al diálogo
        configDialog.add(configPanel, BorderLayout.CENTER);
        configDialog.add(applyButton, BorderLayout.SOUTH); // Botón en la parte inferior

        configDialog.setLocationRelativeTo(owner); // Centrar el diálogo
        configDialog.setVisible(true);
    }


// Define un mapa para almacenar las secciones eliminadas
private Map<Integer, JPanel> removedSectionsMap = new LinkedHashMap<>();


// Método modificado para eliminar y almacenar las secciones en orden y su índice
private void removeSpecificSections(int[] sectionsToRemove) 
{
    List<Component> toRemove = new ArrayList<>();
    // Recolectar los paneles a eliminar y almacenarlos en el mapa con su índice
    Component[] components = sectionsPanel.getComponents();
    for (int i = 0; i < components.length; i++) 
    {
        if (components[i] instanceof JPanel) {
            JPanel panel = (JPanel) components[i];
            int sectionNumber = Integer.parseInt(panel.getName().substring(8)); // Obtener el número de sección
            for (int section : sectionsToRemove) {
                if (section == sectionNumber) {
                    toRemove.add(panel);
                    // Almacenar la sección eliminada junto con su índice en el LinkedHashMap
                    removedSectionsMap.put(i, panel);
                    break;
                }
            }
        }
    }

    // Eliminar los paneles recolectados del panel principal
    for (Component comp : toRemove) {
        sectionsPanel.remove(comp);
    }

    // Actualizar el panel después de la eliminación
    sectionsPanel.revalidate();
    sectionsPanel.repaint();
}


// Método para agregar secciones desde el mapa en la posición original
private void addSpecificSectionsFromMap() 
{
    // Recorre las secciones eliminadas en el mapa en el orden de inserción
    for (Map.Entry<Integer, JPanel> entry : removedSectionsMap.entrySet()) 
    {
        int index = entry.getKey();
        JPanel panel = entry.getValue();

        // Inserta el panel en su posición original
        sectionsPanel.add(panel, index);
    }

    // Actualizar el panel después de añadir las secciones
    sectionsPanel.revalidate();
    sectionsPanel.repaint();

    // Limpiar el mapa después de reinsertar las secciones
    removedSectionsMap.clear();
}


    private void openPopupWithTable(Object[][] alertData) 
    {
        JDialog tableDialog = new JDialog((Frame) null, "Detalles de la Alerta", true); // No se pasa el owner
        tableDialog.setSize(600, 400); // Tamaño de la ventana emergente
        tableDialog.setLayout(new BorderLayout());

        // Columnas para la tabla (deberían coincidir con los datos pasados en
        // alertData)
        String[] columnNames = {
                "Alert ID", "Cod Alerta", "Nombre", "Sentencia ID", "Inicio Evento", "Identificación Alerta",
                "Nombre Activo", "Proceso", "Latencia", "Tipo Servicio", "CI", "Subtipo Servicio",
                "Jitter", "Disponibilidad", "Packet Lost", "RSSI", "NSR", "PLM", "Tipo ExWa",
                "Código Evento", "Descripción Evento", "Origen", "Tipo Documento", "Estado",
                "Resumen", "Título", "Número", "Fecha Estado", "Razón Estado"
        };

        // Crear la tabla con los datos y columnas
        JTable table = new JTable(alertData, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Desactiva el ajuste automático del tamaño de columnas
        JScrollPane scrollPane = new JScrollPane(table);

        // Configurar scroll para la tabla
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Añadir la tabla con el JScrollPane al diálogo
        tableDialog.add(scrollPane, BorderLayout.CENTER);

        // Añadir un botón para cerrar el diálogo
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableDialog.dispose();
            }
        });
        tableDialog.add(closeButton, BorderLayout.SOUTH);

        // Mostrar el diálogo
        tableDialog.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        tableDialog.setVisible(true);
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
        selectedColorButtonForAlertConfigColor = new JButton("Seleccionar Color");
        selectedColorButtonForAlertConfigColor.setBackground(Color.LIGHT_GRAY);

        selectedColorButtonForAlertConfigColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llama al modal del selector de color para las alertas
                Color selectedColor = showColorPickerModalForAlerts(owner);

                // Si se selecciona un color (no se cierra el modal sin elegir)
                if (selectedColor != null) {
                    // Cambiar el color de fondo del botón al color seleccionado
                    selectedColorButtonForAlertConfigColor.setBackground(selectedColor);

                    // Opcional: si quieres guardar este color en alguna variable de configuración
                    alertaConfig.setColor(selectedColor);
                }
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
                Color color = selectedColorButtonForAlertConfigColor.getBackground();

                alertaConfig.setTipoAlerta(tipoAlerta); // Establecer el tipo de alerta
                alertaConfig.setSeveridad(severidad); // Establecer la severidad
                alertaConfig.setForma(forma); // Establecer la forma
                alertaConfig.setColor(color); // Establecer el color

            
                alertDialog.dispose(); // Cierra el diálogo después de guardar

                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(owner, "Configuración guardada con éxito.");
            }
        });

        alertTypeComboBox.setSelectedItem(alertaConfig.getTipoAlerta());
        severityComboBox.setSelectedItem(alertaConfig.getSeveridad());
        shapeComboBox.setSelectedItem(alertaConfig.getForma());
        selectedColorButtonForAlertConfigColor.setBackground(alertaConfig.getColor());

        // Agregar componentes al panel de configuración
        configPanel.add(alertTypeLabel);
        configPanel.add(alertTypeComboBox);
        configPanel.add(severityLabel);
        configPanel.add(severityComboBox);
        configPanel.add(shapeLabel);
        configPanel.add(shapeComboBox);
        configPanel.add(colorLabel);
        configPanel.add(selectedColorButtonForAlertConfigColor);

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

    private Color showColorPickerModalForAlerts(JFrame owner) {
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

    // Método para mostrar el modal de cambio de título
    private void showTitleChangeModal(JFrame owner) 
    {
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

}
