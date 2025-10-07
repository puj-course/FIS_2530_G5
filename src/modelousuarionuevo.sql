CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE ROLES(
     id SERIAL primary key
     ,nombre text
);

CREATE TABLE tipodoc(
     id SERIAL primary key
     ,documento text
);

CREATE TABLE usuarios(
     id SERIAL primary key not null
     ,nombre text not null
     ,apellidos text not null
     ,fechaNacimiento Date not null
     ,tipo_id int references tipodoc(id) on delete cascade on update cascade not null
     ,numero_doc bytea not null
     ,correo text not null unique
     ,contrasena bytea not null
     ,rol_id int references roles (id) on delete cascade on update cascade not null
     ,fechaCreacion timestamp not null
     ,telefono VARCHAR(15)
     ,direccion TEXT
     ,estado int check (estado IN (1,2,3)) 
);


CREATE TABLE publicaciones (
    id SERIAL PRIMARY KEY NOT NULL,
    titulo TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    categoria TEXT NOT NULL,
    imagen text  NOT NULL, -- se guarda el archivo como bytes
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publicador_id INT REFERENCES usuarios(id) ON DELETE CASCADE NOT NULL,
    estados INT NOT NULL CHECK (estados IN (1, 2, 3))
    -- 1 = Disponible, 2 = Borrado, 3 = Intercambiado
    -- categorias tecnologia , reci
);


CREATE TABLE sesiones(
     id SERIAL primary key not null
     ,id_usuario int references usuarios (id) not null
     ,fecha timestamp not null
     ,estado int check (estado IN (1,2))
);




-- Función registrar_usuario (versión que retorna int)
CREATE OR REPLACE FUNCTION registrar_usuario(
    p_nombre text,
    p_apellidos text,
    p_fechanacimiento text,
    p_tipo_doc text,
    p_numero_doc text,
    p_correo text,
    p_contrasena text,
    p_rol text,
    p_telefono VARCHAR DEFAULT NULL,
    p_direccion TEXT DEFAULT NULL
) returns int as $$
declare
    v_contrasena bytea;
    v_tipocuenta int;
    v_tipo_doc int;
    v_numero_doc bytea;
    v_fecha_nacimiento DATE;
begin
    v_fecha_nacimiento := TO_DATE(p_fechanacimiento, 'YYYY-MM-DD');
    
    if (p_correo !~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$') then 
        return 1;
    end if;

    if (p_rol = 'administrador') then 
        v_tipocuenta := 2; 
    elsif (p_rol = 'usuario') then 
        v_tipocuenta := 1;
    else
        return 3;
    end if;

    if (p_tipo_doc = 'CC') then v_tipo_doc := 1; 
    elsif (p_tipo_doc = 'TI') then v_tipo_doc := 2;
    elsif (p_tipo_doc = 'CE') then v_tipo_doc := 3;
    elsif (p_tipo_doc = 'PASAPORTE') then v_tipo_doc := 4;
    else
        return 4;
    end if;

    if exists (select 1 from usuarios where correo = p_correo) then
        return 2;
    end if;

    v_contrasena := digest(convert_to(p_contrasena, 'UTF8'), 'sha256');
    v_numero_doc := digest(convert_to(p_numero_doc, 'UTF8'), 'sha256');
    
    INSERT INTO usuarios(nombre, apellidos, fechanacimiento, tipo_id, numero_doc, 
                        correo, contrasena, rol_id, fechacreacion, estado, 
                        telefono, direccion)
    VALUES (p_nombre, p_apellidos, v_fecha_nacimiento, v_tipo_doc, v_numero_doc, 
            p_correo, v_contrasena, v_tipocuenta, NOW(), 1,
            p_telefono, p_direccion);

    return 0;
end;
$$ language plpgsql;

-- Función iniciar_sesion (versión que retorna int)
CREATE OR REPLACE FUNCTION iniciar_sesion(p_correo text, p_contrasena text) 
RETURNS INT AS $$
DECLARE
    v_contrasenaalmacenada bytea;
    v_usuario_id INT;
BEGIN
    -- Verificar si el usuario existe
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = p_correo) THEN 
        RETURN 1; -- Usuario no existe
    END IF;

    -- Encriptar contrasena proporcionada
    v_contrasenaalmacenada := digest(convert_to(p_contrasena, 'UTF8'), 'sha256');

    -- Verificar credenciales
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = p_correo AND "contrasena" = v_contrasenaalmacenada) THEN 
        RETURN 2; -- contrasena incorrecta
    END IF;

    -- Obtener ID del usuario
    SELECT id INTO v_usuario_id 
    FROM usuarios 
    WHERE correo = p_correo AND "contrasena" = v_contrasenaalmacenada;

    -- Registrar sesion
    INSERT INTO sesiones(id_usuario, fecha, estado) VALUES (v_usuario_id, NOW(), 1);
    
    RETURN 0; -- Exito
END;
$$ LANGUAGE plpgsql;

-- Función cerrar_sesion (versión que retorna int)
create or replace function cerrar_sesion(p_usuario_id int) returns int as $$
begin
    if not exists (select 1 from sesiones where id_usuario = p_usuario_id and estado = 1) then
        return 1; -- No hay sesión activa
    end if;

    update sesiones set estado = 2 
    where id_usuario = p_usuario_id and estado = 1;

    return 0; -- Sesión cerrada exitosamente
end;
$$ language plpgsql;
-- Datos iniciales
insert into roles(nombre) values ('usuario');
insert into roles(nombre) values ('administrador');

insert into tipodoc(documento) values ('CC');         
insert into tipodoc(documento) values ('TI');         
insert into tipodoc(documento) values ('CE');         
insert into tipodoc(documento) values ('PASAPORTE');  


-- PARA OBTENER EL LISTADO DE USUARIOS REPORTADOS
select id, nombre || ' ' || apellidos as nombre from usuarios where estado = 2;
-- PARA BLOQUEAR USUARIOS REPORTADOS
create or replace function bloquear_usuario(p_usuario_id int) 
returns boolean as $$
begin
    if not exists (
        select 1 from usuarios where id = p_usuario_id and estado = 2
    ) then
        return false;
    end if;

    update usuarios set estado = 3 where id = p_usuario_id and estado = 2;
    return true;
end;
$$ language plpgsql

CREATE OR REPLACE FUNCTION actualizar_correo_usuario(
    p_usuario_id INT,
    p_nuevo_correo TEXT
) RETURNS INT AS $$
DECLARE
    v_correo_actual TEXT;
BEGIN
    -- Primero obtener el correo actual del usuario
    SELECT correo INTO v_correo_actual 
    FROM usuarios 
    WHERE id = p_usuario_id;

    -- Si el nuevo correo es igual al actual, permitirlo
    IF v_correo_actual = p_nuevo_correo THEN
        RETURN 0; -- Éxito (es el mismo correo)
    END IF;

    -- Validar formato de correo (solo si es diferente)
    IF (p_nuevo_correo !~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$') THEN 
        RETURN 1; -- Correo inválido
    END IF;

    -- Verificar si el nuevo correo ya existe en otro usuario
    IF EXISTS (
        SELECT 1 FROM usuarios 
        WHERE correo = p_nuevo_correo 
        AND id != p_usuario_id
        AND estado = 1 -- Solo usuarios activos
    ) THEN
        RETURN 2; -- Correo ya en uso por otro usuario
    END IF;

    -- Actualizar el correo
    UPDATE usuarios 
    SET correo = p_nuevo_correo 
    WHERE id = p_usuario_id;

    -- Verificar si se actualizó
    IF NOT FOUND THEN
        RETURN 3; -- Usuario no encontrado
    END IF;

    RETURN 0; -- Éxito
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION registrar_material(
    p_titulo TEXT,
    p_descripcion TEXT,
    p_categoria TEXT,
    p_imagen TEXT,
    p_publicador_id INT
) RETURNS INT AS $$
DECLARE
    v_usuario_exists INT;
BEGIN
    -- Validar que el publicador exista
    SELECT COUNT(*) INTO v_usuario_exists
    FROM usuarios
    WHERE id = p_publicador_id;

    IF v_usuario_exists = 0 THEN
        RETURN 2; -- Usuario no existe
    END IF;

    -- Insertar material con estado inicial = 1 (Disponible)
    INSERT INTO materiales(
        titulo,
        descripcion,
        categoria,
        imagen,
        fecha_publicacion,
        publicador_id,
        estados
    )
    VALUES (
        p_titulo,
        p_descripcion,
        p_categoria,
        p_imagen,
        CURRENT_TIMESTAMP,
        p_publicador_id,
        1 -- Disponible
    );

    RETURN 0; -- Éxito
END;
$$ LANGUAGE plpgsql;
