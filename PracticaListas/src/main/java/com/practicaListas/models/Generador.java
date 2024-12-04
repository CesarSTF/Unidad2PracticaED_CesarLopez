package com.practicaListas.models;

import com.practicaListas.models.enums.Uso;

public class Generador {
    private Integer id;
    public String codigoGenerador;
    private String modelo;
    private float costo;
    private float consumoComustible;
    private float enegeriaGenerada;
    private Uso uso;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idGenerador) {
        this.id = idGenerador;
    }

    public String getCodigoGenerador() {
        return this.codigoGenerador;
    }

    public void setCodigoGenerador(String codigoGenerador) {
        this.codigoGenerador = codigoGenerador;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public float getCosto() {
        return this.costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getConsumoComustible() {
        return this.consumoComustible;
    }

    public void setConsumoComustible(float consumoComustible) {
        this.consumoComustible = consumoComustible;
    }

    public float getEnegeriaGenerada() {
        return this.enegeriaGenerada;
    }

    public void setEnegeriaGenerada(float enegeriaGenerada) {
        this.enegeriaGenerada = enegeriaGenerada;
    }

    public Uso getUso() {
        return this.uso;
    }

    public void setUso(Uso uso) {
        this.uso = uso;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", modelo='" + getModelo() + "'" +
            " codigoGerador= " + getCodigoGenerador() + "'" +
            ", costo='" + getCosto() + "'" +
            ", consumoComustible='" + getConsumoComustible() + "'" +
            ", enegeriaGenerada='" + getEnegeriaGenerada() + "'" +
            ", uso='" + getUso() + "'" +
            "}";
    }
}
