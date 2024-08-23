package com.example.alertas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Cambia "miBaseDeDatos" al nombre de tu base de datos.
    // Agregamos el parámetro encrypt=false para deshabilitar SSL.
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=alertas_db;integratedSecurity=true;encrypt=false;";

    public static void main(String[] args) {
        Connection conn = null;
 
        try 
        {
            // Intenta establecer la conexión con la base de datos
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Conexión exitosa a la base de datos");
        } 
        catch (SQLException e) 
        {
            // Manejo de excepciones
            System.out.println("Error al conectarse a la base de datos");
            e.printStackTrace();
        } 
        finally 
        {
            // Cerrar la conexión
            if (conn != null) {
                try {
                    conn.close();
                }
                 catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
