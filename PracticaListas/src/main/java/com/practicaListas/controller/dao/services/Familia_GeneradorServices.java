package com.practicaListas.controller.dao.services;

import java.util.HashMap;

import com.practicaListas.controller.dao.Familia_GeneradorDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia;
import com.practicaListas.models.Familia_Generador;
import com.practicaListas.models.Generador;
import com.practicaListas.models.enums.Uso;

public class Familia_GeneradorServices {
    private Familia_GeneradorDao obj;

    public Object[] listShowAll() throws Exception {
        if (!obj.getListAll().isEmpty()) {
            Familia_Generador[] lista = (Familia_Generador[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                Familia p = new FamiliaServices().get(lista[i].getId_Familia());
                Generador m = new GeneradorServices().get(lista[i].getId_Generador());
                HashMap mapa = new HashMap<>();
                mapa.put("id", lista[i].getId());
                mapa.put("descripcion", lista[i].getDescripcion());
                mapa.put("persona", p);
                mapa.put("generador", m);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[] {};
    }

    public Familia_GeneradorServices() {
        this.obj = new Familia_GeneradorDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public Familia_Generador getFamilia_Generador() {
        return obj.getFamilia_Generador();
    }

    public void setFamilia_Generador(Familia_Generador familia_Generador) {
        obj.setFamilia_Generador(familia_Generador);
    }

    public Familia_Generador getByIndex(Integer index) throws Exception {
        return obj.getByIndex(index);
    }

    public String getJsonByIndex(Integer index) throws Exception {
        return obj.getJsonByIndex(index);
    }

    public void update() throws Exception {
        obj.update();
    }

    public Boolean delete(Integer index) throws Exception {
        return obj.deleteByIndex(index);
    }

    public Familia_Generador get(Integer id) throws Exception {
        return obj.get(id);
    }

    public LinkedList<Familia_Generador> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public Uso[] getUso(){
        return Uso.values();
    }

    public Uso getUsoTipo(String uso){
        return Uso.valueOf(uso);
    }
}
