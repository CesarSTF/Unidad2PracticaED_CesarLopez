package com.practicaListas.controller.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.practicaListas.controller.dao.implement.AdapterDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.HistorialCrud;
import com.practicaListas.models.enums.TipoDeCrud;

public class HistorialCrudDao extends AdapterDao<HistorialCrud>{
    private HistorialCrud historialCrud;
    private HistorialCrud[] listAll;

    public HistorialCrudDao() {
        super(HistorialCrud.class); 
    }

    public HistorialCrud geHistorialCrud() {
        if (historialCrud == null) {
            historialCrud = new HistorialCrud();
        }
        return this.historialCrud;
    }

    public void setHistorialCrud(HistorialCrud historialCrud) {
        this.historialCrud = historialCrud; 
    }

    public HistorialCrud[] getAllHistorialCrud() {
        try {
            LinkedList<HistorialCrud> list = this.listAll();
            if (list.isEmpty()) {
                throw new Exception("Error: Sin eventos por el momento.");
            }
            return list.toTypedArray(new HistorialCrud[0]);
        } catch (Exception e) {
            System.err.println("Error al obtener el historial: " + e.getMessage());
            return new HistorialCrud[0];
        }
    }    
    
    public boolean save() throws Exception {
        Integer id = getAllHistorialCrud().length + 1;
        historialCrud.setId(id);
        this.persist(this.historialCrud);
        listAll = null;
        return true;
    }

    public boolean registraHistorial(TipoDeCrud tipoDeCrud, String mensaje) throws Exception {
        String horaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HistorialCrud evento = new HistorialCrud(tipoDeCrud, horaFecha, mensaje); 
        setHistorialCrud(evento);
        return save();
    }

    public String toJson() throws Exception {
        return g.toJson(this.historialCrud);
    }

    public HistorialCrud getHistorialCrudById(Integer id) throws Exception {
        return get(id);
    }

    public String getHistorialCrudJsonById(Integer id) throws Exception {
        return g.toJson(getHistorialCrudById(id));
    }

    public HistorialCrud[] getListAll() {
        return listAll;
    }

    public void setListAll(HistorialCrud[] listAll) {
        this.listAll = listAll;
    }

    public TipoDeCrud getTipoDeEventoCrud(String tipoDeEventoCrud) {
        return TipoDeCrud.valueOf(tipoDeEventoCrud);
    }

    public TipoDeCrud[] getTipoDeEventoCurd() {
        return TipoDeCrud.values();
    }
}
