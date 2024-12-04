package com.practicaListas.controller.dao.implement;

import com.practicaListas.controller.tda.list.LinkedList;

public interface InterfazDao <T>{
    public void persist(T object) throws Exception;
    public void merge(T object, Integer Index) throws Exception;
    @SuppressWarnings("rawtypes")
    public LinkedList listAll();
    public T get(Integer id) throws Exception;
    public void delete(Integer id) throws Exception;
}
