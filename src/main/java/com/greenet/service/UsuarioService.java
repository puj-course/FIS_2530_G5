package com.greenet.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.greenet.Admin;
import com.greenet.DatabaseConnection;

/**
 * Servicio para manejar operaciones de usuarios
 * Reemplaza las funciones almacenadas de PostgreSQL con código Java
 */
public class UsuarioService {

    /**
     * Registra un nuevo usuario
     *
     * @return 0=éxito, 1=correo inválido, 2=correo ya existe, 3=rol inválido, 4=tipo doc inválido
     */
    public static int registrarUsuario(
            String nombre,
            String apellidos,
            String fechaNacimiento,
            String tipoDoc,
            String numeroDoc,
            String correo,
            String contrasena,
            String rol,
            String telefono,
            String direccion
    ) {
        // 1. Validar correo
        if (!correo.matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")) {
            return 1; // Correo inválido
        }

        // 2. Determinar rol_id
        int rolId;
        if ("administrador".equalsIgnoreCase(rol)) {
            rolId = 2;
        } else if ("usuario".equalsIgnoreCase(rol)) {
            rolId = 1;
        } else {
            return 3; // Rol inválido
        }

        // 3. Determinar tipo_id
        int tipoId;
        switch (tipoDoc.toUpperCase()) {
            case "CC" -> tipoId = 1;
            case "TI" -> tipoId = 2;
            case "CE" -> tipoId = 3;
            case "PASAPORTE" -> tipoId = 4;
            default -> {
                return 4; // Tipo documento inválido
            }
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // 4. Verificar si el correo ya existe
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, correo);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return 2; // Correo ya existe
                }
            }

            // 5. Encriptar contraseña y número de documento
            byte[] contrasenaHash = hashSHA256(contrasena);
            byte[] numeroDocHash = hashSHA256(numeroDoc);

            // 6. Insertar usuario
            String insertSql = """
                        INSERT INTO usuarios (nombre, apellidos, fechaNacimiento, tipo_id, numero_doc,
                                           correo, contrasena, rol_id, fechaCreacion, estado, telefono, direccion)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 1, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, apellidos);
                pstmt.setDate(3, Date.valueOf(fechaNacimiento));
                pstmt.setInt(4, tipoId);
                pstmt.setBytes(5, numeroDocHash);
                pstmt.setString(6, correo);
                pstmt.setBytes(7, contrasenaHash);
                pstmt.setInt(8, rolId);
                pstmt.setString(9, telefono);
                pstmt.setString(10, direccion);

                int rowsAffected = pstmt.executeUpdate();

                // DEBUG: Verificar que se insertó
                System.out.println("✅ Usuario registrado: " + nombre + " " + apellidos);
                System.out.println("   Filas afectadas: " + rowsAffected);
                System.out.println("   Correo: " + correo);
            }

            return 0; // Éxito

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return -1; // Error de BD
        }
    }

    /**
     * Inicia sesión de un usuario
     *
     * @return 0=éxito, 1=usuario no existe, 2=contraseña incorrecta, -1=error
     */
    public static int iniciarSesion(String correo, String contrasena) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1. Verificar si el usuario existe
            String checkUserSql = "SELECT id, contrasena, estado FROM usuarios WHERE correo = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(checkUserSql)) {
                pstmt.setString(1, correo);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    return 1; // Usuario no existe
                }

                int usuarioId = rs.getInt("id");
                byte[] contrasenaAlmacenada = rs.getBytes("contrasena");
                int estado = rs.getInt("estado");

                // Verificar estado del usuario
                if (estado != 1) {
                    return 1; // Usuario bloqueado o reportado
                }

                // 2. Verificar contraseña
                byte[] contrasenaHash = hashSHA256(contrasena);

                if (!MessageDigest.isEqual(contrasenaAlmacenada, contrasenaHash)) {
                    return 2; // Contraseña incorrecta
                }

                // 3. Registrar sesión
                String insertSesionSql = "INSERT INTO sesiones(id_usuario, fecha, estado) VALUES (?, CURRENT_TIMESTAMP, 1)";
                try (PreparedStatement sesionStmt = conn.prepareStatement(insertSesionSql)) {
                    sesionStmt.setInt(1, usuarioId);
                    sesionStmt.executeUpdate();
                }

                System.out.println("✅ Sesión iniciada para usuario ID: " + usuarioId);
                return 0; // Éxito
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Cierra la sesión de un usuario
     *
     * @return 0=éxito, 1=no hay sesión activa, -1=error
     */
    public static int cerrarSesion(int usuarioId) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Verificar si hay sesión activa
            String checkSql = "SELECT COUNT(*) FROM sesiones WHERE id_usuario = ? AND estado = 1";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, usuarioId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    return 1; // No hay sesión activa
                }
            }

            // Cerrar sesión
            String updateSql = "UPDATE sesiones SET estado = 2 WHERE id_usuario = ? AND estado = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, usuarioId);
                pstmt.executeUpdate();
            }

            System.out.println("✅ Sesión cerrada para usuario ID: " + usuarioId);
            return 0; // Éxito

        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar sesión: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Actualiza el correo de un usuario
     *
     * @return 0=éxito, 1=correo inválido, 2=correo ya existe, 3=usuario no encontrado
     */
    public static int actualizarCorreo(int usuarioId, String nuevoCorreo) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1. Obtener correo actual
            String getCurrentSql = "SELECT correo FROM usuarios WHERE id = ?";
            String correoActual = null;

            try (PreparedStatement pstmt = conn.prepareStatement(getCurrentSql)) {
                pstmt.setInt(1, usuarioId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    correoActual = rs.getString("correo");
                } else {
                    return 3; // Usuario no encontrado
                }
            }

            // 2. Si es el mismo correo, permitir
            if (correoActual.equals(nuevoCorreo)) {
                return 0; // Éxito (mismo correo)
            }

            // 3. Validar formato
            if (!nuevoCorreo.matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")) {
                return 1; // Correo inválido
            }

            // 4. Verificar si ya existe
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE correo = ? AND id != ? AND estado = 1";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, nuevoCorreo);
                checkStmt.setInt(2, usuarioId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return 2; // Correo ya en uso
                }
            }

            // 5. Actualizar correo
            String updateSql = "UPDATE usuarios SET correo = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, nuevoCorreo);
                pstmt.setInt(2, usuarioId);
                pstmt.executeUpdate();
            }

            System.out.println("✅ Correo actualizado para usuario ID: " + usuarioId);
            return 0; // Éxito

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar correo: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Bloquea un usuario reportado
     */
    public static boolean bloquearUsuario(int usuarioId) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "UPDATE usuarios SET estado = 3 WHERE id = ? AND estado = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, usuarioId);
                int affected = pstmt.executeUpdate();

                if (affected > 0) {
                    System.out.println("✅ Usuario bloqueado ID: " + usuarioId);
                    return true;
                }
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al bloquear usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Encripta un texto usando SHA-256
     */
    private static byte[] hashSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
    public static Boolean VerificarAdmin(int usuarioId) {
        String sql = "SELECT 1 FROM usuarios WHERE rol_id = 2 AND id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { // esta es de default

            stmt.setInt(1, usuarioId);  // asignamos el parámetro id
            ResultSet rs = stmt.executeQuery();

            // Si existe un registro, significa que el usuario es admin
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar administrador: " + e.getMessage());
            return false;
        }
    }
    public static List<Admin> obtenerAdmins() {
        List<Admin> admins = new ArrayList<>();

        String sql = "SELECT nombre, correo, telefono FROM usuarios WHERE rol = 2";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                Long telefono = rs.getLong("telefono");

                admins.add(new Admin(nombre, correo, telefono));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener administradores: " + e.getMessage());
        }

        return admins;
    }
    public static int BuscarId(String correo) {
        String sql = "SELECT id FROM usuarios WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);  // asignamos el parámetro correo
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // si hay un registro
                return rs.getInt("id"); // devolvemos el id
            } else {
                return -1; // no se encontró el usuario
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ID: " + e.getMessage());
            return -1;
        }
    }
    public static Integer BuscarPublicadorId(String nombrePublicacion) {
        String sql = "SELECT publicador_id FROM publicaciones WHERE titulo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,nombrePublicacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("publicador_id");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar publicador_id: " + e.getMessage());
        }

        return null; // si no encuentra nada
    }


    public static String BuscarDescripcion(String nombrePublicacion) {
        String sql = "SELECT descripcion FROM publicaciones WHERE titulo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,nombrePublicacion );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("descripcion");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar descripción: " + e.getMessage());
        }

        return null;
    }
    public static String BuscarNombrePorId(int publicadorId) {
        String sql = "SELECT nombre FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, publicadorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar nombre del usuario: " + e.getMessage());
        }

        return null; // si no encuentra el usuario
    }





}


