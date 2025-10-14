package com.example.padron_decorador_modificado;

public class NotificacionBase implements Notificacion {
    @Override
    public void sendMessage(String message,String inden) {
      
    }

    @Override
    public void operation() {
        System.out.println("Operación base ejecutada");
    }
}
