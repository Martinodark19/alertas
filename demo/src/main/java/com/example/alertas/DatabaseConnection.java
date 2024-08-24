package com.example.alertas;

import java.sql.*;

public class DatabaseConnection {

    // Cambiar al nombre de tu base de datos
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=alertas_db;integratedSecurity=true;encrypt=false;";

    // Método para obtener el último registro de la tabla alertas
    public static Object[] fetchLastAlert() {
        Object[] lastAlert = null;
        String query = "SELECT TOP 1 * FROM dbo.alertas ORDER BY alertaId DESC"; // Selecciona el último registro por
                                                                                 // alertaId en orden descendente
        try (Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) 
            {
                lastAlert = new Object[30]; // Ajustar tamaño según el número de columnas de la tabla alertas

                lastAlert[0] = resultSet.getInt("alertaid");
                lastAlert[1] = resultSet.getString("codalerta");
                lastAlert[2] = resultSet.getString("nombre");
                lastAlert[3] = resultSet.getInt("sentenciaId");
                lastAlert[4] = resultSet.getTimestamp("inicioevento");
                lastAlert[5] = resultSet.getTimestamp("identificacionalerta");
                lastAlert[6] = resultSet.getString("nombreActivo");
                lastAlert[7] = resultSet.getString("proceso");
                lastAlert[8] = resultSet.getBigDecimal("latencia"); 
                lastAlert[9] = resultSet.getString("tipoServicio");
                lastAlert[10] = resultSet.getString("CI");
                lastAlert[11] = resultSet.getString("Subtiposervicio");
                lastAlert[12] = resultSet.getBigDecimal("jitter"); 
                lastAlert[13] = resultSet.getBigDecimal("disponibilidad"); 
                lastAlert[14] = resultSet.getBigDecimal("packetlost"); 
                lastAlert[15] = resultSet.getBigDecimal("rssi"); 
                lastAlert[16] = resultSet.getBigDecimal("nsr"); 
                lastAlert[17] = resultSet.getBigDecimal("PLM"); 
                lastAlert[18] = resultSet.getString("tipoExWa");
                lastAlert[19] = resultSet.getString("codigoEvento");
                lastAlert[20] = resultSet.getString("descripcionevento");
                lastAlert[21] = resultSet.getString("Origen");
                lastAlert[22] = resultSet.getString("tipodocumento");
                lastAlert[23] = resultSet.getString("estado");
                lastAlert[24] = resultSet.getString("resumen");
                lastAlert[25] = resultSet.getString("titulo");
                lastAlert[26] = resultSet.getString("numero");
                lastAlert[27] = resultSet.getTimestamp("fechaestado");
                lastAlert[28] = resultSet.getString("razonestado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastAlert;
    }
}
