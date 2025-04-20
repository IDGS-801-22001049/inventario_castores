package com.castores.utilidades;

public class ExcepcionInventario extends RuntimeException {
    public ExcepcionInventario(String mensaje) {
        super(mensaje);
    }
}
