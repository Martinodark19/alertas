package com.example.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties 
{
    private static final Properties properties = new Properties();

    // Cargar el archivo properties al iniciar la clase
    static 
    {
        try (InputStream input = ConfigProperties.class.getClassLoader()
                .getResourceAsStream("application.properties")) 
        {

            if (input == null) 
            {
                throw new IOException("No se pudo encontrar el archivo application.properties");
            }

            properties.load(input);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Error al cargar las propiedades", e);
        }
    }

    // Método para obtener una propiedad por su clave
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Opcional: Método para obtener todas las propiedades
    public static Properties getAllProperties() 
    {
        return properties;
    }
}
