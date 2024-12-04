package com.practicaListas.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.practicaListas.controller.tda.list.LinkedList;

@Path("sortPerformance")
public class MyResource {

    // @GET
    // @Produces(MediaType.TEXT_PLAIN)
    // public String evaluateSorts() throws Exception {
    // int startTime, endTime, duration;

    // // Generar listas aleatorias
    // LinkedList<Integer> randomList = createRandomList(20000);
    // LinkedList<Integer> clonedList1 = randomList.cloneList();
    // LinkedList<Integer> clonedList2 = randomList.cloneList();

    // // Shell Sort
    // startTime = (int) System.currentTimeMillis();
    // randomList.shellSort(1);
    // endTime = (int) System.currentTimeMillis();
    // duration = endTime - startTime;
    // System.out.println("ShellSort Execution Time: " + duration + " ms");

    // // Quick Sort
    // startTime = (int) System.currentTimeMillis();
    // clonedList1.quickSort(1);
    // endTime = (int) System.currentTimeMillis();
    // duration = endTime - startTime;
    // System.out.println("QuickSort Execution Time: " + duration + " ms");

    // // Merge Sort
    // startTime = (int) System.currentTimeMillis();
    // clonedList2.mergeSort(1);
    // endTime = (int) System.currentTimeMillis();
    // duration = endTime - startTime;
    // System.out.println("MergeSort Execution Time: " + duration + " ms");

    // return "Sorting performance evaluated successfully!";
    // }

    // private LinkedList<Integer> createRandomList(int size) {
    // LinkedList<Integer> list = new LinkedList<>();
    // for (int i = 0; i < size; i++) {
    // list.add((int) (Math.random() * 100));
    // }
    // return null;//list;
    // }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String evaluateSearchPerformance() throws Exception {
        JsonObject response = new JsonObject();
        LinkedList<Integer> randomList = createRandomList(10000);

        // valor aleatorio de la lista para buscar
        int searchValue = randomList.get((int) (Math.random() * randomList.getSize()));
        response.addProperty("searchValue", searchValue);
        System.out.println("===========================================");
        System.out.println("        SORTING AND SEARCH PERFORMANCE      ");
        System.out.println("===========================================");
        System.out.println("Value to Search: " + searchValue);
        System.out.println();

        // tiempo para la busqueda secuencial (lineal)
        long linearStart = System.currentTimeMillis();
        int linearResultIndex = linearSearch(randomList, searchValue);
        long linearEnd = System.currentTimeMillis();
        long linearDuration = linearEnd - linearStart;


        System.out.println("----- LINEAR SEARCH -----");
        System.out.println("Execution Time: " + linearDuration + " ms");
        System.out.println("Result: " + (linearResultIndex != -1));
        System.out.println();

        response.addProperty("linearSearchTime", (linearEnd - linearStart) + " ms");

        randomList.quickSort(1);

        // tiempo para la busqueda binaria
        long binaryStart = System.currentTimeMillis();
        int binaryResultIndex = binarySearch(randomList, searchValue);
        long binaryEnd = System.currentTimeMillis();
        long binaryDuration = binaryEnd - binaryStart;


        System.out.println("----- BINARY SEARCH -----");
        System.out.println("Execution Time: " + binaryDuration + " ms");
        System.out.println("Result: " + (binaryResultIndex != -1));
        System.out.println();

        response.addProperty("binarySearchTime", (binaryEnd - binaryStart) + " ms");

        System.out.println("===========================================");
        System.out.println("      PERFORMANCE EVALUATION COMPLETE      ");
        System.out.println("===========================================");

        return response.toString();
    }

    // lista de datos aleatorios
    private LinkedList<Integer> createRandomList(int size) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add((int) (Math.random() * 10000));
        }
        return list;
    }

    // busqueda lineal
    private int linearSearch(LinkedList<Integer> list, int value) throws Exception {
        for (int i = 0; i < list.getSize(); i++) {
            if (list.get(i) == value) {
                return i;
            }
        }
        return -1;
    }

    // busqueda binaria
    private int binarySearch(LinkedList<Integer> list, int value) throws Exception {
        int low = 0;
        int high = list.getSize() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int midValue = list.get(mid);

            if (midValue == value) {
                return mid;
            } else if (midValue < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
