package com.example.alertas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.alertas.configuracion.AlertasConfig;
import com.example.alertas.configuracion.ConfigProperties;
import com.example.alertas.configuracion.DatabaseConnection;
import com.example.alertas.figuras.FigurasDivididas;
import com.example.alertas.figuras.ShapePanel;
import com.example.alertas.ui_modificadores.ModificadoresInterfaz;

public class MainSwing 
{
    // map para almacenar las secciones
    public static Map<Integer, JPanel> allSections = new HashMap<>();

    // Mapa para almacenar los colores de las alertas
    private Map<String, Color> severityColorMap = new HashMap<>();

    public static JPanel selectedSection;
    public static JLabel selectedSectionLabel; // Para cambiar el título de la sección
    public static JPanel sectionsPanel;

    public static JButton selectedColorButton; // Para actualizar el color del botón en el diálogo de Configurar Alerta
    //public static JButton selectedColorButtonForAlertConfigColor; // Para actualizar el color del botón en el diálogo de

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
    public static DatabaseConnection databaseConnection;
    public static AlertasConfig alertasConfig;


    public static Map<String, Color> coloresPorSeveridadMap = new HashMap<>();
    
    public static Map<String, Color> getColoresPorSeveridadMap() 
    {
        coloresPorSeveridadMap.put("Critica", Color.RED);
        coloresPorSeveridadMap.put("Alta", Color.ORANGE);
        coloresPorSeveridadMap.put("Media", Color.PINK);
        coloresPorSeveridadMap.put("Baja", Color.BLACK);
        return coloresPorSeveridadMap;
    }

    // checkbox del boton de Activado y desactivdo de pop-up
    //JCheckBox popupCheckBox = new JCheckBox("Mostrar/Ocultar", configuracion.isShowPopup()); // Inicializa marcado
    public static String showPopupFromProperties = ConfigProperties.getProperty("app.showPopup").trim().toLowerCase();
    public static String hideTableFromProperties = ConfigProperties.getProperty("app.hideTable").trim().toLowerCase();

    public static JCheckBox popupCheckBox = new JCheckBox("Mostrar/Ocultar", Boolean.parseBoolean(showPopupFromProperties)); // Inicializa marcado

    // checkbox del boton de Activado y desactivdo de visualizar tablas
    public static JCheckBox hideTableCheckBox = new JCheckBox("Mostrar/Ocultar", Boolean.parseBoolean(hideTableFromProperties));
    
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



            // Colores por defecto

        this.databaseConnection = databaseConnection;

        this.alertasConfig = new AlertasConfig(); // Inicializar AlertasConfig

        JFrame frame = new JFrame("Mi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa

        String dbUrl = DatabaseConnection.getDbUrl();

        try (Connection connection = DriverManager.getConnection(dbUrl)) 
        {
            System.out.println("Conexión a la base de datos exitosa."); // Mensaje opcional
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
            System.exit(0); // Cambiado de 0 a 1 para indicar un error
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




        for (int i = 1; i <= 8; i++) 
        {
            JPanel sectionPanel = new JPanel(new BorderLayout());
            sectionPanel.setBackground(Color.decode("#cccccc"));
            sectionPanel.setBorder(new EmptyBorder(7, 7, 7, 7)); // Reducimos los bordes internos
            sectionPanel.setName("Section-" + i);


            // Contenido de la sección
            String getNameSectionFromProperties = ConfigProperties.getProperty("section." + i);
            if (getNameSectionFromProperties == null || getNameSectionFromProperties.isEmpty()) 
            {
                JOptionPane.showMessageDialog(
                    null,
                    "El archivo de configuración no contiene el nombre para la sección " + i + ".\n" +
                    "Por favor, asegúrese de que el archivo 'config.properties' incluya todas las secciones",
                    "Error en Configuración de Secciones",
                    JOptionPane.WARNING_MESSAGE
                );

            }
        
            JLabel sectionLabel = new JLabel(getNameSectionFromProperties, SwingConstants.CENTER);
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
            figuresPanelLeft.setMinimumSize(new Dimension(47, 47));
            figuresPanelLeft.setMaximumSize(new Dimension(47, 47));

            // Botón de Cambiar Color
            JButton changeColorButton = new JButton("Cambiar Color");
            changeColorButton.setBackground(Color.decode("#009dad"));
            changeColorButton.setForeground(Color.WHITE);
            changeColorButton.setFont(new Font("Arial", Font.PLAIN, 8));
            changeColorButton.setMargin(new Insets(0, 0, 0, 0));
            changeColorButton.setBorderPainted(false);
            changeColorButton.setFocusPainted(false);
            changeColorButton.setPreferredSize(new Dimension(68, 25));

            changeColorButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    selectedSection = sectionPanel;
                    ModificadoresInterfaz.showColorPickerModal(frame);
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
                    ModificadoresInterfaz.showTitleChangeModal(frame, sectionPanel.getName());
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
            ModificadoresInterfaz.removeSpecificSections(new int[] {2,4,6,8});
        }

        // Actualizar el panel para mostrar la nueva configuración
        sectionsPanel.revalidate();
        sectionsPanel.repaint();


        // Añadir secciones al GridBagLayout
gbc.gridx = 0;
gbc.gridy = 0;
gbc.weightx = 1;
gbc.weighty = 0.2; // Las secciones ocupan un 20% del espacio vertical
gbc.insets = new Insets(0, 0, 0, 0); // Sin márgenes
gbc.fill = GridBagConstraints.BOTH;
contentPanel.add(sectionsPanel, gbc);

// Crear la leyenda de figuras
JPanel figureLegendPanel = ShapePanel.createFigureTipoServicioPanel();

// Configurar el GridBagConstraints para la leyenda
gbc.gridx = 0;
gbc.gridy = 1; // Posición debajo de las secciones
gbc.weightx = 1;
gbc.weighty = 0; // No ocupa espacio extra vertical
gbc.insets = new Insets(0, 0, 0, 0); // Sin márgenes
gbc.fill = GridBagConstraints.HORIZONTAL; // Solo se expande horizontalmente
contentPanel.add(figureLegendPanel, gbc);



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

                for (Object[] alert : alertsArray) 
                {
                }

                //List<Object[]> alertasConPermisosAd = databaseConnection.filtrarAlertasConPermiso(alertsArray);

                

                                                        
                // aqui le debo enviar ese array a otro metodo para que nos retorne el array 

                /* 
                if (!alertasConPermisosAd.isEmpty()) 
                {
                    for (Object[] alert : alertasConPermisosAd) 
                    {
                        alertTableModel.insertRow(0, alert); // Inserta en la primera posición
                        lastAlertForPopup = alert;

                        // Lógica para mostrar la figura en la sección correspondiente
                        showAlertsToSection = true;

                        // alert proceso sera ejemplo 1
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
                                System.out.println("entro al caudrado");
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

                */
                // este sera el caso de que no tenga los permisos

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

                                String extraerSeccionProceso = (String) alert[7]; // Ejemplo: acceder al proceso (posición 1)

                                if (extraerSeccionProceso != null && extraerSeccionProceso.matches("[1357]")) 
                                {
                                    int seccion = Integer.parseInt(extraerSeccionProceso);
                                } 
                                else 
                                {
                                    JOptionPane.showMessageDialog(
                                        null,
                                        "El valor '" + extraerSeccionProceso + "' no es válido para la columna proceso.\n" +
                                        "Por favor, asegúrate de que el número ingresado sea uno de los siguientes valores permitidos: 1, 3, 5, 7.",
                                        "Error de Validación",
                                        JOptionPane.ERROR_MESSAGE
                                    );      
                                    continue; // Salir del bucle actual      
                                }
                                

                                int[] seccionesEspecificas  = { 0, 2, 4, 6};

                                Map<Integer, JPanel> seccionesMap = new LinkedHashMap<>();

                                for (int sectionIndex : seccionesEspecificas) 
                                {
                                    JPanel sectionPanel = allSections.get(Integer.parseInt(extraerSeccionProceso)); // Ajuste de índice
                                    if (sectionPanel != null && sectionPanel.getParent() != null) 
                                    {
                                        seccionesMap.put(sectionIndex, sectionPanel); // Añadir al mapa
                                    }
                                }

                                // Obtener un valor aleatorio
                                JPanel randomValue = ModificadoresInterfaz.getRandomValueFromMap(seccionesMap);
                                                                
                                // Obtén el `labelsPanel` de esa sección para añadir la figura
                                JPanel labelsPanel = (JPanel) randomValue.getComponent(2); 

                                                
                                                            //obtener el color de la configuracion

                                //obtener el color segun el tipo de severidad de cada alertas


                                /* */
                                //obtener el tipoServicio para determinar la figura a asociar
                                String tipoServicio = (String) alert[9]; // Ejemplo: acceder a tipoServicio


                                // Asociaciones de tipoServicio con figuras
                                String asociaciones = """
                                    Asociaciones disponibles:
                                    Tipo 1 → Círculo
                                    Tipo 2 → Cuadrado
                                    Tipo 3 → Triángulo
                                    """;

                                // Determinar la figura asociada
                                String figura = "";

                                switch (tipoServicio.toLowerCase()) 
                                {
                                    case "tipo 1":
                                        figura = "circulo";
                                        break;
                                    case "tipo 2":
                                        figura = "cuadrado";
                                        break;
                                    case "tipo 3":
                                        figura = "triangulo";
                                        break;
                                    default:
                                    figura = "error";
                                    JOptionPane.showMessageDialog(null, 
                                    "Tipo de servicio desconocido: " + tipoServicio + ". No se puede determinar la figura.\n" + asociaciones, 
                                    "Advertencia", 
                                    JOptionPane.WARNING_MESSAGE);
                                        break;                       
                                }

                                if(figura.equals("error"))
                                {
                                    continue;
                                }
                                Integer severidad = (Integer) alert[34];

                                //Asignar la forma a la configuracion
                                //alertasConfig.setForma(figura);

                                //Asignar la severidad a la configuracion
                                Color alertColor;

                                switch (severidad) 
                                {
                                    case 1:
                                        alertColor = coloresPorSeveridadMap.get("Baja"); // Convierte el valor hexadecimal a un objeto Color
                                        break;

                                    case 2:
                                        alertColor = coloresPorSeveridadMap.get("Media"); // Convierte el valor hexadecimal a un objeto Color
                                        break;

                                    case 3:
                                    alertColor = coloresPorSeveridadMap.get("Alta"); // Convierte el valor hexadecimal a un objeto Color
                                    break;

                                    case 4:
                                       alertColor = coloresPorSeveridadMap.get("Critica"); // Convierte el valor hexadecimal a un objeto Color
                                       break;

                                    default:
                                        throw new AssertionError();
                                }

                                
                                
                                // Ejemplo de uso
                                //Integer severidad = (Integer) alert[34];
                                //System.out.println("esta es la severidad: " + severidad);
                               //Color colorAsociado = severityColorMap.get(severidad);


                               //String alertColorFromProperties = ConfigProperties.getProperty("alert.color").trim();

                               //Color alertColor = Color.decode(alertColorFromProperties); // Convierte el valor hexadecimal a un objeto Color

                                //Seleccionar la figura basada en la configuración de la alerta
                                JPanel figuraPanel;
                                        switch (figura) 
                                        {
                                            case "circulo":

                                                figuraPanel = new FigurasDivididas.CirculoPanel(alertColor,
                                                        new Object[][] { alert });
                                                if (popupCheckBox.isSelected()) 
                                                {
                                                    ModificadoresInterfaz.openPopupWithTable(new Object[][] { alert });
                                                }
                                                break;
                                            case "cuadrado":
                                                figuraPanel = new FigurasDivididas.CuadradoPanel(alertColor,
                                                        new Object[][] { alert });
                                                if (popupCheckBox.isSelected()) {
                                                    ModificadoresInterfaz.openPopupWithTable(new Object[][] { alert });
                                                }

                                                break;
                                            case "triangulo":
                                                figuraPanel = new FigurasDivididas.TrianguloPanel(alertColor,
                                                        new Object[][] { alert });
                                                if (popupCheckBox.isSelected()) {
                                                    ModificadoresInterfaz.openPopupWithTable(new Object[][] { alert });
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

        // Configuración de las tablas
        gbc.gridx = 0;
        gbc.gridy = 2; // Posición debajo de la leyenda
        gbc.weightx = 1;
        gbc.weighty = 0.8; // Las tablas ocupan el 80% del espacio vertical
        gbc.insets = new Insets(0, 0, 0, 0); // Sin márgenes
        gbc.fill = GridBagConstraints.BOTH;
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
                ModificadoresInterfaz.showAlertConfigDialog(frame);
            }
        });

        // Evento para abrir el diálogo de configuración
        configureWindowButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificadoresInterfaz.showConfigDialog(frame);
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


        // Define un mapa para almacenar las secciones eliminadas
        public static Map<Integer, JPanel> removedSectionsMap = new LinkedHashMap<>();











}
