package com.practicaListas.models.enums;

public enum Uso {
    DOMESTICO("DOMESTICO"),
    INDUSTRIAL("INDUSTRIAL"),
    COMERCIAL("COMERCIAL");

    private String name;

    private Uso(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
