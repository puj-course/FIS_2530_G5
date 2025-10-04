package com.example.greenet;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControladorPublicaciones {

    // Crear una nueva publicación
    public int crearPublicacion(int usuarioId, Material material, String[] datos) {
        String sql = "{ ? = call crear_publicacion(?, ?, ?, ?) }";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, datos[0]); // título
            stmt.setString(4, datos[1]); // descripción
            stmt.setString(5, material.getTipo());
            stmt.execute();
            
            return stmt.getInt(1);
            
        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    // Eliminar publicación
    public boolean eliminarPublicacion(int publicacionId, int usuarioId) {
        // Primero verificar que la publicación pertenece al usuario
        if (!verificarPropietario(publicacionId, usuarioId)) {
            System.err.println("El usuario no es propietario de esta publicación");
            return false;
        }

        String sql = "{ ? = call eliminar_publicacion(?) }";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setInt(2, publicacionId);
            stmt.execute();
            
            return stmt.getBoolean(1);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar publicación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Editar publicación (cambiar título y descripción)
    public boolean editarPublicacion(int publicacionId, int usuarioId, String nuevoTitulo, String nuevaDescripcion) {
        if (!verificarPropietario(publicacionId, usuarioId)) {
            System.err.println("El usuario no es propietario de esta publicación");
            return false;
        }

        String sql = "UPDATE publicaciones SET titulo = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoTitulo);
            stmt.setString(2, nuevaDescripcion);
            stmt.setInt(3, publicacionId);
            
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error al editar publicación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Buscar publicaciones con filtros
    public List<Publicacion> buscarPublicaciones(String filtro) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.estado, p.fecha_publicacion, " +
                     "u.nombre, u.apellidos, m.tipo, m.unidad_media " +
                     "FROM publicaciones p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "LEFT JOIN materiales m ON p.material_id = m.id " +
                     "WHERE p.titulo ILIKE ? OR p.descripcion ILIKE ? " +
                     "ORDER BY p.fecha_publicacion DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String filtroLike = "%" + filtro + "%";
            stmt.setString(1, filtroLike);
            stmt.setString(2, filtroLike);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Publicacion pub = new Publicacion();
                pub.setId(rs.getInt("id"));
                pub.setTitulo(rs.getString("titulo"));
                pub.setDescripcion(rs.getString("descripcion"));
                pub.setEstado(rs.getString("estado"));
                pub.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                pub.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellidos"));
                pub.setTipoMaterial(rs.getString("tipo"));
                pub.setUnidadMedia(rs.getInt("unidad_media"));
                
                publicaciones.add(pub);
            }
            
        } catch (Exception e) {
            System.err.println("Error al buscar publicaciones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return publicaciones;
    }

    // Obtener publicaciones de un usuario específico
    public List<Publicacion> obtenerPublicacionesUsuario(int usuarioId) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.estado, p.fecha_publicacion, " +
                     "m.tipo, m.unidad_media " +
                     "FROM publicaciones p " +
                     "LEFT JOIN materiales m ON p.material_id = m.id " +
                     "WHERE p.usuario_id = ? " +
                     "ORDER BY p.fecha_publicacion DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Publicacion pub = new Publicacion();
                pub.setId(rs.getInt("id"));
                pub.setTitulo(rs.getString("titulo"));
                pub.setDescripcion(rs.getString("descripcion"));
                pub.setEstado(rs.getString("estado"));
                pub.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                pub.setTipoMaterial(rs.getString("tipo"));
                pub.setUnidadMedia(rs.getInt("unidad_media"));
                
                publicaciones.add(pub);
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener publicaciones del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return publicaciones;
    }

    // Verificar que el usuario es propietario de la publicación
    private boolean verificarPropietario(int publicacionId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM publicaciones WHERE id = ? AND usuario_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, publicacionId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (Exception e) {
            System.err.println("Error al verificar propietario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    // Cambiar estado de publicación
    public boolean cambiarEstado(int publicacionId, String nuevoEstado) {
        String sql = "UPDATE publicaciones SET estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, publicacionId);
            
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
