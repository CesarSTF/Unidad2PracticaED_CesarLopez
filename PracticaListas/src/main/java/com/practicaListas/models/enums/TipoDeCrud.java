package com.practicaListas.models.enums;

public enum TipoDeCrud {
    CREATE("Crear"),
    READ("Leer"),
    UPDATE("Actualizar"),
    DELETE("Eliminar");

    private String modificacion;

    TipoDeCrud(String modificacion) {
        this.modificacion = modificacion;
    }

    public String getModificacion() { 
        return modificacion;
    }
}
