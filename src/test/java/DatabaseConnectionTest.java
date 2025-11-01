
package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @AfterEach
    public void tearDown() throws Exception {
        // Cerrar la conexión después de cada prueba para limpiar el estado
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * Método 1: Verificar que getConnection() devuelve una conexión válida
     */
    @Test
    public void testGetConnection_ReturnsValidConnection() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        assertNotNull(conn, "La conexión no debería ser null");
        assertFalse(conn.isClosed(), "La conexión debería estar abierta");
    }

    /**
     * Método 2: Verificar patrón Singleton
     */
    @Test
    public void testGetConnection_ReturnsSameInstance() throws SQLException {
        Connection conn1 = DatabaseConnection.getConnection();
        Connection conn2 = DatabaseConnection.getConnection();

        assertSame(conn1, conn2, "Debería retornar la misma instancia (Singleton)");
    }
}
