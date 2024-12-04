package com.practicaListas.controller.dao.services;

import com.practicaListas.controller.dao.GeneradorDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Generador;
import com.practicaListas.models.enums.Uso;

public class GeneradorServices {
    private GeneradorDao obj;

    public GeneradorServices(){
        this.obj = new GeneradorDao();
    }

    public Boolean save()throws Exception{
        return obj.save();
    }

    public LinkedList listAll()throws Exception{
        return obj.getListAll();
    }

    public Generador getGenerador(){
        return obj.getGenerador();
    }

    public void setGenerador(Generador generador){
        obj.setGenerador(generador);
    }

    public Generador getGeneradorByIndex(Integer index)throws Exception{
        return obj.getGeneradorByIndex(index);
    }

    public String getGeneradorJsonByIndex(Integer index)throws Exception{
        return obj.getGeneradorJsonByIndex(index);
    }

    public Boolean update()throws Exception{
        return obj.update();
    }

    public Boolean delete(Integer index) throws Exception {
        return obj.deleteGeneradorByIndex(index);
    }

    public Uso[] getUso(){
        return Uso.values();
    }

    public Uso getUso(String uso){
        return Uso.valueOf(uso);
    }

    public Generador get(Integer id) throws Exception {
        return obj.get(id);
    }

    public LinkedList<Generador> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public LinkedList<Generador> ordenarPor(String atribute, Integer type, String metodo) throws Exception {
        return obj.ordenarPor(atribute, type, metodo);
    }

    public Generador binarySearch(String attribute, Object value) throws Exception {
        return obj.binarySearch(attribute, value);
    }
    
    public LinkedList<Generador> binarySearchLineal(String attribute, Object value) throws Exception {
        return obj.binarySearchLineal(attribute, value);
    }

    public String codigoG(String input){
        return obj.codigoG(input);
    }
}
