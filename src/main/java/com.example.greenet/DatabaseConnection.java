package com.example.greenet;

import org.h2.tools.Server;

import java.io.File;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Gestiona la conexión a la base de datos H2 embebida y la consola web
 * Implementa el patrón Singleton para una única instancia
 */
public class DatabaseConnection {

    // === SINGLETON INSTANCE ===
    private static DatabaseConnection instance;

    // === DATABASE CONFIGURATION ===
    private static final String URL = "jdbc:h2:file:./data/greenet;MODE=PostgreSQL;DATABASE_TO_UPPER=FALSE;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // === SERVER MANAGEMENT ===
    private static Server h2Server;
    private static int puertoH2;
    private static boolean consolaIniciada = false;

    // === CONNECTION POOL (opcional) ===
    private static Connection connection;

    /**
     * Constructor privado para prevenir instanciación externa
     */
    private DatabaseConnection() {
        // Inicialización perezosa
        System.out.println("🔧 Instancia DatabaseConnection creada");
    }

    /**
     * Obtiene la única instancia del Singleton
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtiene una conexión a la base de datos (método principal)
     */
    public static Connection getConnection() throws SQLException {
        // Si ya tenemos una conexión válida, la reutilizamos
        if (connection != null && !connection.isClosed() && connection.isValid(2)) {
            return connection;
        }

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Inicializar BD y consola
            initializeDatabase(connection);
            iniciarConsolaH2();

            return connection;

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de H2 no encontrado", e);
        }
    }

    public static void shutdown() {
    }

    /**
     * Cierra la conexión actual
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✅ Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Reinicia completamente la instancia singleton (útil para tests)
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.cleanup();
            instance = null;
            System.out.println("🔄 Instancia DatabaseConnection reiniciada");
        }
    }

    /**
     * Limpieza de recursos
     */
    private void cleanup() {
        closeConnection();
        cerrarConsolaH2();
    }

    // === MÉTODOS DE CONSOLA H2 (sin cambios) ===

    /**
     * Inicia la consola web H2 automáticamente
     */
    public static void iniciarConsolaH2() {
        if (consolaIniciada) {
            System.out.println("⚠️ Consola H2 ya está iniciada en puerto " + puertoH2);
            return;
        }

        try {
            puertoH2 = encontrarPuertoLibre(8082, 8100);
            File dbFile = new File("./data/greenet");
            String rutaAbsoluta = dbFile.getAbsolutePath().replace("\\", "/");

            h2Server = Server.createWebServer(
                    "-web", "-webAllowOthers", "-webPort", String.valueOf(puertoH2)
            ).start();

            consolaIniciada = true;

            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║        🌐 CONSOLA H2 INICIADA                         ║");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.println("║  URL:      http://localhost:" + puertoH2 + "                       ║");
            System.out.println("║  JDBC URL: jdbc:h2:file:" + rutaAbsoluta);
            System.out.println("║  Usuario:  sa                                          ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");

        } catch (SQLException e) {
            System.err.println("❌ Error al iniciar consola H2: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    /**
     * Cierra la consola H2
     */
    public static void cerrarConsolaH2() {
        if (h2Server != null) {
            h2Server.stop();
            consolaIniciada = false;
            System.out.println("✅ Consola H2 cerrada (puerto " + puertoH2 + ")");
        }
    }

    // === MÉTODOS DE INICIALIZACIÓN DE BD (sin cambios) ===

    private static void initializeDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // ... (tu código actual de initializeDatabase permanece igual)
            // Solo cambia el acceso a private
        }
    }

    // === MÉTODOS UTILITARIOS (sin cambios) ===

    private static int encontrarPuertoLibre(int inicio, int fin) {
        for (int puerto = inicio; puerto <= fin; puerto++) {
            try (ServerSocket socket = new ServerSocket(puerto)) {
                return puerto;
            } catch (Exception e) {
                // Puerto en uso, continuar
            }
        }
        throw new RuntimeException("No se encontró puerto libre entre " + inicio + " y " + fin);
    }

    // === MÉTODOS PÚBLICOS ADICIONALES ===

    /**
     * Verifica el estado de la conexión
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene el puerto de la consola H2
     */
    public int getPuertoH2() {
        return puertoH2;
    }

    /**
     * Verifica si la consola H2 está iniciada
     */
    public boolean isConsolaIniciada() {
        return consolaIniciada;
    }

    /**
     * Inicializa la base de datos manualmente
     */
    public static void inicializarBaseDatosManual() {
        System.out.println("🔧 Inicializando base de datos manualmente...");
        try {
            getConnection(); // Esto fuerza la inicialización
            System.out.println("✅ Base de datos inicializada correctamente");
        } catch (SQLException e) {
            System.err.println("❌ Error al inicializar BD: " + e.getMessage());
        }
    }
}
