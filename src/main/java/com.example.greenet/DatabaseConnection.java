package com.example.greenet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/greenet";
    private static final String USER = "brand";
    private static final String PASSWORD = "2005";
    
    // Instancia única de Connection
    private static Connection instance;
    
    // Constructor privado para evitar instanciación
    private DatabaseConnection() {}
}
