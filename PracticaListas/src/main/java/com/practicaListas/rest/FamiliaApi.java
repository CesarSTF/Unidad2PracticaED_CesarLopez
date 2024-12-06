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
import com.practicaListas.controller.dao.services.FamiliaServices;
import com.practicaListas.controller.dao.services.HisotiralCrudServices;
import com.practicaListas.controller.excepcion.ListEmptyException;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia;
import com.practicaListas.models.enums.TipoDeCrud;

@Path("/familia")
public class FamiliaApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response gettAll() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        FamiliaServices ps = new FamiliaServices();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        FamiliaServices ps = new FamiliaServices();
        HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setFamilia(ps.get(id));
            if (ps.getFamilia() == null || ps.getFamilia().getId() == null) {
                map.put("msg", "No existe familias con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            hisotiralCrudServices.registrarHistorial(TipoDeCrud.READ,
                    "Familia realizada con exito:" + ps.getFamilia().toString());
            map.put("msg", "OK");
            map.put("data", ps.getFamilia());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener la familia");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/codigo/{value}")
    public Response getCodigoF(@PathParam("value") String value) {
        HashMap<String, Object> map = new HashMap<>();
        FamiliaServices ps = new FamiliaServices();
        try {
            if (value == null) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setFamilia(ps.binarySearch("codigoFamilia", value.toString()));
            if (ps.getFamilia() == null) {
                map.put("msg", "No existe familias con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getFamilia());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener la familia");
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
        // String a = g.toJson(map);

        try {
            FamiliaServices ps = new FamiliaServices();
            HisotiralCrudServices historialCrudServices = new HisotiralCrudServices();
            ps.getFamilia().setNombreFamilia(map.get("NombreFamilia").toString());
            ps.getFamilia().setCodigoFamilia(ps.codigoF(map.get("NombreFamilia").toString()));
            ps.getFamilia().setNroIntegrantes(Integer.parseInt(map.get("NroIntegrantes").toString()));
            ps.getFamilia().setSaldo(Float.parseFloat(map.get("saldo").toString()));
            ps.save();
            historialCrudServices.registrarHistorial(TipoDeCrud.CREATE,
                    "Familia creada con exito:" + ps.getFamilia().toString());
            res.put("msg", "OK");
            res.put("data", "Familia registrada correctamente");
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
            FamiliaServices ps = new FamiliaServices();
            HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();
            ps.setFamilia(ps.get(Integer.parseInt(map.get("id").toString())));
            ps.getFamilia().setNombreFamilia(map.get("NombreFamilia").toString());
            ps.getFamilia().setNroIntegrantes(Integer.parseInt(map.get("NroIntegrantes").toString()));
            ps.getFamilia().setSaldo(Float.parseFloat(map.get("saldo").toString()));
            hisotiralCrudServices.registrarHistorial(TipoDeCrud.UPDATE,
                    "Familia actualizada con exito: " + ps.getFamilia().toString());
            ps.update();
            res.put("status", "success");
            res.put("message", "Familia actualizada con exito.");
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
    public Response deleteById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        FamiliaServices ps = new FamiliaServices();
        HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();

        try {
            if (id == null || id < 1) {
                map.put("status", "error");
                map.put("msg", "ID invalido. Debe ser un numero positivo.");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            boolean deleted = ps.deleteFamiliaByIndex(id);
            if (deleted) {
                hisotiralCrudServices.registrarHistorial(TipoDeCrud.DELETE,
                        "Familia eliminada con exito: " + ps.getFamilia().toString());
                map.put("status", "success");
                map.put("msg", "Familia eliminada correctamente.");
                return Response.ok(map).build();
            } else {
                map.put("status", "error");
                map.put("msg", "No se pudo eliminar la familia. Verifica si el ID existe.");
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
        FamiliaServices ps = new FamiliaServices();

        try {
            LinkedList<Familia> lista = ps.order(attribute, type);

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
        FamiliaServices ps = new FamiliaServices();

        try {
            LinkedList<Familia> lista = ps.ordenarPor(attribute, type, metodo);

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

        FamiliaServices ps = new FamiliaServices();

        try {
            LinkedList<Familia> results;

            if (attribute.equalsIgnoreCase("saldo")) {                
                try {
                    Float pardeFloat = Float.parseFloat(value);
                    results = ps.binarySearchLineal(attribute, pardeFloat);
                } catch (NumberFormatException e) {
                    map.put("msg", "El valor proporcionado no es un numero valido");
                    return Response.status(Status.BAD_REQUEST).entity(map).build();
                }                
            } else if (attribute.equalsIgnoreCase("nrointegrantes")) {
                results = ps.binarySearchLineal(attribute, Integer.parseInt(value));
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
        FamiliaServices ps = new FamiliaServices();

        try {
            Object searchValue = null;

            switch (attribute.toLowerCase()) {
                case "saldo":
                    try {
                        searchValue = Float.parseFloat(value);
                        //System.out.println(searchValue);
                    } catch (NumberFormatException e) {
                        map.put("msg", "El valor proporcionado no es un número valido");
                        return Response.status(Status.BAD_REQUEST).entity(map).build();
                    }
                    break;
                case "nrointegrantes":
                    try {
                        searchValue = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        map.put("msg", "El valor proporcionado no es un número valido para nroIntegrantes");
                        return Response.status(Status.BAD_REQUEST).entity(map).build();
                    }
                    break;
                default:
                    searchValue = value;
                    break;
            }

            LinkedList<Familia> results = ps.binarySearchLineal(attribute, searchValue);

            if (results != null && !results.isEmpty()) {
                map.put("msg", "OK");
                map.put("data", results);
                return Response.ok(map).build();
            } else {
                map.put("msg", "No se encontraron las familias con los valores proporcionados");
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
