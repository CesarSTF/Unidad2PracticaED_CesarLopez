package com.practicaListas.models;

public class Familia {
    private Integer id;
    private String codigoFamilia;
    private Integer nroIntegrantes;
    private String nombreFamilia;
    private float saldo;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoFamilia() {
        return this.codigoFamilia;
    }

    public void setCodigoFamilia(String codigoFamilia) {
        this.codigoFamilia = codigoFamilia;
    }

    public Integer getNroIntegrantes() {
        return this.nroIntegrantes;
    }

    public void setNroIntegrantes(Integer nroIntegrantes) {
        this.nroIntegrantes = nroIntegrantes;
    }

    public String getNombreFamilia() {
        return this.nombreFamilia;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }


    public float getSaldo() {
        return this.saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nroIntegrantes='" + getNroIntegrantes() + "'" +
            ", nombreFamilia='" + getNombreFamilia() + "'" +
            ", saldo='" + getSaldo() + "'" +
            "}";
    }
}
