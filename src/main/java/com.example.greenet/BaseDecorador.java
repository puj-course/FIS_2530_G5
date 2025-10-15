package com.example.padron_decorador_modificado;

public abstract class BaseDecorador implements Notificacion {
    protected Notificacion wrappe;

    public BaseDecorador(Notificacion notifier) {
        this.wrappe = notifier;
    }

    @Override
    public void sendMessage(String message,String correo, long  telefono) {
        if (wrappe != null) {
            wrappe.sendMessage(message,correo,telefono);
        }
    }

    @Override
    public void operation() {
        if (wrappe != null) {
            wrappe.operation();
        }
    }
}
