package com.example.greenet;

public interface VisitorPublicaciones {
    void visitar(PublicacionHogar hogar);
    void visitar(PublicacionRopa ropa);
    void visitar(PublicacionTecnologia tecnologia);
}
