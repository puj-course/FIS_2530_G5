package com.example.padron_decorador_modificado;

public class Admin implements Suscribe {
    private String nombre;
    private String correo;


    public Admin(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    @Override
    public void actualizar(String mensaje) {
        Notificacion notificacion = new NotificacionBase();
        Notificacion notificacionWhatsApp = new WhatsAppDecorador(notificacion);
        Notificacion notificacionGmail = new GmailDecorador(notificacionWhatsApp);
        notificacionGmail.sendMessage(mensaje,correo);

    }
}
