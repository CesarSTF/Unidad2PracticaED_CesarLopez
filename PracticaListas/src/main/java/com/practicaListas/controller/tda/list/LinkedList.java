package com.practicaListas.controller.tda.list;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.practicaListas.controller.excepcion.ListEmptyException;

public class LinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private Integer size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node<E> getHead() {
        return this.head;
    }

    public void setHead(Node<E> head) {
        this.head = head;
    }

    public Node<E> getTail() {
        return this.tail;
    }

    public void setTail(Node<E> tail) {
        this.tail = tail;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isEmpty() {
        return this.head == null || this.size == 0;
    }

    public void addHeader(E data) {
        Node<E> aux = new Node<>(data);
        if (isEmpty()) {
            head = aux;
            tail = head;
        } else {
            aux.setNext(head);
            head = aux;
        }
        size++;
    }

    private void addTail(E data) {
        Node<E> aux = new Node<>(data);
        if (isEmpty()) {
            head = aux;
            tail = head;
        } else {
            tail.setNext(aux);
            tail = aux;
        }
        size++;
    }

    public void add(E data) {
        addTail(data);
    }

    public void add(E data, Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
        System.out.println("Agregando en índice: " + index + ", data: " + data);

        if (index == 0) {
            addHeader(data);
        } else if (index.intValue() == size) {
            addTail(data);
        } else {
            Node<E> search = getNode(index - 1);
            System.out.println("Nodo anterior encontrado: " + search.getData());

            Node<E> aux = new Node<>(data);
            aux.setNext(search.getNext());
            search.setNext(aux);
            size++;
        }
    }

    private Node<E> getNode(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
        if (index == size - 1) {
            return tail;
        }
        Node<E> search = head;
        Integer count = 0;
        while (count < index) {
            search = search.getNext();
            count++;
        }
        return search;
    }

    public E get(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        return getNode(index).getData();
    }

    public void set(Integer index, E data) throws ListEmptyException, IndexOutOfBoundsException {
        getNode(index).setData(data);
    }

    public void reset() {
        head = null;
        tail = null;
        size = 0;
    }

    public E delete(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        } else if (index == 0) {
            return deleteHeader();
        } else if (index == size - 1) {
            return deleteTail();
        } else {
            Node<E> prevNode = getNode(index - 1);
            Node<E> actualNode = getNode(index);
            E element = prevNode.getData();
            Node<E> nextNode = actualNode.getNext();
            actualNode = null;
            prevNode.setNext(nextNode);
            size--;
            return element;
        }
    }

    public E deleteHeader() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else {
            E element = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (size == 1) {
                tail = null;
            }
            size--;
            return element;
        }
    }

    public E deleteTail() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else {
            E element = tail.getData();
            Node<E> aux = getNode(size - 2);
            if (aux == null) {
                head = null;
                tail = null;
                if (size == 2) {
                    tail = head;
                } else {
                    head = null;
                }
            } else {
                tail = null;
                tail = aux;
                tail.setNext(null);
            }
            size--;
            return element;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> search = head;
        while (search != null) {
            sb.append(search.getData());
            sb.append("\n");
            search = search.getNext();
        }
        return sb.toString();
    }

    public E[] toArray() {
        E[] matrix = null;
        if (!isEmpty()) {
            @SuppressWarnings("rawtypes")
            Class clazz = head.getData().getClass();
            matrix = (E[]) java.lang.reflect.Array.newInstance(clazz, size);
            Node<E> aux = head;
            for (int i = 0; i < size; i++) {
                matrix[i] = aux.getData();
                aux = aux.getNext();
            }
        }
        return matrix;
    }

    public void update(E data, Integer post) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else if (post < 0 || post >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        } else if (post == 0) {
            head.setData(data);
        } else if (post == size - 1) {
            tail.setData(data);
        } else {
            Node<E> search = head;
            Integer count = 0;
            while (count < post) {
                count++;
                search = search.getNext();
            }
            search.setData(data);
        }
    }

    public LinkedList<E> toList(E[] matrix) {
        reset();
        for (int i = 0; i < matrix.length; i++) {
            add(matrix[i]);
        }
        return this;
    }

    private Boolean compare(Object a, Object b, Integer type) {
        if (a == null && b == null) {
            return false;
        }
        if (a == null) {
            return type == 1;
        }
        if (b == null) {
            return type == 0;
        }

        switch (type) {
            case 0:
                if (a instanceof Number && b instanceof Number) {
                    return ((Number) a).doubleValue() > ((Number) b).doubleValue();
                } else {
                    return a.toString().compareToIgnoreCase(b.toString()) > 0;
                }
            default:
                if (a instanceof Number && b instanceof Number) {
                    return ((Number) a).doubleValue() < ((Number) b).doubleValue();
                } else {
                    return a.toString().compareToIgnoreCase(b.toString()) < 0;
                }
        }
    }

    public LinkedList<E> order(String atribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E data = this.head.getData();
            if (data instanceof Object) {
                E[] lista = this.toArray();
                reset();
                for (int i = 1; i < lista.length; i++) {
                    E aux = lista[i];
                    int j = i - 1;
                    while (j >= 0 && atrribute_compare(atribute, lista[j], aux, type)) {
                        lista[j + 1] = lista[j--];
                    }
                    lista[j + 1] = aux;
                }
                this.toList(lista);
            }
        }
        return this;
    }

    private Boolean atrribute_compare(String attribute, E a, E b, Integer type) throws Exception {
        return compare(exist_attribute(a, attribute), exist_attribute(b, attribute), type);
    }

    private Object exist_attribute(E a, String attribute) throws Exception {
        Method method = null;

        attribute = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);

        try {
            for (Method m : a.getClass().getMethods()) {
                if (m.getName().equalsIgnoreCase(attribute)) {
                    method = m;
                    break;
                }
            }

            if (method == null) {
                for (Method m : a.getClass().getSuperclass().getMethods()) {
                    if (m.getName().equalsIgnoreCase(attribute)) {
                        method = m;
                        break;
                    }
                }
            }

            if (method != null) {
                return method.invoke(a);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error al invocar el metodo para el atributo: " + attribute, e);
        }

        return null; 

    }

    public E[] toTypedArray(E[] a) {
        if (a.length < size) {
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        Node<E> current = head;
        int index = 0;
        while (current != null) {
            a[index++] = current.getData();
            current = current.getNext();
        }
        return a;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // metodos para ordenar por atributos
    public LinkedList<E> shellSort(String attribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            int n = lista.length;

            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    E aux = lista[i];
                    int j = i;

                    while (j >= gap && atrribute_compare(attribute, aux, lista[j - gap], type)) {
                        lista[j] = lista[j - gap];
                        j -= gap;
                    }

                    lista[j] = aux;
                }
            }

            this.toList(lista);
        }
        return this;
    }

    public LinkedList<E> quickSort(String attribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            quickSort(lista, 0, lista.length - 1, attribute, type);
            this.toList(lista);
        }
        return this;
    }

    private void quickSort(E[] lista, Integer low, Integer high, String attribute, Integer type) throws Exception {
        if (low < high) {

            Integer pivot = particion(lista, low, high, attribute, type);

            quickSort(lista, low, pivot - 1, attribute, type);
            quickSort(lista, pivot + 1, high, attribute, type);
        }
    }

    private Integer particion(E[] lista, Integer low, Integer high, String attribute, Integer type) throws Exception {
        E pivot = lista[high];

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (atrribute_compare(attribute, lista[j], pivot, type)) {
                i++;
                E aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;
            }
        }

        E aux = lista[i + 1];
        lista[i + 1] = lista[high];
        lista[high] = aux;

        return i + 1;
    }

    public LinkedList<E> mergeSort(String atribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            mergeSort(lista, 0, lista.length - 1, atribute, type);
            this.toList(lista);
        }
        return this;
    }

    private void mergeSort(E[] lista, Integer l, Integer r, String attribute, Integer type) throws Exception {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(lista, l, m, attribute, type);
            mergeSort(lista, m + 1, r, attribute, type);
            merge(lista, l, m, r, attribute, type);
        }
    }

    private void merge(E[] lista, Integer l, Integer m, Integer r, String attribute, Integer type) throws Exception {
        int n1 = m - l + 1;
        int n2 = r - m;

        E[] L = (E[]) java.lang.reflect.Array.newInstance(lista.getClass().getComponentType(), n1);
        E[] R = (E[]) java.lang.reflect.Array.newInstance(lista.getClass().getComponentType(), n2);

        for (int i = 0; i < n1; i++) {
            L[i] = lista[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = lista[m + 1 + j];
        }

        int leftIndex = 0, rightIndex = 0;
        int k = l;

        while (leftIndex < n1 && rightIndex < n2) {
            boolean condicion = atrribute_compare(attribute, L[leftIndex], R[rightIndex], type);
            if (condicion) {
                lista[k] = L[leftIndex];
                leftIndex++;
            } else {
                lista[k] = R[rightIndex];
                rightIndex++;
            }
            k++;
        }

        while (leftIndex < n1) {
            lista[k] = L[leftIndex];
            leftIndex++;
            k++;
        }

        while (rightIndex < n2) {
            lista[k] = R[rightIndex];
            rightIndex++;
            k++;
        }
    }

    public LinkedList<E> cloneList() {
        LinkedList<E> clonedList = new LinkedList<>();

        // verificar que la lista no sea null
        if (!this.isEmpty()) {
            Node<E> current = this.head;
            while (current != null) {
                clonedList.add(current.getData()); // elementos uno a uno
                current = current.getNext();
            }
        }

        return clonedList;
    }

    //////////////////////////////////////////////////////////////////////////////////
    public LinkedList<E> shellSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            int n = lista.length;
            switch (type) {
                case 1:
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i += 1) {
                            E temp = lista[i];
                            int j;
                            for (j = i; j >= gap
                                    && lista[j - gap].toString().compareTo(temp.toString()) > 0; j -= gap) {
                                lista[j] = lista[j - gap];
                            }
                            lista[j] = temp;
                        }
                    }
                    break;
                default:
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i += 1) {
                            E temp = lista[i];
                            int j;
                            for (j = i; j >= gap
                                    && lista[j - gap].toString().compareTo(temp.toString()) < 0; j -= gap) {
                                lista[j] = lista[j - gap];
                            }
                            lista[j] = temp;
                        }
                    }
                    break;
            }
            this.toList(lista);
        }
        return this;
    }

    public LinkedList<E> quickSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            quickSort(lista, 0, lista.length - 1, type);
            this.toList(lista);
        }
        return this;
    }

    private void quickSort(E[] lista, Integer low, Integer high, Integer type) {
        if (low < high) {
            Integer pi = particion(lista, low, high, type);
            quickSort(lista, low, pi - 1, type);
            quickSort(lista, pi + 1, high, type);
        }
    }

    private Integer particion(E[] lista, Integer low, Integer high, Integer type) {
        E pivot = lista[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            Boolean condicion = (type == 1) ? (lista[j].toString().compareTo(pivot.toString()) < 0)
                    : (lista[j].toString().compareTo(pivot.toString()) > 0);
            if (condicion) {
                i++;
                E aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;

            }
        }
        E aux = lista[i + 1];
        lista[i + 1] = lista[high];
        lista[high] = aux;
        return i + 1;
    }

    public LinkedList<E> mergeSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            mergeSort(lista, 0, lista.length - 1, type);
            this.toList(lista);
        }
        return this;
    }

    private void mergeSort(E[] lista, Integer l, Integer r, Integer type) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(lista, l, m, type);
            mergeSort(lista, m + 1, r, type);
            merge(lista, l, m, r, type);
        }
    }

    private void merge(E[] lista, Integer l, Integer m, Integer r, Integer type) {
        int n1 = m - l + 1;
        int n2 = r - m;

        E L[] = (E[]) new Object[n1];
        E R[] = (E[]) new Object[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = lista[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = lista[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            Boolean condicion = (type == 1) ? (L[i].toString().compareTo(R[j].toString()) <= 0)
                    : (L[i].toString().compareTo(R[j].toString()) >= 0);

            if (condicion) {
                lista[k] = L[i];
                i++;
            } else {
                lista[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            lista[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            lista[k] = R[j];
            j++;
            k++;
        }
    }  
}
