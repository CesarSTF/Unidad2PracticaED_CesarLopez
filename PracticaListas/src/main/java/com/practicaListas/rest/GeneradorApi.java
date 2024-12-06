package com.practicaListas.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.practicaListas.controller.dao.services.GeneradorServices;
import com.practicaListas.controller.dao.services.HisotiralCrudServices;
import com.practicaListas.controller.excepcion.ListEmptyException;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Generador;
import com.practicaListas.models.enums.TipoDeCrud;

@Path("/generador")
public class GeneradorApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response gettAll() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }

    @Path("/listType")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType() {
        HashMap map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();
        map.put("msg", "OK");
        map.put("data", ps.getUso());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getGeneradorById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();
        HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setGenerador(ps.get(id));
            if (ps.getGenerador() == null || ps.getGenerador().getId() == null) {
                map.put("msg", "No existe generador con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            hisotiralCrudServices.registrarHistorial(TipoDeCrud.READ, "Generador leido con exito: "+ ps.getGenerador().toString());
            map.put("msg", "OK");
            map.put("data", ps.getGenerador());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el generador");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/codigo/{value}")
    public Response getCodigoG(@PathParam("value") String value) {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();
        try {
            if (value == null) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setGenerador(ps.binarySearch("codigoGenerador", value.toString()));
            if (ps.getGenerador() == null) {
                map.put("msg", "No existen generadores con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getGenerador());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el generador");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        Gson g = new Gson();
        @SuppressWarnings("unused")
        String a = g.toJson(map);

        try {
            GeneradorServices ps = new GeneradorServices();
            HisotiralCrudServices historialCrudServices = new HisotiralCrudServices();
            ps.getGenerador().setConsumoComustible(Float.parseFloat(map.get("consumo").toString()));
            ps.getGenerador().setCosto(Float.parseFloat(map.get("costo").toString()));
            ps.getGenerador().setEnegeriaGenerada(Float.parseFloat(map.get("energia").toString()));
            ps.getGenerador().setModelo(map.get("modelo").toString());
            ps.getGenerador().setCodigoGenerador(ps.codigoG(map.get("modelo").toString()));
            ps.getGenerador().setUso(ps.getUso(map.get("uso").toString()));
            historialCrudServices.registrarHistorial(TipoDeCrud.CREATE, "Generador creada con exito: " + ps.getGenerador().toString());
            ps.save();
            res.put("msg", "OK");
            res.put("data", "Generador registrado correctamente");
            return Response.ok(res).build();
        } catch (NumberFormatException e) {
            res.put("msg", "Error en formato numerico");
            res.put("data", "Por favor verifica que los campos numericos sean validos.");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            System.out.println("Error en save data: " + e.toString());
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap map) {
        HashMap res = new HashMap<>();
        try {
            GeneradorServices ps = new GeneradorServices();
            HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();
            ps.setGenerador(ps.get(Integer.parseInt(map.get("id").toString())));
            ps.getGenerador().setConsumoComustible(Float.parseFloat(map.get("consumo").toString()));
            ps.getGenerador().setCosto(Float.parseFloat(map.get("costo").toString()));
            ps.getGenerador().setEnegeriaGenerada(Float.parseFloat(map.get("energia").toString()));
            ps.getGenerador().setModelo(map.get("modelo").toString());
            ps.getGenerador().setUso(ps.getUso(map.get("uso").toString()));
            hisotiralCrudServices.registrarHistorial(TipoDeCrud.UPDATE, "Generador actutalizado con extio: "+ ps.getGenerador().toString());
            ps.update();
            res.put("status", "success");
            res.put("message", "Generador actualizado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    public Response deleteGeneradorById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();
        HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();

        try {
            if (id == null || id < 1) {
                map.put("status", "error");
                map.put("msg", "ID invalido. Debe ser un numero positivo.");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            boolean deleted = ps.delete(id);
            if (deleted) {
                hisotiralCrudServices.registrarHistorial(TipoDeCrud.DELETE, "Generador eliminado con exito" + ps.getGenerador().toString());
                map.put("status", "success");
                map.put("msg", "Generador eliminado correctamente.");
                return Response.ok(map).build();
            } else {
                map.put("status", "error");
                map.put("msg", "No se pudo eliminar el generador. Verifica si el ID existe.");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
        } catch (Exception e) {
            map.put("status", "error");
            map.put("msg", "Error interno del servidor.");
            map.put("error", e.getMessage());
            e.printStackTrace(); // Log
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}")
    public Response getOrder(@PathParam("attribute") String attribute, @PathParam("type") Integer type) {
        HashMap<String, Object> map = new HashMap<>();
        
        GeneradorServices ps = new GeneradorServices();

        try {
            LinkedList<Generador> lista = ps.order(attribute, type);

            map.put("msg", "OK");
            map.put("data", lista.toArray());

            if (lista.isEmpty()) {
                map.put("data", new Object[] {});
            }
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }

        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}/{metodo}")
    public Response getOrderPor(@PathParam("attribute") String attribute, @PathParam("type") Integer type,
            @PathParam("metodo") String metodo) {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices();

        try {
            LinkedList<Generador> lista = ps.ordenarPor(attribute, type, metodo);

            map.put("msg", "OK");
            map.put("data", lista.toArray());

            if (lista.isEmpty()) {
                map.put("data", new Object[] {});
            }
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }

        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{attribute}/{value}")
    public Response binarySearchLin(@PathParam("attribute") String attribute, @PathParam("value") String value) {
        HashMap<String, Object> map = new HashMap<>();
        GeneradorServices ps = new GeneradorServices(); 

    try {
        LinkedList<Generador> results;

        if (attribute.equalsIgnoreCase("consumoComustible") || 
            attribute.equalsIgnoreCase("costo") || 
            attribute.equalsIgnoreCase("enegeriaGenerada")) {

            try {
                Float parsedValue = Float.parseFloat(value);
                results = ps.binarySearchLineal(attribute, parsedValue);
            } catch (NumberFormatException e) {
                map.put("msg", "El valor proporcionado no es un numero valido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }

        } else if (attribute.equalsIgnoreCase("uso")) {
            results = ps.binarySearchLineal(attribute, ps.getUso(value)); 
        } else {
            results = ps.binarySearchLineal(attribute, value); 
        }

        if (results != null && !results.isEmpty()) {
            map.put("msg", "OK");
            map.put("data", results);
            return Response.ok(map).build();
        } else {
            map.put("msg", "No se encontraron generadores con los valores proporcionados");
            return Response.status(Status.NOT_FOUND).entity(map).build();
        }

    } catch (Exception e) {
        map.put("msg", "Error en la busqueda");
        map.put("error", e.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
    }
        
        /*
        GeneradorServices ps = new GeneradorServices();

        try {
            Object searchValue = null;

            switch (attribute.toLowerCase()) {
                case "consumoComustible":
                case "costo":
                case "enegeriaGenerada":
                    try {
                        searchValue = Float.parseFloat(value);
                        //System.out.println(searchValue);
                    } catch (NumberFormatException e) {
                        map.put("msg", "El valor proporcionado no es un numero valido");
                        return Response.status(Status.BAD_REQUEST).entity(map).build();
                    }
                    break;
                case "uso":
                    searchValue = ps.getUso(value); 
                    break;
                default:
                    searchValue = value; 
                    break;
            }

            LinkedList<Generador> results = ps.binarySearchLineal(attribute, searchValue);

            if (results != null && !results.isEmpty()) {
                map.put("msg", "OK");
                map.put("data", results);
                return Response.ok(map).build();
            } else {
                map.put("msg", "No se encontraron generadores con los valores proporcionados");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }

        } catch (Exception e) {
            map.put("msg", "Error en la busqueda");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }        
            */
    }
}
