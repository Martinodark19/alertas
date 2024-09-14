package com.example.alertas;

import java.util.ArrayList;
import java.util.List;

public class DbTest 
{
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

    public static void main(String[] args) 
    {
        // Datos de prueba con todas las columnas de la tabla alertas
        Object[][] alertas = {
            {1, "cod1", "Alerta 1", 123, "2024-08-01 12:30:00", "2024-08-01 12:31:00", "Activo1", "MQoS", 100, "Servicio1", "CI1", "Subtipo1", 10, 99, 1, 50, 0, 10, "Tipo1", "Evento1", "Descripción Evento 1", "Origen1", "Documento1", "Estado1", "Resumen1", "Título1", "Número1", "2024-08-01 12:45:00", "Razón1"},
            {2, "cod2", "Alerta 2", 124, "2024-08-02 12:30:00", "2024-08-02 12:31:00", "Activo2", "OtroProceso", 200, "Servicio2", "CI2", "Subtipo2", 20, 98, 2, 60, 1, 20, "Tipo2", "Evento2", "Descripción Evento 2", "Origen2", "Documento2", "Estado2", "Resumen2", "Título2", "Número2", "2024-08-02 12:45:00", "Razón2"},
            {3, "cod3", "Alerta 3", 125, "2024-08-03 12:30:00", "2024-08-03 12:31:00", "Activo3", "MQoS", 300, "Servicio3", "CI3", "Subtipo3", 30, 97, 3, 70, 2, 30, "Tipo3", "Evento3", "Descripción Evento 3", "Origen3", "Documento3", "Estado3", "Resumen3", "Título3", "Número3", "2024-08-03 12:45:00", "Razón3"}
        };

        // Llamar al método para filtrar las alertas
        List<Object[]> alertasConPermisos = filtrarAlertasConPermiso(alertas);

        // Mostrar todas las alertas que tienen permisos (proceso = MQoS)
        for (Object[] alerta : alertasConPermisos) 
        {
            System.out.println("Alerta con permiso:");
            for (Object campo : alerta) 
            {
                System.out.print(campo + " ");
            }
            System.out.println(); // Para nueva línea
        }
    }
}
