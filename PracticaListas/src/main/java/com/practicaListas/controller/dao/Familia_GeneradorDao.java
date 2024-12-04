package com.practicaListas.controller.dao;

import com.practicaListas.controller.dao.implement.AdapterDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia_Generador;
import com.practicaListas.models.enums.Uso;

public class Familia_GeneradorDao extends AdapterDao<Familia_Generador>{
    private Familia_Generador familia_Generador;
    private LinkedList<Familia_Generador> listAll;

    public Familia_GeneradorDao() {
        super(Familia_Generador.class);
    }

    public Familia_Generador getFamilia_Generador() {
        if (familia_Generador == null) {
            familia_Generador = new Familia_Generador();
        }
        return familia_Generador;
    }

    public void setFamilia_Generador(Familia_Generador familia_Generador) {
        this.familia_Generador = familia_Generador;
    }

    public LinkedList<Familia_Generador> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        familia_Generador.setId(id);
        this.persist(this.familia_Generador);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getFamilia_Generador(), getFamilia_Generador().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean deleteByIndex(Integer id) throws Exception {
        this.delete(id);
        LinkedList<Familia_Generador> list = listAll();
        for (int i = 0; i < list.getSize(); i++) {
            list.get(i).setId(i + 1);
        }
        updateListFile(list);
        this.listAll = list;
        return true;
    }    

    public Familia_Generador getByIndex(Integer index) throws Exception {
        return get(index);
    }

    public String getJsonByIndex(Integer index) throws Exception {
        return g.toJson(getByIndex(index));
    }

    public void setListALlG(LinkedList<Familia_Generador> listAll) throws Exception {
        this.listAll = listAll;
    }

    public Uso[] getUsos() {
        return Uso.values();
    }

    public Uso getUso(String uso) {
        return Uso.valueOf(uso);
    }

    //Metodos de ordenacion:
    public LinkedList<Familia_Generador> order(String atribute, Integer type) throws Exception {
        LinkedList<Familia_Generador> listita = listAll();
        if (!listita.isEmpty()) {
            // Llamamos al método genérico `order` de la LinkedList
            listita = listita.order(atribute, type);
        }
        return listita;
    }
}
