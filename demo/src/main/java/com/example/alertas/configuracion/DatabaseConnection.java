package com.example.alertas.configuracion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection 
{
    private static final String DB_URL_FROM_PROPERTIES = ConfigProperties.getProperty("db.url");

    private static final String DB_URL = DB_URL_FROM_PROPERTIES + ";integratedSecurity=true;encrypt=false;";
    
    // Getter para DB_URL
    public static String getDbUrl() 
    {
        return DB_URL;
    }

    public static List<Object[]> fetchAlertsAfterId(int lastId) 
    {
        List<Object[]> alertList = new ArrayList<>();
        String query = "SELECT * FROM dbo.alertas WHERE alertaId > ? ORDER BY alertaId ASC";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) 
        {
            statement.setInt(1, lastId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) 
            {
                Object[] alert = new Object[42]; // Ajustar tamaño según el número de columnas de la tabla alertas

                alert[0] = resultSet.getInt("alertaid");
                alert[1] = resultSet.getString("codalerta");
                alert[2] = resultSet.getString("nombre");
                alert[3] = resultSet.getInt("sentenciaId");
                alert[4] = resultSet.getTimestamp("inicioevento");
                alert[5] = resultSet.getTimestamp("identificacionalerta");
                alert[6] = resultSet.getString("nombreActivo");
                alert[7] = resultSet.getString("proceso");
                alert[8] = resultSet.getBigDecimal("latencia");
                alert[9] = resultSet.getString("tipoServicio");
                alert[10] = resultSet.getString("CI");
                alert[11] = resultSet.getString("Subtiposervicio");
                alert[12] = resultSet.getBigDecimal("jitter");
                alert[13] = resultSet.getBigDecimal("disponibilidad");
                alert[14] = resultSet.getBigDecimal("packetlost");
                alert[15] = resultSet.getBigDecimal("rssi");
                alert[16] = resultSet.getBigDecimal("nsr");
                alert[17] = resultSet.getBigDecimal("PLM");
                alert[18] = resultSet.getString("tipoExWa");
                alert[19] = resultSet.getString("codigoEvento");
                alert[20] = resultSet.getString("descripcionevento");
                alert[21] = resultSet.getString("Origen");
                alert[22] = resultSet.getString("tipodocumento");
                alert[23] = resultSet.getString("estado");
                alert[24] = resultSet.getString("resumen");
                alert[25] = resultSet.getString("titulo");
                alert[26] = resultSet.getString("numero");
                alert[27] = resultSet.getTimestamp("fechaestado");
                alert[28] = resultSet.getString("razonestado");
                alert[29] = resultSet.getBigDecimal("gpsx");
                alert[30] = resultSet.getBigDecimal("gpsy");
                alert[31] = resultSet.getBigDecimal("gpsz");
                alert[32] = resultSet.getBigDecimal("gpsh");
                alert[33] = resultSet.getBigDecimal("radio");
                alert[34] = resultSet.getInt("severidad");
                alert[35] = resultSet.getString("userid");
                alert[36] = resultSet.getString("comentario");
                alert[37] = resultSet.getString("valida");
                alert[38] = resultSet.getString("ot");
                alert[39] = resultSet.getString("ticket");
                alert[40] = resultSet.getDate("fecha_reconocimiento");
                alert[41] = resultSet.getString("grupo_local");


                alertList.add(alert);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return alertList; // Devolver la lista de las nuevas alertas ordenadas por alertaId ascendente
    }


       

        // Método que recibe un array de alertas y filtra las que tengan "proceso" igual a "MQoS"
        public static List<Object[]> filtrarAlertasConPermiso(Object[][] alertas) 
        {
            List<Object[]> alertasConPermisos = new ArrayList<>();
    
            // Recorrer el array de alertas
            for (Object[] alerta : alertas) 
            {
                String proceso = (String) alerta[7]; // El campo proceso se asume en la posición 7 del array
    
                // Compara si el campo proceso es igual a "MQoS"
                if ("MQoS".equals(proceso)) 
                {
                    alertasConPermisos.add(alerta); // Añadir la alerta al nuevo array si el proceso es "MQoS"
                }
            }
    
            return alertasConPermisos; // Retornar el array con las alertas filtradas
        }
        public List<Object[]> getAlertsByProceso(String proceso) 
        {
            List<Object[]> results = new ArrayList<>();
            String sql = "SELECT * FROM alertas WHERE proceso = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, proceso);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = new Object[42]; // Ajustar según el número de columnas en la tabla
                        row[0] = rs.getInt("alertaid");
                        row[1] = rs.getString("codalerta");
                        row[2] = rs.getString("nombre");
                        row[3] = rs.getInt("sentenciaId");
                        row[4] = rs.getTimestamp("inicioevento");
                        row[5] = rs.getTimestamp("identificacionalerta");
                        row[6] = rs.getString("nombreActivo");
                        row[7] = rs.getString("proceso");
                        row[8] = rs.getBigDecimal("latencia");
                        row[9] = rs.getString("tipoServicio");
                        row[10] = rs.getString("CI");
                        row[11] = rs.getString("Subtiposervicio");
                        row[12] = rs.getBigDecimal("jitter");
                        row[13] = rs.getBigDecimal("disponibilidad");
                        row[14] = rs.getBigDecimal("packetlost");
                        row[15] = rs.getBigDecimal("rssi");
                        row[16] = rs.getBigDecimal("nsr");
                        row[17] = rs.getBigDecimal("PLM");
                        row[18] = rs.getString("tipoExWa");
                        row[19] = rs.getString("codigoEvento");
                        row[20] = rs.getString("descripcionevento");
                        row[21] = rs.getString("Origen");
                        row[22] = rs.getString("tipodocumento");
                        row[23] = rs.getString("estado");
                        row[24] = rs.getString("resumen");
                        row[25] = rs.getString("titulo");
                        row[26] = rs.getString("numero");
                        row[27] = rs.getTimestamp("fechaestado");
                        row[28] = rs.getString("razonestado");
                        row[29] = rs.getBigDecimal("gpsx");
                        row[30] = rs.getBigDecimal("gpsy");
                        row[31] = rs.getBigDecimal("gpsz");
                        row[32] = rs.getBigDecimal("gpsh");
                        row[33] = rs.getBigDecimal("radio");
                        row[34] = rs.getInt("severidad");
                        row[35] = rs.getString("userid");
                        row[36] = rs.getString("comentario");
                        row[37] = rs.getString("valida");
                        row[38] = rs.getString("ot");
                        row[39] = rs.getString("ticket");
                        row[40] = rs.getDate("fecha_reconocimiento");
                        row[41] = rs.getString("grupo_local");
        
                        results.add(row);
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return results;
        }


        public boolean actualizarAlertaValid(int alertId, String usuario, String comentario) 
        {
            // Definir la URL de la base de datos y cualquier configuración adicional.

            try (Connection conn = DriverManager.getConnection(DB_URL)) 
            { // Cambia usuario y contraseña según tu BD
                // Consultar si la alerta existe
                try (PreparedStatement selectStatement = conn.prepareStatement("SELECT alertaid FROM alertas WHERE alertaid = ?")) 
                {
                    selectStatement.setInt(1, alertId);
                    try (ResultSet resultSet = selectStatement.executeQuery()) 
                    {
                        
                    // Obtener la fecha y hora actual
                    LocalDateTime fechaHoraActual = LocalDateTime.now();

                    // Convertir LocalDateTime a Timestamp
                    Timestamp timestamp = Timestamp.valueOf(fechaHoraActual);

                        if (resultSet.next()) 
                        {
                            // Si existe, actualizar usuario y comentario
                            try (PreparedStatement updateStatement = conn.prepareStatement(
                                    "UPDATE alertas SET userid = ?, comentario = ?, fecha_reconocimiento = ? WHERE alertaid = ?")) 
                            {
                                updateStatement.setString(1, usuario);
                                updateStatement.setString(2, comentario);
                                updateStatement.setTimestamp(3, timestamp);
                                updateStatement.setInt(4, alertId);
        
                                int rowsAffected = updateStatement.executeUpdate();
                                return rowsAffected > 0; // Retorna true si la actualización fue exitosa
                            }
                        } 
                        else 
                        {
                            return false; // La alerta no existe
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Error durante la operación
            }
        }



        public boolean actualizarAlerta(int alertId, String usuario) 
        {
            // Definir la URL de la base de datos y cualquier configuración adicional.

            try (Connection conn = DriverManager.getConnection(DB_URL)) 
            { // Cambia usuario y contraseña según tu BD
                // Consultar si la alerta existe
                try (PreparedStatement selectStatement = conn.prepareStatement("SELECT alertaid FROM alertas WHERE alertaid = ?")) 
                {
                    selectStatement.setInt(1, alertId);
                    try (ResultSet resultSet = selectStatement.executeQuery()) 
                    {
                        
                    // Obtener la fecha y hora actual
                    LocalDateTime fechaHoraActual = LocalDateTime.now();

                    // Convertir LocalDateTime a Timestamp
                    Timestamp timestamp = Timestamp.valueOf(fechaHoraActual);

                        if (resultSet.next()) 
                        {
                            // Si existe, actualizar usuario y comentario
                            try (PreparedStatement updateStatement = conn.prepareStatement(
                                    "UPDATE alertas SET userid = ?, fecha_reconocimiento = ? WHERE alertaid = ?")) 
                            {
                                updateStatement.setString(1, usuario);
                                updateStatement.setTimestamp(2, timestamp);
                                updateStatement.setInt(3, alertId);
        
                                int rowsAffected = updateStatement.executeUpdate();
                                return rowsAffected > 0; // Retorna true si la actualización fue exitosa
                            }
                        } 
                        else 
                        {
                            return false; // La alerta no existe
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Error durante la operación
            }
        }
        
        
        
        



    
}
