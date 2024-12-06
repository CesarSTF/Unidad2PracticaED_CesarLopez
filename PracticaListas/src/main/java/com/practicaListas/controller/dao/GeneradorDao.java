package com.practicaListas.controller.dao;

import java.lang.reflect.InvocationTargetException;

import com.google.gson.Gson;
import com.practicaListas.controller.dao.implement.AdapterDao;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Generador;
import com.practicaListas.models.enums.Uso;

public class GeneradorDao extends AdapterDao<Generador> {
    private Generador generador;
    private LinkedList<Generador> listAll;
    private Gson g = new Gson();

    public GeneradorDao() {
        super(Generador.class);
    }

    public Generador getGenerador() {
        if (generador == null) {
            generador = new Generador();
        }
        return generador;
    }

    public void setGenerador(Generador generador) {
        this.generador = generador;
    }

    public LinkedList<Generador> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        generador.setId(id);
        this.persist(this.generador);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getGenerador(), getGenerador().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean deleteGeneradorByIndex(Integer id) throws Exception {
        this.delete(id);
        LinkedList<Generador> list = listAll();
        for (int i = 0; i < list.getSize(); i++) {
            list.get(i).setId(i + 1);
        }
        updateListFile(list);
        this.listAll = list;
        return true;
    }

    public Generador getGeneradorByIndex(Integer index) throws Exception {
        return get(index);
    }

    public String getGeneradorJsonByIndex(Integer index) throws Exception {
        return g.toJson(getGeneradorByIndex(index));
    }

    public void setListALlG(LinkedList<Generador> listAll) throws Exception {
        this.listAll = listAll;
    }

    public Uso[] getUso() {
        return Uso.values();
    }

    public Uso getUso(String uso) {
        return Uso.valueOf(uso);
    }

    // Metodos de ordenacion:
    public LinkedList<Generador> order(String atribute, Integer type) throws Exception {
        LinkedList<Generador> listita = listAll();
        if (!listita.isEmpty()) {
            // Llamamos al metodo order de la LinkedList
            listita = listita.order(atribute, type);
        }
        return listita;
    }

    public LinkedList<Generador> ordenarPor(String atribute, Integer type, String metodo) throws Exception {
        LinkedList<Generador> lista = listAll();
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

    public Generador binarySearch(String attribute, Object valor) throws Exception {
        //lista ordenada por el atributo
        LinkedList<Generador> sortedList = ordenarPor(attribute, 1, "quick");
    
        int bajo = 0;
        int high = sortedList.getSize() - 1;
    
        while (bajo <= high) {
            int mid = (bajo + high) / 2;
            Generador midElement = sortedList.get(mid);
    
            Object valorMed = getAttributeValue(midElement, attribute);
    
            //System.out.println("Comparando " + valorMed + " con " + valor);

            // asegurarse de que el valor es float
            if (attribute.equals("consumoComustible") || attribute.equals("enegeriaGenerada")) {
                valorMed = Float.parseFloat(valorMed.toString());
                valor = Float.parseFloat(valor.toString());
            }
    

            int comparar = compareValues(valorMed, valor);
    
            if (comparar == 0) {
                return midElement; // elemento encontrado
            } else if (comparar < 0) {
                bajo = mid + 1; // mitad superior
            } else {
                high = mid - 1; // mitad inferior
            }
        }
    
        return null; // devolver null
    }

    private Object getAttributeValue(Generador generador, String attribute) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        switch (attribute.toLowerCase()) {
            case "id":
                return generador.getId();
            case "codigogenerador":
                return generador.getCodigoGenerador();
            case "modelo":
                return generador.getModelo();
            case "costo":
                return generador.getCosto();
            case "consumoComustible":
            //System.out.println("Buscando por consumoComustible: " + generador.getConsumoComustible());
                return generador.getConsumoComustible();
            case "enegeriaGenerada":
                return generador.getEnegeriaGenerada();
            case "uso":
                return generador.getUso();
            default:
                throw new IllegalArgumentException("Atributo no valido: " + attribute);
        }
    }
    
    private int compareValues(Object value1, Object value2) {
        //System.out.println("Comparando " + value1 + " con " + value2);
        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            return ((Comparable) value1.toString().toLowerCase()).compareTo(value2.toString().toLowerCase());
        }
        throw new IllegalArgumentException("Los valores no son comparables");
    }

    public LinkedList<Generador> binarySearchLineal(String attribute, Object valor) throws Exception {
        LinkedList<Generador> sortedList = ordenarPor(attribute, 1, "quick");
        
        int bajo = 0;
        int high = sortedList.getSize() - 1;
        LinkedList<Generador> matches = new LinkedList<>();  // almacenar las coincidencias 
        
        while (bajo <= high) {
            int mid = (bajo + high) / 2;
            Generador midElement = sortedList.get(mid);
            Object valorMed = getAttributeValue(midElement, attribute);
    
            // asegurarse de que el valor es float
            if (attribute.equals("consumoComustible") || attribute.equals("enegeriaGenerada")) {
                valorMed = Float.parseFloat(valorMed.toString());
                valor = Float.parseFloat(valor.toString());
            }
    
            int comparar = compareValues(valorMed, valor);
    
            if (comparar == 0) {
                // agregamos a la lista de cooncidencias
                matches.add(midElement);
    
                // buscar en ambas direcciones 
                int left = mid - 1;
                int right = mid + 1;
    
                // izquierda
                while (left >= 0) {
                    Generador leftElement = sortedList.get(left);
                    Object leftValue = getAttributeValue(leftElement, attribute);
                    if (compareValues(leftValue, valor) == 0) {
                        matches.add(leftElement);
                        left--;
                    } else {
                        break;
                    }
                }
    
                // derecha
                while (right < sortedList.getSize()) {
                    Generador rightElement = sortedList.get(right);
                    Object rightValue = getAttributeValue(rightElement, attribute);
                    if (compareValues(rightValue, valor) == 0) {
                        matches.add(rightElement);
                        right++;
                    } else {
                        break;
                    }
                }
    
                break;  
            } else if (comparar < 0) {
                bajo = mid + 1;  // mitad superiorz
            } else {
                high = mid - 1;  // mitad inferior
            }
        }
    
        return matches;  // devolver lista de coincidencias
    }

    public String codigoG(String input){
        int base = 0;
        for (char c : input.toCharArray()) {
            base += c; 
        }
        int randomNum = (int) (Math.random() * 100000);    
        String codigo = String.format("%010d", base * 100000 + randomNum);        
        return codigo;
    }
}
