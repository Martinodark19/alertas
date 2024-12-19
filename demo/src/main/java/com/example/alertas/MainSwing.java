package com.example.alertas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.FigurasDivididas;
import com.example.configuracion.ConfigProperties;

public class MainSwing 
{
    // map para almacenar las secciones
    private Map<Integer, JPanel> allSections = new HashMap<>();

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

    // checkbox del boton de Activado y desactivdo de pop-up
    //JCheckBox popupCheckBox = new JCheckBox("Mostrar/Ocultar", configuracion.isShowPopup()); // Inicializa marcado
    String showPopupFromProperties = ConfigProperties.getProperty("app.showPopup").trim().toLowerCase();
    String hideTableFromProperties = ConfigProperties.getProperty("app.hideTable").trim().toLowerCase();

    JCheckBox popupCheckBox = new JCheckBox("Mostrar/Ocultar", Boolean.parseBoolean(showPopupFromProperties)); // Inicializa marcado

    // checkbox del boton de Activado y desactivdo de visualizar tablas
    JCheckBox hideTableCheckBox = new JCheckBox("Mostrar/Ocultar", Boolean.parseBoolean(hideTableFromProperties));
    
    // variable que contendra la frecuencia en ms
    Integer updateFrequency = Integer.parseInt(ConfigProperties.getProperty("app.updateFrequency").trim());

    // instancia de las figuras
    FigurasDivididas figurasDivididas = new FigurasDivididas();


    public static void main(String[] args) 
    {
        // Forzar la inicialización de ConfigProperties
        ConfigProperties.getAllProperties();
        SwingUtilities.invokeLater(() -> new MainSwing(new DatabaseConnection()));


    }

    public MainSwing(DatabaseConnection databaseConnection) 
    {

        this.databaseConnection = databaseConnection;

        JFrame frame = new JFrame("Mi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa

        try 
        {
            String dbUrl = DatabaseConnection.getDbUrl();
            Connection connection = DriverManager.getConnection(dbUrl); // Intenta conectar
            System.out.println("Conexión a la base de datos exitosa."); // Mensaje opcional
            connection.close(); // Cierra la conexión si fue exitosa
        } 
        catch (Exception e) 
        {
            // Captura cualquier excepción relacionada con la conexión
            e.printStackTrace(); // Para depuración, imprime el stack trace en la consola
    
            // Muestra un mensaje de error al usuario
            JOptionPane.showMessageDialog(
                frame,
                "No se pudo establecer conexión con la base de datos. El programa se cerrará.",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE
            );
    
            // Cierra el programa con un código de error
            System.exit(0);
        }


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
            figuresPanel = new JPanel();
            figuresPanel.setOpaque(false);
            figuresPanel.setLayout(new BoxLayout(figuresPanel, BoxLayout.X_AXIS));
            figuresPanel.setMinimumSize(new Dimension(36, 36));
            figuresPanel.setMaximumSize(new Dimension(36, 36));

            // Panel para figuras a la izquierda
            figuresPanelLeft = new JPanel();
            figuresPanelLeft.setOpaque(false);
            figuresPanelLeft.add(figuresPanel);
            figuresPanelLeft.setLayout(new BoxLayout(figuresPanelLeft, BoxLayout.X_AXIS));
            figuresPanelLeft.setMinimumSize(new Dimension(39, 39));
            figuresPanelLeft.setMaximumSize(new Dimension(39, 39));

            // Botón de Cambiar Color
            JButton changeColorButton = new JButton("Cambiar Color");
            changeColorButton.setBackground(Color.decode("#009dad"));
            changeColorButton.setForeground(Color.WHITE);
            changeColorButton.setFont(new Font("Arial", Font.PLAIN, 8));
            changeColorButton.setMargin(new Insets(0, 0, 0, 0));
            changeColorButton.setBorderPainted(false);
            changeColorButton.setFocusPainted(false);
            changeColorButton.setPreferredSize(new Dimension(68, 25));

            changeColorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    selectedSection = sectionPanel;
                    //showColorPickerModal(frame);
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
            changeTitleButton.setPreferredSize(new Dimension(65, 25));

            changeTitleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedSectionLabel = sectionLabel;
                    showTitleChangeModal(frame);
                }
            });

            // Panel para los botones
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setOpaque(false);
            buttonPanel.add(changeColorButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            buttonPanel.add(changeTitleButton);

            // Panel envolvente para los botones
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            wrapperPanel.add(buttonPanel, BorderLayout.EAST);

            // Añadir componentes a la sección
            sectionPanel.add(labelsPanel, BorderLayout.CENTER);
            sectionPanel.add(wrapperPanel, BorderLayout.SOUTH);
            sectionPanel.add(figuresPanel, BorderLayout.EAST);
            sectionPanel.add(figuresPanelLeft, BorderLayout.WEST);

            // Añadir la sección al sectionsPanel
            sectionsPanel.add(sectionPanel);

            // Añadir la sección al Map con su índice
            allSections.put(i, sectionPanel);
        }

        String verificarIniciadoSecciones = ConfigProperties.getProperty("app.sections").trim();

        if (!verificarIniciadoSecciones.equals("8")  && !verificarIniciadoSecciones.equals("4")) 
        {
            JOptionPane.showMessageDialog(
                frame, 
                "El valor configurado para 'app.sections' es inválido: '" + verificarIniciadoSecciones + "'.\n" +
                "Por favor, asegúrese de que el valor en el archivo de configuración sea '4' o '8'.\n" +
                "Se usará el valor por defecto: 4 secciones.", 
                "Error en Configuración", 
                JOptionPane.ERROR_MESSAGE
            );
        
            // Asignar un valor por defecto si es necesario
            verificarIniciadoSecciones = "4";
        }

        if (verificarIniciadoSecciones.equals("4")) 
        {
            removeSpecificSections(new int[] {2,4,6,8});
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

        timeForTimmerUpdated = Integer.parseInt(ConfigProperties.getProperty("app.updateFrequency").trim());

        // Crear un nuevo Timer con la nueva frecuencia
        Timer timerForUpdateConfigMs = new Timer(1000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                timer.start();

                if (verifySaveMs == true) {
                    timeForTimmerUpdated = updateFrequency;

                    if (timer.isRunning() == true) 
                    {
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
                            // Array de secciones disponibles
                            int[] seccionesEspecificas  = { 0, 2, 4, 6};

                            Map<Integer, JPanel> seccionesMap = new LinkedHashMap<>();

                            for (int sectionIndex : seccionesEspecificas) 
                            {
                                JPanel sectionPanel = allSections.get(sectionIndex + 1); // Ajuste de índice
                                if (sectionPanel != null && sectionPanel.getParent() != null) 
                                {
                                    seccionesMap.put(sectionIndex, sectionPanel); // Añadir al mapa
                                }
                            }
                            // Obtener un valor aleatorio
                            JPanel randomValue = getRandomValueFromMap(seccionesMap);

                            // Obtén el `labelsPanel` de esa sección para añadir la figura
                            JPanel labelsPanel = (JPanel) randomValue.getComponent(2);
                                                                                                                                              
                            // Seleccionar la figura basada en la configuración de la alerta
                            JPanel figuraPanel;

                            //obtener el color de la configuracion
                            String alertColorFromProperties = ConfigProperties.getProperty("alert.color").trim();
                            Color alertColor = Color.decode(alertColorFromProperties); // Convierte el valor hexadecimal a un objeto Color


                            switch (ConfigProperties.getProperty("alert.shape").toLowerCase()) 
                            {
                                case "circulo":
                                    figuraPanel = new FigurasDivididas.CirculoPanel((alertColor),
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) 
                                    {
                                        openPopupWithTable(new Object[][] { alert });
                                    }
                                    break;
                                case "cuadrado":
                                    figuraPanel = new FigurasDivididas.CuadradoPanel(alertColor,
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) 
                                    {
                                        openPopupWithTable(new Object[][] { alert });
                                    }

                                    break;
                                case "triangulo":
                                    figuraPanel = new FigurasDivididas.TrianguloPanel(alertColor,
                                            new Object[][] { alert });
                                    if (popupCheckBox.isSelected()) 
                                    {
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

                            JPanel labelsPanelLeft = (JPanel) randomValue.getComponent(3); // Obtén el primer
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
                                // Array de secciones disponibles

                                int[] seccionesEspecificas  = { 0, 2, 4, 6};

                                Map<Integer, JPanel> seccionesMap = new LinkedHashMap<>();

                                for (int sectionIndex : seccionesEspecificas) 
                                {
                                    JPanel sectionPanel = allSections.get(sectionIndex + 1); // Ajuste de índice
                                    if (sectionPanel != null && sectionPanel.getParent() != null) 
                                    {
                                        seccionesMap.put(sectionIndex, sectionPanel); // Añadir al mapa
                                    }
                                }

                                // Obtener un valor aleatorio
                                JPanel randomValue = getRandomValueFromMap(seccionesMap);
                                                                
                                // Obtén el `labelsPanel` de esa sección para añadir la figura
                                JPanel labelsPanel = (JPanel) randomValue.getComponent(2); 

                                                
                                                            //obtener el color de la configuracion
                                String alertColorFromProperties = ConfigProperties.getProperty("alert.color").trim();
                                Color alertColor = Color.decode(alertColorFromProperties); // Convierte el valor hexadecimal a un objeto Color


                                //Seleccionar la figura basada en la configuración de la alerta
                                JPanel figuraPanel;
                                        switch (ConfigProperties.getProperty("alert.type").trim().toLowerCase()) 
                                        {
                                            case "circulo":
                                                figuraPanel = new FigurasDivididas.CirculoPanel(alertColor,
                                                        new Object[][] { alert });
                                                if (popupCheckBox.isSelected()) {
                                                    openPopupWithTable(new Object[][] { alert });
                                                }
                                                break;
                                            case "cuadrado":
                                                figuraPanel = new FigurasDivididas.CuadradoPanel(alertColor,
                                                        new Object[][] { alert });
                                                if (popupCheckBox.isSelected()) {
                                                    openPopupWithTable(new Object[][] { alert });
                                                }

                                                break;
                                            case "triangulo":
                                                figuraPanel = new FigurasDivididas.TrianguloPanel(alertColor,
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

                                        JPanel labelsPanelLeft = (JPanel) randomValue.getComponent(3); // Obtén el primer
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


        // Lógica para ocultar tablas y mostrarlas
        if (hideTableCheckBox.isSelected()) 
        {
            // Oculta la tabla si el JCheckBox está marcado
            tablesPanel.setVisible(false);
        }
        else 
        {
            tablesPanel.setVisible(true);
        }

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
        // Cambiar el color del texto a rojo
        msField.setDisabledTextColor(Color.BLUE);
        msField.setEnabled(false);

        msField.setText(ConfigProperties.getProperty("app.updateFrequency").trim());
        // Nueva opción para "Mostrar pop-up en pantalla"
        
        JLabel popupLabel = new JLabel("Mostrar pop-up en pantalla:");
        popupCheckBox.setEnabled(false);

        // Nueva opción para "Mostrar pop-up en pantalla"
        JLabel hideTable = new JLabel("Ocultar tabla:");
        hideTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        hideTableCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Deshabilitar para que no sean clickeables
        hideTableCheckBox.setEnabled(false);


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
            sectionsPanel.revalidate();
            sectionsPanel.repaint();
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
        configPanel.add(popupCheckBox); // Input para activar/desactivar el pop-up
        configPanel.add(hideTable);
        configPanel.add(hideTableCheckBox);
        configPanel.add(sectionsLabelConfiguration); // Etiqueta para seleccionar el número de secciones
        configPanel.add(sectionComboBox); // ComboBox para seleccionar 4 u 8 secciones

        // Añadir el panel de configuración al diálogo
        configDialog.add(configPanel, BorderLayout.CENTER);

        configDialog.setLocationRelativeTo(owner); // Centrar el diálogo
        configDialog.setVisible(true);
    }


// Define un mapa para almacenar las secciones eliminadas
private Map<Integer, JPanel> removedSectionsMap = new LinkedHashMap<>();


private void removeSpecificSections(int[] sectionsToRemove) {
    for (int index : sectionsToRemove) {
        JPanel sectionPanel = allSections.get(index);
        if (sectionPanel != null && sectionPanel.getParent() != null) {
            sectionsPanel.remove(sectionPanel);
        }
    }
    sectionsPanel.revalidate();
    sectionsPanel.repaint();
}

private void addSpecificSectionsFromMap(int[] sectionsToAdd) 
{
    for (int index : sectionsToAdd) {
        JPanel sectionPanel = allSections.get(index);
        if (sectionPanel != null && sectionPanel.getParent() == null) {
            // Agrega la sección en su posición original
            sectionsPanel.add(sectionPanel, index - 1); // Restamos 1 si los índices comienzan en 1
        }
    }
    sectionsPanel.revalidate();
    sectionsPanel.repaint();
}


    private void openPopupWithTable(Object[][] alertData) 
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

    // Método para mostrar el diálogo de configuración de alerta
    private void showAlertConfigDialog(JFrame owner) 
    {
        JDialog alertDialog = new JDialog(owner, "Configurar Alerta", true);
        alertDialog.setSize(400, 300);
        alertDialog.setLayout(new BorderLayout());

        // Panel principal del diálogo
        JPanel configPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String alertTypeFromProperties = ConfigProperties.getProperty("alert.type").trim();
        
        // Tipo de alerta
        JLabel alertTypeLabel = new JLabel("Tipo de Alerta:");
        String[] alertTypes = { "1", "2", "3", "4" };
        JComboBox<String> alertTypeComboBox = new JComboBox<>(alertTypes);
        alertTypeComboBox.setSelectedItem(alertTypeFromProperties);

        // Verificar si el valor de alertTypeFromProperties está en los valores permitidos
        if (Arrays.asList(alertTypes).contains(alertTypeFromProperties)) 
        {
            alertTypeComboBox.setSelectedItem(alertTypeFromProperties);
            // Cambiar el color del texto deshabilitado
            UIManager.put("ComboBox.disabledForeground", new Color(0, 0, 255)); // Azul

            // Actualizar la apariencia del ComboBox
            SwingUtilities.updateComponentTreeUI(alertTypeComboBox);
            alertTypeComboBox.setEnabled(false);
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

        String alertSeverityFromProperties = ConfigProperties.getProperty("alert.severity").trim().toLowerCase();

        // Convertir la primera letra a mayúscula
        if (!alertSeverityFromProperties.isEmpty()) 
        {
            alertSeverityFromProperties = alertSeverityFromProperties.substring(0, 1).toUpperCase() 
            + alertSeverityFromProperties.substring(1).toLowerCase();
        }

        // Severidad
        JLabel severityLabel = new JLabel("Severidad:");
        String[] severities = { "Alta", "Media", "Baja" };
        
        JComboBox<String> severityComboBox = new JComboBox<>(severities);
            
        if (Arrays.asList(severities).contains(alertSeverityFromProperties)) 
        {
            severityComboBox.setSelectedItem(alertSeverityFromProperties);
            severityComboBox.setEnabled(false);
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

        String alertShapeFromProperties = ConfigProperties.getProperty("alert.shape").trim().toLowerCase();

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

        // Validar si el valor de 'alert.shape' está entre las opciones válidas
        if (Arrays.asList(shapes).contains(alertShapeFromProperties)) 
        {
            shapeComboBox.setSelectedItem(alertShapeFromProperties);
            shapeComboBox.setEnabled(false);
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

        // Color
        JLabel colorLabel = new JLabel("Color:");
        selectedColorButtonForAlertConfigColor = new JButton("");
        selectedColorButtonForAlertConfigColor.setBackground(Color.LIGHT_GRAY);

        // Obtén el valor del color desde las propiedades
        String colorFromProperties = ConfigProperties.getProperty("alert.color").trim(); // Valor predeterminado: #CCCCCC
        colorFromProperties = colorFromProperties.replaceAll("\\s+", ""); // Elimina espacios en caso de que existan

        try 
        {
            // Intenta establecer el color del botón
            Color parsedColor = Color.decode(colorFromProperties);
            selectedColorButtonForAlertConfigColor.setBackground(parsedColor);
            selectedColorButtonForAlertConfigColor.setEnabled(false);
        } 
        catch (NumberFormatException e) 
        {
            // Si el color no es válido, muestra un mensaje de advertencia al usuario y usa un valor por defecto
            JOptionPane.showMessageDialog(
                null,
                "El valor configurado para 'alert.color' es inválido o no es un color soportado: '" + colorFromProperties + "'.\n" +
                "Por favor, asegúrese de que sea un código hexadecimal válido (ejemplo: #FF0000).\n" +
                "Se usará el valor por defecto: #CCCCCC.",
                "Advertencia de Configuración",
                JOptionPane.WARNING_MESSAGE
            );
            // Configura un color por defecto
            selectedColorButtonForAlertConfigColor.setBackground(Color.LIGHT_GRAY);
        }

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
        alertDialog.setLocationRelativeTo(owner);
        alertDialog.setVisible(true);
    }




        // Implementación del método para obtener un valor aleatorio del mapa
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
