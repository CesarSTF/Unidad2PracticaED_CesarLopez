package com.practicaListas.controller.dao.services;

import com.practicaListas.controller.dao.FamiliaDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia;
public class FamiliaServices {
    public FamiliaDao obj;

    public FamiliaServices() {
        this.obj = new FamiliaDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public Familia getFamilia() {
        return obj.getFamilia();
    }

    public void setFamilia(Familia familia) {
        obj.setFamilia(familia);
    }

    public Familia getFamiliaByIndex(Integer index) throws Exception {
        return obj.getFamiliaByIndex(index);
    }

    public String getFamiliaJsonByIndex(Integer index) throws Exception {
        return obj.getFamiliaJsonByIndex(index);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean deleteFamiliaByIndex(Integer id) throws Exception {
        return obj.deleteFamiliaByIndex(id);
    }

    public Familia get(Integer id) throws Exception {
        return obj.get(id);
    }

    public LinkedList<Familia> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public LinkedList<Familia> ordenarPor(String atribute, Integer type, String metodo)throws Exception{
        return obj.ordenarPor(atribute, type, metodo);
    }

    public Familia binarySearch(String attribute, Object value) throws Exception {
        return obj.binarySearch(attribute, value);
    }
    
    public LinkedList<Familia> binarySearchLineal(String attribute, Object value) throws Exception {
        return obj.binarySearchLineal(attribute, value);
    }

    public String codigoF(String input){
        return obj.codigoF(input);
    }
}
