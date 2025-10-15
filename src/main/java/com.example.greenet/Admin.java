package com.example.padron_decorador_modificado;

public class Admin implements Suscribe {
    private String nombre;
    private String correo;
    private long telefono;


    public Admin(String nombre, String correo, long telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono=telefono;
    }

    @Override
    public void actualizar(String mensaje) {
        Notificacion notificacion = new NotificacionBase();
        Notificacion notificacionWhatsApp = new WhatsAppDecorador(notificacion);
        Notificacion notificacionGmail = new GmailDecorador(notificacionWhatsApp);
        notificacionGmail.sendMessage(mensaje,correo,telefono);

    }
}

