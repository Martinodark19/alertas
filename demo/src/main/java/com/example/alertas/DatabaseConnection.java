package com.example.alertas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection 
{
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=alertas_db;integratedSecurity=true;encrypt=false;";

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
                Object[] alert = new Object[30]; // Ajustar tamaño según el número de columnas de la tabla alertas

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

                alertList.add(alert);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return alertList; // Devolver la lista de las nuevas alertas ordenadas por alertaId ascendente
    }
}
