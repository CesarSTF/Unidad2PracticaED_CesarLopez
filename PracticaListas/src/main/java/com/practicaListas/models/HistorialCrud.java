package com.practicaListas.models;

import com.practicaListas.models.enums.TipoDeCrud;

public class HistorialCrud {
    private Integer id;
    private TipoDeCrud tipoDeCrud;
    private String horaFecha;
    private String mensaje;

    public HistorialCrud() {
    }

    public HistorialCrud(TipoDeCrud tipoDeCrud, String horaFecha, String mensaje) {
        this.tipoDeCrud = tipoDeCrud;
        this.horaFecha = horaFecha;
        this.mensaje = mensaje;
    }

    public TipoDeCrud getTipoDeCrud() {
        return tipoDeCrud;
    }

    public void setTipoDeCrud(TipoDeCrud tipoDeCrud) {
        this.tipoDeCrud = tipoDeCrud;
    }

    public String getHoraFecha() {
        return horaFecha;
    }

    public void setHoraFecha(String horaFecha) {
        this.horaFecha = horaFecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
