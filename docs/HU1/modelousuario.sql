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
     ,contraseña bytea not null
     ,rol_id int references roles (id) on delete cascade on update cascade not null
     ,fechaCreacion timestamp not null
);

CREATE TABLE sesiones(
     id SERIAL primary key not null
     ,id_usuario int references usuarios (id) not null
     ,fecha timestamp not null
     ,estado int check (estado IN (1,2))
);

create or replace function crearCuenta(
    p_nombre text
    ,p_apellidos text
    ,p_fechanacimiento date
    ,p_tipo_doc text
    ,p_numero_doc text
    ,p_correo text
    ,p_contraseña text
    ,p_rol text) returns int as $$
declare
    v_contraseña bytea;
    v_tipocuenta int;
    v_tipo_doc int;
    v_numero_doc bytea;
begin
    if (p_correo !~ '.*@.*') then 
        return 1; -- correo inválido
    end if;

    if (p_rol = 'administrador') then 
        v_tipocuenta := 2; 
    elsif (p_rol = 'usuario') then 
        v_tipocuenta := 1;
    end if;

    if (p_tipo_doc = 'CC') then v_tipo_doc := 1; 
    elsif (p_tipo_doc = 'TI') then v_tipo_doc := 2;
    elsif (p_tipo_doc = 'CE') then v_tipo_doc := 3;
    elsif (p_tipo_doc = 'PASAPORTE') then v_tipo_doc := 4;   
    end if;

    if exists (select 1 from usuarios where correo = p_correo) then
        return 2; -- para java: correo ya existe
    end if;

    v_contraseña := digest(convert_to(p_contraseña, 'UTF8'), 'sha256');
    v_numero_doc := digest(convert_to(p_numero_doc, 'UTF8'), 'sha256');
    
    insert into usuarios(nombre, apellidos, fechanacimiento, tipo_id, numero_doc, correo, contraseña, rol_id, fechacreacion)
    values (p_nombre, p_apellidos, p_fechanacimiento, v_tipo_doc, v_numero_doc, p_correo, v_contraseña, v_tipocuenta, now());

    return 0; --Para java: el 0 es éxito
end;
$$ language plpgsql;

create or replace function iniciarSesion(p_correo text, p_contraseña text) returns int as $$
declare
    v_contraseñaalmacenada bytea;
    v_numerousuario int;
begin
    if not exists (select 1 from usuarios where correo = p_correo) then 
        return 1; -- Para java: El usuario no existe
    end if;

    v_contraseñaalmacenada := digest(convert_to(p_contraseña, 'UTF8'), 'sha256');

    if not exists (select 1 from usuarios where correo = p_correo and contraseña = v_contraseñaalmacenada) then 
        return 2; -- Para java: contraseña incorrecta
    end if;

    select id into v_numerousuario from usuarios where correo = p_correo and contraseña = v_contraseñaalmacenada;
    
    insert into sesiones(id_usuario, fecha, estado) values (v_numerousuario, now(), 1);
    return 0; -- sesión iniciada
end;
$$ language plpgsql;

create or replace function cerrarSesion(p_usuario_id int) returns int as $$
begin
    if not exists (select 1 from sesiones where id_usuario = p_usuario_id and estado = 1) then
        return 1; -- no hay sesión activa
    end if;

    update sesiones set estado = 2 where id_usuario = p_usuario_id and estado = 1;

    return 0; -- sesión cerrada
end;
$$ language plpgsql;

-- Datos iniciales
insert into roles(nombre) values ('usuario');
insert into roles(nombre) values ('administrador');

insert into tipodoc(documento) values ('CC');         
insert into tipodoc(documento) values ('TI');         
insert into tipodoc(documento) values ('CE');         
insert into tipodoc(documento) values ('PASAPORTE');  

-- PRUEBAS

-- crear cuenta exitosa
select crearCuenta('juan','pérez',1995-04-10,'CC','100200300','juan@example.com','secreto123','usuario');
-- esperado: 0

-- crear cuenta con correo inválido
select crearCuenta('maria','gómez','1998-07-21','TI','200300400','maria.example.com','clave123','usuario');
-- esperado: 1

-- crear cuenta con correo ya registrado
select crearCuenta('juan','pérez','1995-04-10','CC','100200300','juan@example.com','secreto123','usuario');
-- esperado: 2

-- iniciar sesión correcta
select iniciarSesion('juan@example.com','secreto123');
-- esperado: 0

-- usuario que no existe
select iniciarSesion('fantasma@example.com','x');

-- contraseña incorrecta
select iniciarSesion('juan@example.com','clave_mala');

-- cerrar sesión de juan (id_usuario debería ser 1)
select cerrarSesion(1);

-- intentar cerrar sesión de alguien sin sesión activa
select cerrarSesion(1);

