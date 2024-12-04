package com.practicaListas.controller.dao;

import com.google.gson.Gson;
import com.practicaListas.controller.dao.implement.AdapterDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia;

public class FamiliaDao extends AdapterDao<Familia>{
    private Familia familia;
    private LinkedList<Familia> listAll;
    private Gson g = new Gson();

    public FamiliaDao(){
        super(Familia.class);
    }

    public Familia getFamilia(){
        if (familia == null) {
            familia = new Familia();
        }
        return familia;
    }

    public void setFamilia(Familia familia){
        this.familia = familia;
    }

    public LinkedList<Familia> getListAll()throws Exception{
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll; 
    }

    public Boolean save()throws Exception{
        Integer id = listAll().getSize()+1;
        familia.setId(id);
        this.persist(this.familia);
        this.listAll = listAll();
        return true;
    }

    public Familia getFamiliaByIndex(Integer index)throws Exception{
        return get(index);
    }

    public String getFamiliaJsonByIndex(Integer index)throws Exception{
        return g.toJson(getFamiliaByIndex(index));
    }

    public Boolean update()throws Exception{
        this.merge(getFamilia(), getFamilia().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean deleteFamiliaByIndex(Integer id)throws Exception{
        this.delete(id);
        LinkedList<Familia> list = listAll();
        for (int i = 0; i < list.getSize(); i++) {
            list.get(i).setId(i + 1);
        }
        updateListFile(list);
        this.listAll = list;
        return true;
    }

    //Metodo de ordenacion:
    public LinkedList<Familia> order(String atribute, Integer type) throws Exception {
        LinkedList<Familia> listita = listAll();
        if (!listita.isEmpty()) {
            // Llamamos al metodo order de la LinkedList
            listita = listita.order(atribute, type);
        }
        return listita;
    }

    public LinkedList<Familia> ordenarPor(String atribute, Integer type, String metodo)throws Exception{
        LinkedList<Familia> lista = listAll();
        switch (metodo) {
            case "quick":
                return lista.quickSort(atribute, type);
                
            case "shell":
                return lista.shellSort(atribute, type);
            
            case "merge":
                return lista.mergeSort(atribute, type);
            default:
                return lista;
        }
    }

    /////////////////////////////////////////////////////////
    public Familia binarySearch(String attribute, Object value) throws Exception {
        // lista ordenada por el atributo
        LinkedList<Familia> sortedList = ordenarPor(attribute, 1, "quick");
        //System.out.println("Lista ordenada por " + attribute + ": " + sortedList);
    
        int low = 0;
        int high = sortedList.getSize() - 1;
    
        while (low <= high) {
            int mid = (low + high) / 2;
            Familia midElement = sortedList.get(mid);
    
            Object midValue = getAttributeValue(midElement, attribute);
    
            //System.out.println("Comparando " + midValue + " con " + value);

            // asegurarse de que el valor es float
            if (attribute.equals("saldo")) {
                midValue = Float.parseFloat(midValue.toString());
                value = Float.parseFloat(value.toString());
            }
    

            int comparison = compareValues(midValue, value);
    
            if (comparison == 0) {
                return midElement; // elemento encontrado
            } else if (comparison < 0) {
                low = mid + 1; // mitad superior
            } else {
                high = mid - 1; // mitad inferior
            }
        }
    
        return null; // devolver null
    }

    private Object getAttributeValue(Familia familia, String attribute) throws NoSuchMethodException, IllegalAccessException{
        switch (attribute.toLowerCase()) {
            case "id":
                return familia.getId();
            case "codigofamilia":
                return familia.getCodigoFamilia();
            case "nombrefamilia":
                return familia.getNombreFamilia();
            case "nrointegrantes":
                return familia.getNroIntegrantes();
            case "saldo":
                return familia.getSaldo();
            default:
                throw new IllegalArgumentException("Atributo no valido: " + attribute);
        }
    }
    
    private int compareValues(Object value1, Object value2) {
        if (value1 instanceof Integer && value2 instanceof Integer) {
            return ((Integer) value1).compareTo((Integer) value2);
        } else if (value1 instanceof Float && value2 instanceof Float) {
            return ((Float) value1).compareTo((Float) value2);
        } else if (value1 instanceof String && value2 instanceof String) {
            return ((String) value1.toString().toLowerCase()).compareTo((String) value2.toString().toLowerCase());
        }
        throw new IllegalArgumentException("Los valores no son comparables");
    }

    public LinkedList<Familia> binarySearchLineal(String attribute, Object value) throws Exception {
        LinkedList<Familia> sortedList = ordenarPor(attribute, 1, "quick");
        
        int low = 0;
        int high = sortedList.getSize() - 1;
        LinkedList<Familia> matches = new LinkedList<>();  // almacenar las coincidencias 
        
        while (low <= high) {
            int mid = (low + high) / 2;
            Familia midElement = sortedList.get(mid);
            Object midValue = getAttributeValue(midElement, attribute);
    
            // asegurarse de que el valor es float
            if (attribute.equals("saldo")) {
                midValue = Float.parseFloat(midValue.toString());
                value = Float.parseFloat(value.toString());
            }
    
            int comparison = compareValues(midValue, value);
    
            if (comparison == 0) {
                // agregamos a la lista de coincidencias
                matches.add(midElement);
    
                // buscar en ambas direcciones
                int left = mid - 1;
                int right = mid + 1;
    
                // izquierda
                while (left >= 0) {
                    Familia leftElement = sortedList.get(left);
                    Object leftValue = getAttributeValue(leftElement, attribute);
                    if (compareValues(leftValue, value) == 0) {
                        matches.add(leftElement);
                        left--;
                    } else {
                        break;
                    }
                }
    
                // derecha
                while (right < sortedList.getSize()) {
                    Familia rightElement = sortedList.get(right);
                    Object rightValue = getAttributeValue(rightElement, attribute);
                    if (compareValues(rightValue, value) == 0) {
                        matches.add(rightElement);
                        right++;
                    } else {
                        break;
                    }
                }
    
                break;  
            } else if (comparison < 0) {
                low = mid + 1;  // buscar en mitad superior
            } else {            
                high = mid - 1;  // buscar en mitad inferior
            }
        }
    
        return matches;  // devolver lista de coincidencias 
    }

    public String codigoF(String input){
        int base = 0;
        for (char c : input.toCharArray()) {
            base += c; 
        }
        int randomNum = (int) (Math.random() * 100000);    
        String codigo = String.format("%010d", base * 100000 + randomNum);        
        return codigo;
    }
    
}
