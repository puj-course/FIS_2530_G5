package com.example.greenet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPublicaciones {

    // Componentes FXML
    @FXML private VBox publicationsContainer;
    @FXML private TextField searchField;

    private PublicacionFactory publicacionFactory;
    private int usuarioIdActual = 1;

    @FXML
    public void initialize() {
        publicacionFactory = new PublicacionFactory();
        cargarPublicacionesUsuario();

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                buscarPublicaciones(newVal);
            });
        }
    }

    // ===== MÉTODOS DE INTERFAZ GRÁFICA =====

    private void cargarPublicacionesUsuario() {
        if (publicationsContainer != null) {
            publicationsContainer.getChildren().clear();

            List<Publicacion> publicaciones = obtenerPublicacionesUsuario(usuarioIdActual);

            for (Publicacion publicacion : publicaciones) {
                publicationsContainer.getChildren().add(crearTarjetaPublicacion(publicacion));
            }
        }
    }

    private void buscarPublicaciones(String filtro) {
        if (publicationsContainer != null && filtro != null && !filtro.trim().isEmpty()) {
            publicationsContainer.getChildren().clear();

            List<Publicacion> publicaciones = buscarPublicacionesBD(filtro.trim());

            for (Publicacion publicacion : publicaciones) {
                publicationsContainer.getChildren().add(crearTarjetaPublicacion(publicacion));
            }
        } else if (filtro != null && filtro.trim().isEmpty()) {
            cargarPublicacionesUsuario();
        }
    }

    private HBox crearTarjetaPublicacion(Publicacion publicacion) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-padding: 10; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Crear imagen desde bytes
        ImageView imageView = new ImageView();
        if (publicacion.getImagen() != null && publicacion.getImagen().length > 0) {
            Image image = new Image(new ByteArrayInputStream(publicacion.getImagen()));
            imageView.setImage(image);
        } else {
            imageView.setStyle("-fx-background-color: #E0E0E0; -fx-min-width: 70; -fx-min-height: 70;");
        }
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);

        VBox infoBox = new VBox(6);
        Label title = new Label(publicacion.getTitulo());
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label description = new Label(publicacion.getDescripcion());
        description.setWrapText(true);
        description.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");



        HBox botonesBox = new HBox(10);

        Button edit = new Button("Editar");
        edit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 11px;");

        Button delete = new Button("Eliminar");
        delete.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 11px;");

        // Configurar acciones de los botones
        final int publicacionId = obtenerIdPublicacion(publicacion);

        edit.setOnAction(e -> {
            editarPublicacion(publicacion);
        });

        delete.setOnAction(e -> {
            if (eliminarPublicacion(publicacionId, usuarioIdActual)) {
                if (publicationsContainer != null) {
                    publicationsContainer.getChildren().remove(card);
                }
                mostrarAlerta("Publicación eliminada", "La publicación \"" + publicacion.getTitulo() + "\" ha sido eliminada.");
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la publicación.");
            }
        });

        botonesBox.getChildren().addAll(edit, delete);
        infoBox.getChildren().addAll(title, description, botonesBox);
        card.getChildren().addAll(imageView, infoBox);

        return card;
    }


    private void editarPublicacion(Publicacion publicacion) {
        TextInputDialog tituloDialog = new TextInputDialog(publicacion.getTitulo());
        tituloDialog.setTitle("Editar Publicación");
        tituloDialog.setHeaderText("Editar Título");
        tituloDialog.setContentText("Nuevo título:");

        tituloDialog.showAndWait().ifPresent(nuevoTitulo -> {
            TextInputDialog descDialog = new TextInputDialog(publicacion.getDescripcion());
            descDialog.setTitle("Editar Publicación");
            descDialog.setHeaderText("Editar Descripción");
            descDialog.setContentText("Nueva descripción:");

            descDialog.showAndWait().ifPresent(nuevaDescripcion -> {
                if (editarPublicacionBD(
                        obtenerIdPublicacion(publicacion),
                        usuarioIdActual,
                        nuevoTitulo,
                        nuevaDescripcion)) {
                    mostrarAlerta("Éxito", "Publicación actualizada correctamente.");
                    cargarPublicacionesUsuario();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar la publicación.");
                }
            });
        });
    }

    // ===== MÉTODOS DE BASE DE DATOS =====

    public int crearPublicacion(int usuarioId, String titulo, String descripcion,
                                String categoria, byte[] imagen, String... parametrosEspecificos) {

        try {
            // Usar PublicacionFactory para crear la publicación específica
            Publicacion publicacion = publicacionFactory.crearPublicacion(
                    categoria, titulo, descripcion, imagen, usuarioId, parametrosEspecificos
            );

            // Registrar en base de datos
            return registrarPublicacionEnBD(publicacion, usuarioId);

        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private int registrarPublicacionEnBD(Publicacion publicacion, int usuarioId) {
        String sql = "{ ? = call crear_publicacion(?, ?, ?, ?, ?) }";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, publicacion.getTitulo());
            stmt.setString(4, publicacion.getDescripcion());
            stmt.setString(5, publicacion.getCategoria());
            stmt.setBytes(6, publicacion.getImagen());


            stmt.execute();
            return stmt.getInt(1);

        } catch (Exception e) {
            System.err.println("Error al registrar publicación en BD: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }



    public List<Publicacion> obtenerPublicacionesUsuario(int usuarioId) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.categoria, p.imagen, " +
                "p.modelo, p.marca, p.garantia, p.talla, p.material, p.tipo_mueble, " +
                "p.estado, p.fecha_publicacion " +
                "FROM publicaciones p " +
                "WHERE p.usuario_id = ? AND p.estado != 'inactivo' " +
                "ORDER BY p.fecha_publicacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Publicacion publicacion = crearPublicacionDesdeResultSet(rs);
                publicaciones.add(publicacion);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener publicaciones del usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return publicaciones;
    }

    public List<Publicacion> buscarPublicacionesBD(String filtro) {
        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.descripcion, p.categoria, p.imagen, " +
                "p.modelo, p.marca, p.garantia, p.talla, p.material, p.tipo_mueble, " +
                "p.estado, p.fecha_publicacion, u.nombre, u.apellidos " +
                "FROM publicaciones p " +
                "JOIN usuarios u ON p.usuario_id = u.id " +
                "WHERE (p.titulo ILIKE ? OR p.descripcion ILIKE ? OR p.categoria ILIKE ?) " +
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
                Publicacion publicacion = crearPublicacionDesdeResultSet(rs);
                publicacion.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellidos"));
                publicaciones.add(publicacion);
            }

        } catch (Exception e) {
            System.err.println("Error al buscar publicaciones: " + e.getMessage());
            e.printStackTrace();
        }

        return publicaciones;
    }

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

    public boolean editarPublicacionBD(int publicacionId, int usuarioId, String nuevoTitulo, String nuevaDescripcion) {
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

    // ===== MÉTODOS AUXILIARES =====

    private Publicacion crearPublicacionDesdeResultSet(ResultSet rs) throws Exception {
        String categoria = rs.getString("categoria");
        String titulo = rs.getString("titulo");
        String descripcion = rs.getString("descripcion");
        byte[] imagen = rs.getBytes("imagen");
        int publicadorId = rs.getInt("usuario_id");

        // Crear la publicación específica según la categoría
        return switch (categoria) {
            case "Tecnología" -> new PublicacionTecnologia(
                    titulo, descripcion, imagen, publicadorId,
                    rs.getString("modelo"), rs.getString("marca"), rs.getBoolean("garantia")
            );
            case "Ropa" -> new PublicacionRopa(
                    titulo, descripcion, imagen, publicadorId,
                    rs.getFloat("talla"), rs.getString("material")
            );
            case "Hogar" -> new PublicacionHogar(
                    titulo, descripcion, imagen, publicadorId,
                    rs.getString("tipo_mueble")
            );
            default -> throw new IllegalArgumentException("Categoría no soportada: " + categoria);
        };
    }

    private int obtenerIdPublicacion(Publicacion publicacion) {
        // Necesitarías agregar un campo ID a las clases Publicacion
        // Por ahora, esto es un placeholder
        return publicacion.hashCode();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}