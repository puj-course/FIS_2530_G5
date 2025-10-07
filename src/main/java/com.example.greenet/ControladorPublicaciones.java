package com.example.greenet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPublicaciones {

    // Crear una nueva publicación con material
    public int crearPublicacion(int usuarioId, String tituloPublicacion, String descripcionPublicacion,
                                String categoriaMaterial, byte[] imagen, String... parametrosMaterial) {

        // 1. Crear el material usando MaterialFactory
        Material material = MaterialFactory.crearMaterial(
                categoriaMaterial,
                tituloPublicacion, // El título del material puede ser el mismo que la publicación
                descripcionPublicacion, // La descripción del material puede ser la misma
                imagen,
                usuarioId,
                parametrosMaterial
        );

        // 2. Validar el material
        if (!material.validar()) {
            return -2; // Material no válido
        }

        // 3. Registrar el material en la base de datos
        int materialId = registrarMaterial(material);
        if (materialId <= 0) {
            return -3; // Error al registrar material
        }

        // 4. Crear la publicación asociada al material
        String sql = "{ ? = call crear_publicacion(?, ?, ?, ?) }";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, tituloPublicacion);
            stmt.setString(4, descripcionPublicacion);
            stmt.setInt(5, materialId);
            stmt.execute();

            return stmt.getInt(1);

        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    // Registrar material en la base de datos
    private int registrarMaterial(Material material) {
        String sql = "SELECT registrar_material(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, material.getTitulo());
            stmt.setString(3, material.getDescripcion());
            stmt.setString(4, material.getCategoria());
            stmt.setBytes(5, material.getImagen());
            stmt.setInt(6, material.getPublicadorId());

            stmt.execute();
            return stmt.getInt(1);

        } catch (Exception e) {
            System.err.println("Error al registrar material: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    // Obtener publicaciones de un usuario
    public List<Publicacion> obtenerPublicacionesUsuario(int usuarioId) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.estado, p.fecha_publicacion, " +
                "m.id as material_id, m.titulo as material_titulo, m.descripcion as material_descripcion, " +
                "m.categoria, m.imagen, m.estados as material_estado " +
                "FROM publicaciones p " +
                "JOIN materiales m ON p.material_id = m.id " +
                "WHERE p.usuario_id = ? AND p.estado != 'inactivo' " +
                "ORDER BY p.fecha_publicacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Crear el material específico usando MaterialFactory
                Material material = MaterialFactory.crearMaterial(
                        rs.getString("categoria"),
                        rs.getString("material_titulo"),
                        rs.getString("material_descripcion"),
                        rs.getBytes("imagen"),
                        usuarioId
                );
                material.setId(rs.getInt("material_id"));
                material.setEstado(rs.getInt("material_estado"));

                // Crear la publicación con el material
                Publicacion publicacion = new Publicacion(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        usuarioId,
                        material
                );
                publicacion.setId(rs.getInt("id"));
                publicacion.setEstado(rs.getString("estado"));
                publicacion.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));

                publicaciones.add(publicacion);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener publicaciones del usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return publicaciones;
    }

    // Buscar publicaciones
    public List<Publicacion> buscarPublicaciones(String filtro) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.estado, p.fecha_publicacion, " +
                "p.usuario_id, u.nombre, u.apellidos, " +
                "m.id as material_id, m.titulo as material_titulo, m.descripcion as material_descripcion, " +
                "m.categoria, m.imagen, m.estados as material_estado " +
                "FROM publicaciones p " +
                "JOIN materiales m ON p.material_id = m.id " +
                "JOIN usuarios u ON p.usuario_id = u.id " +
                "WHERE (p.titulo ILIKE ? OR p.descripcion ILIKE ? OR m.categoria ILIKE ?) " +
                "AND p.estado = 'activa' " +
                "ORDER BY p.fecha_publicacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String filtroLike = "%" + filtro + "%";
            stmt.setString(1, filtroLike);
            stmt.setString(2, filtroLike);
            stmt.setString(3, filtroLike);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Material material = MaterialFactory.crearMaterial(
                        rs.getString("categoria"),
                        rs.getString("material_titulo"),
                        rs.getString("material_descripcion"),
                        rs.getBytes("imagen"),
                        rs.getInt("usuario_id")
                );
                material.setId(rs.getInt("material_id"));
                material.setEstado(rs.getInt("material_estado"));

                Publicacion publicacion = new Publicacion(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getInt("usuario_id"),
                        material
                );
                publicacion.setId(rs.getInt("id"));
                publicacion.setEstado(rs.getString("estado"));
                publicacion.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                publicacion.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellidos"));

                publicaciones.add(publicacion);
            }

        } catch (Exception e) {
            System.err.println("Error al buscar publicaciones: " + e.getMessage());
            e.printStackTrace();
        }

        return publicaciones;
    }

    // Eliminar publicación
    public boolean eliminarPublicacion(int publicacionId, int usuarioId) {
        if (!verificarPropietario(publicacionId, usuarioId)) {
            System.err.println("El usuario no es propietario de esta publicación");
            return false;
        }

        String sql = "UPDATE publicaciones SET estado = 'inactivo' WHERE id = ? AND usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, publicacionId);
            stmt.setInt(2, usuarioId);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar publicación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Editar publicación
    public boolean editarPublicacion(int publicacionId, int usuarioId, String nuevoTitulo, String nuevaDescripcion) {
        if (!verificarPropietario(publicacionId, usuarioId)) {
            System.err.println("El usuario no es propietario de esta publicación");
            return false;
        }

        String sql = "UPDATE publicaciones SET titulo = ?, descripcion = ? WHERE id = ? AND usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoTitulo);
            stmt.setString(2, nuevaDescripcion);
            stmt.setInt(3, publicacionId);
            stmt.setInt(4, usuarioId);

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;

        } catch (Exception e) {
            System.err.println("Error al editar publicación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Verificar propietario
    private boolean verificarPropietario(int publicacionId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM publicaciones WHERE id = ? AND usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, publicacionId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            System.err.println("Error al verificar propietario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}