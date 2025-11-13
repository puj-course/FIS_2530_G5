package com.greenet.service;

import com.greenet.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    @Test
    void testRegistrarUsuario_CorreoInvalido() {
        int resultado = UsuarioService.registrarUsuario(
            "Juan", "Pérez", "1990-01-01", "CC", "123456",
            "correo_invalido", "password", "usuario", "123456", "Calle 123"
        );
        assertEquals(1, resultado);
    }

    @Test
    void testRegistrarUsuario_RolInvalido() {
        int resultado = UsuarioService.registrarUsuario(
            "Juan", "Pérez", "1990-01-01", "CC", "123456",
            "test@test.com", "password", "rol_invalido", "123456", "Calle 123"
        );
        assertEquals(3, resultado);
    }

    @Test
    void testRegistrarUsuario_TipoDocumentoInvalido() {
        int resultado = UsuarioService.registrarUsuario(
            "Juan", "Pérez", "1990-01-01", "INVALIDO", "123456",
            "test@test.com", "password", "usuario", "123456", "Calle 123"
        );
        assertEquals(4, resultado);
    }

    @Test
    void testConsultarProductosDisponibles() {
        List<Publicacion> productos = UsuarioService.ConsultarProductosDisponibles();
        assertNotNull(productos);
    }

    @Test
    void testObtenerUsuariosRestringidos() {
        List<String> usuarios = UsuarioService.obtenerUsuariosRestringidos();
        assertNotNull(usuarios);
    }

    @Test
    void testUsuariosBloqueados() {
        int count = UsuarioService.usuarios_bloqueados();
        assertTrue(count >= 0);
    }

    @Test
    void testBuscarId_CorreoNulo() {
        int resultado = UsuarioService.BuscarId(null);
        assertEquals(-1, resultado);
    }

    @Test
    void testBuscarPublicadorId_TituloNulo() {
        Integer resultado = UsuarioService.BuscarPublicadorId(null);
        assertNull(resultado);
    }

    @Test
    void testBuscarDescripcion_TituloNulo() {
        String resultado = UsuarioService.BuscarDescripcion(null);
        assertNull(resultado);
    }

    @Test
    void testBuscarNombrePorId_IdInvalido() {
        String resultado = UsuarioService.BuscarNombrePorId(-1);
        assertNull(resultado);
    }

    @Test
    void testVerificarAdmin_IdInvalido() {
        boolean resultado = UsuarioService.VerificarAdmin(-1);
        assertFalse(resultado);
    }

    @Test
    void testBloquearUsuario_IdInvalido() {
        boolean resultado = UsuarioService.bloquearUsuario(-1);
        assertFalse(resultado);
    }

    @Test
    void testEmailRegexValidos() {
        String emailRegex = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        String[] correosValidos = {"test@test.com", "user.name@domain.co", "user123@test.org"};
        for (String correo : correosValidos) {
            assertTrue(correo.matches(emailRegex));
        }
    }

    @Test
    void testEmailRegexInvalidos() {
        String emailRegex = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        String[] correosInvalidos = {"correo_invalido", "test@", "@domain.com"};
        for (String correo : correosInvalidos) {
            assertFalse(correo.matches(emailRegex));
        }
    }

    @Test
    void testMapeoRolesValidos() {
        String[] rolesValidos = {"usuario", "administrador", "USUARIO", "ADMINISTRADOR"};
        for (String rol : rolesValidos) {
            int resultado = UsuarioService.registrarUsuario("Test", "User", "1990-01-01", "CC", "123456",
                "test@test.com", "password", rol, "123456", "Calle 123");
            assertNotEquals(3, resultado);
        }
    }

    @Test
    void testMapeoRolesInvalidos() {
        String[] rolesInvalidos = {"rol_invalido", "admin", "user", ""};
        for (String rol : rolesInvalidos) {
            int resultado = UsuarioService.registrarUsuario("Test", "User", "1990-01-01", "CC", "123456",
                "test@test.com", "password", rol, "123456", "Calle 123");
            assertEquals(3, resultado);
        }
    }

    @Test
    void testMapeoTiposDocumentoValidos() {
        String[] tiposValidos = {"CC", "TI", "CE", "PASAPORTE"};
        for (String tipo : tiposValidos) {
            int resultado = UsuarioService.registrarUsuario("Test", "User", "1990-01-01", tipo, "123456",
                "test@test.com", "password", "usuario", "123456", "Calle 123");
            assertNotEquals(4, resultado);
        }
    }

    @Test
    void testMapeoTiposDocumentoInvalidos() {
        String[] tiposInvalidos = {"INVALIDO", "DNI", "RUT", ""};
        for (String tipo : tiposInvalidos) {
            int resultado = UsuarioService.registrarUsuario("Test", "User", "1990-01-01", tipo, "123456",
                "test@test.com", "password", "usuario", "123456", "Calle 123");
            assertEquals(4, resultado);
        }
    }

    @Test
    void testIniciarSesion_CredencialesValidas() {
        assertDoesNotThrow(() -> UsuarioService.iniciarSesion("test@test.com", "password"));
    }

    @Test
    void testCerrarSesion_IdValido() {
        assertDoesNotThrow(() -> UsuarioService.cerrarSesion(999));
    }

    @Test
    void testActualizarCorreo_ValidacionFormato() {
        assertDoesNotThrow(() -> UsuarioService.actualizarCorreo(999, "correo_invalido"));
    }

    @Test
    void testConsultarProductosDisponibles_CubreSwitch() {
        assertDoesNotThrow(() -> UsuarioService.ConsultarProductosDisponibles());
    }

    @Test
    void testActualizarCorreo_MismoCorreo() {
        assertDoesNotThrow(() -> UsuarioService.actualizarCorreo(1, "test@test.com"));
    }

    @Test
    void testRegistrarUsuario_InsercionExitosa() {
        assertDoesNotThrow(() -> UsuarioService.registrarUsuario("Usuario", "Test", "2000-01-01", "CC", "123456789",
            "nuevo_usuario@test.com", "Password123", "usuario", "3001234567", "Calle Test"));
    }

    @Test
    void testRegistrarUsuario_DiferentesRoles() {
        String[] roles = {"usuario", "administrador"};
        for (String rol : roles) {
            assertDoesNotThrow(() -> UsuarioService.registrarUsuario("Role", "Test", "1990-01-01", "CC", "111111",
                "role_" + rol + "@test.com", "pass", rol, "111111", "Address"));
        }
    }

    @Test
    void testRegistrarUsuario_DiferentesTiposDoc() {
        String[] tipos = {"CC", "TI", "CE", "PASAPORTE"};
        for (String tipo : tipos) {
            assertDoesNotThrow(() -> UsuarioService.registrarUsuario("DocType", "Test", "1990-01-01", tipo, "222222",
                "doc_" + tipo.toLowerCase() + "@test.com", "pass", "usuario", "222222", "Addr"));
        }
    }

    @Test
    void testTryCatchCoverage() {
        assertDoesNotThrow(() -> {
            UsuarioService.BuscarId("test@test.com");
            UsuarioService.BuscarPublicadorId("Test Title");
            UsuarioService.BuscarDescripcion("Test Title");
            UsuarioService.BuscarNombrePorId(1);
            UsuarioService.VerificarAdmin(1);
            UsuarioService.bloquearUsuario(1);
        });
    }

    @Test
    void testRegistrarUsuario_CorreoDuplicado() {
        String correoUnico = "duplicado_" + System.currentTimeMillis() + "@test.com";
        int resultado1 = UsuarioService.registrarUsuario("Usuario1", "Test", "1990-01-01", "CC", "111111",
            correoUnico, "password", "usuario", "123456", "Calle 123");
        assertEquals(0, resultado1);

        int resultado2 = UsuarioService.registrarUsuario("Usuario2", "Test", "1990-01-01", "CC", "222222",
            correoUnico, "password", "usuario", "123456", "Calle 123");
        assertEquals(2, resultado2);
    }

    @Test
    void testIniciarSesion_UsuarioNoExiste() {
        int resultado = UsuarioService.iniciarSesion("noexiste_" + System.currentTimeMillis() + "@test.com", "password");
        assertEquals(1, resultado);
    }

    @Test
    void testIniciarSesion_ContrasenaIncorrecta() {
        String correo = "testcontrasena_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Test", "Contraseña", "1990-01-01", "CC", "333333",
            correo, "passwordCorrecta", "usuario", "123456", "Calle Test");

        int resultado = UsuarioService.iniciarSesion(correo, "passwordIncorrecta");
        assertEquals(2, resultado);
    }

    @Test
    void testIniciarSesion_Exitoso() {
        String correo = "success_" + System.currentTimeMillis() + "@test.com";
        String password = "miPassword123";
        UsuarioService.registrarUsuario("Success", "Login", "1990-01-01", "CC", "444444",
            correo, password, "usuario", "123456", "Calle Success");

        int resultado = UsuarioService.iniciarSesion(correo, password);
        assertEquals(0, resultado);
    }

    @Test
    void testCerrarSesion_Exitoso() {
        String correo = "logout_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Logout", "Test", "1990-01-01", "CC", "555555",
            correo, "password", "usuario", "123456", "Calle Logout");

        int usuarioId = UsuarioService.BuscarId(correo);
        UsuarioService.iniciarSesion(correo, "password");
        int resultado = UsuarioService.cerrarSesion(usuarioId);
        assertEquals(0, resultado);
    }

    @Test
    void testCerrarSesion_NoSesionActiva() {
        int resultado = UsuarioService.cerrarSesion(999999);
        assertEquals(1, resultado);
    }

    @Test
    void testActualizarCorreo_CorreoYaExiste() {
        String correo1 = "existente1_" + System.currentTimeMillis() + "@test.com";
        String correo2 = "existente2_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("User1", "Test", "1990-01-01", "CC", "666666", correo1, "pass", "usuario", "123", "Addr1");
        UsuarioService.registrarUsuario("User2", "Test", "1990-01-01", "CC", "777777", correo2, "pass", "usuario", "123", "Addr2");

        int usuarioId1 = UsuarioService.BuscarId(correo1);
        int resultado = UsuarioService.actualizarCorreo(usuarioId1, correo2);
        assertEquals(2, resultado);
    }

    @Test
    void testActualizarCorreo_UsuarioNoEncontrado() {
        int resultado = UsuarioService.actualizarCorreo(-999999, "nuevo@test.com");
        assertEquals(3, resultado);
    }

    @Test
    void testActualizarCorreo_Exitoso() {
        String correoOriginal = "original_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Update", "Test", "1990-01-01", "CC", "888888", correoOriginal, "pass", "usuario", "123", "Addr");

        int usuarioId = UsuarioService.BuscarId(correoOriginal);
        String nuevoCorreo = "nuevo_" + System.currentTimeMillis() + "@test.com";
        int resultado = UsuarioService.actualizarCorreo(usuarioId, nuevoCorreo);
        assertEquals(0, resultado);
    }

    @Test
    void testBloquearUsuario_Exitoso() {
        String correo = "bloquear_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Bloque", "Test", "1990-01-01", "CC", "101010", correo, "pass", "usuario", "123", "Addr");

        int usuarioId = UsuarioService.BuscarId(correo);
        boolean resultado = UsuarioService.bloquearUsuario(usuarioId);
        assertTrue(resultado);
    }

    @Test
    void testVerificarAdmin_True() {
        String correoAdmin = "admin_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Admin", "Test", "1990-01-01", "CC", "121212", correoAdmin, "pass", "administrador", "123", "Addr");

        int adminId = UsuarioService.BuscarId(correoAdmin);
        boolean resultado = UsuarioService.VerificarAdmin(adminId);
        assertTrue(resultado);
    }

    @Test
    void testVerificarAdmin_False() {
        String correoUser = "user_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("User", "Normal", "1990-01-01", "CC", "131313", correoUser, "pass", "usuario", "123", "Addr");

        int userId = UsuarioService.BuscarId(correoUser);
        boolean resultado = UsuarioService.VerificarAdmin(userId);
        assertFalse(resultado);
    }

    @Test
    void testBuscarId_UsuarioExiste() {
        String correo = "buscar_" + System.currentTimeMillis() + "@test.com";
        UsuarioService.registrarUsuario("Buscar", "Test", "1990-01-01", "CC", "141414", correo, "pass", "usuario", "123", "Addr");

        int usuarioId = UsuarioService.BuscarId(correo);
        assertTrue(usuarioId > 0);
    }

    @Test
    void testObtenerAdmins() {
        List<Admin> admins = UsuarioService.obtenerAdmins();
        assertNotNull(admins);
    }
}
