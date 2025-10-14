package com.example.padron_decorador_modificado;

import java.util.ArrayList;
import java.util.List;

public abstract class Publisher {
    protected List<Suscribe> suscriptores = new ArrayList<>();

    public void suscribir(Suscribe s) {
        suscriptores.add(s);
    }

    public void eliminar(Suscribe s) {
        suscriptores.remove(s);
    }

    protected void notificarSubs(String mensaje) {
        for (Suscribe s : suscriptores) {
            s.actualizar(mensaje);
        }
    }
}
