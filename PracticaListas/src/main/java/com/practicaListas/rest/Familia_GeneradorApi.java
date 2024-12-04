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
import com.practicaListas.controller.dao.services.Familia_GeneradorServices;
import com.practicaListas.controller.dao.services.GeneradorServices;
import com.practicaListas.controller.excepcion.ListEmptyException;
import com.practicaListas.controller.tda.list.LinkedList;
import com.practicaListas.models.Familia_Generador;
import com.practicaListas.models.Generador;

@Path("/familia_generador")
public class Familia_GeneradorApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response gettAll() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        Familia_GeneradorServices ps = new Familia_GeneradorServices();
        map.put("msg", "OK");
        try {
            map.put("data", ps.listShowAll());
            if (ps.listAll().isEmpty()) {
                map.put("data", new Object[] {});
            }

        } catch (Exception e) {
            map.put("data", new Object[] {});
            System.out.println("Error " + e);
        }
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        Familia_GeneradorServices ps = new Familia_GeneradorServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setFamilia_Generador(ps.get(id));
            if (ps.getFamilia_Generador() == null || ps.getFamilia_Generador().getId() == null) {
                map.put("msg", "No existe generador con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getFamilia_Generador());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el generador");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/disponibles/{idFamilia}")
    public Response getGeneradoresDisponibles(@PathParam("idFamilia") Integer idFamilia) {
        HashMap<String, Object> map = new HashMap<>();
        FamiliaServices familiaServices = new FamiliaServices();
        GeneradorServices generadorServices = new GeneradorServices();

        try {
            familiaServices.setFamilia(familiaServices.get(idFamilia));
            if (familiaServices.getFamilia() == null) {
                map.put("msg", "Familia no encontrada");
                return Response.status(Response.Status.NOT_FOUND).entity(map).build();
            }

            double presupuesto = familiaServices.getFamilia().getSaldo();

            LinkedList<Generador> generadores = generadorServices.listAll();
            LinkedList<Generador> disponibles = new LinkedList<>();
            for (Object obj : generadores.toArray()) {
                Generador g = (Generador) obj; // Cast 
                if (g.getCosto() <= presupuesto) {
                    disponibles.add(g);
                }
            }
            

            map.put("msg", "OK");
            map.put("data", disponibles.toArray());
            return Response.ok(map).build();

        } catch (Exception e) {
            map.put("msg", "Error interno del servidor");
            map.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);

        try {
            if (map.get("familia") != null && map.get("generador") != null) {
                FamiliaServices familiaServices = new FamiliaServices();
                familiaServices.setFamilia(familiaServices.get(Integer.parseInt(map.get("familia").toString())));
                GeneradorServices generadorServices = new GeneradorServices();
                generadorServices
                        .setGenerador(generadorServices.get(Integer.parseInt(map.get("generador").toString())));
                if (familiaServices.getFamilia().getId() != null && generadorServices.getGenerador().getId() != null) {
                    Familia_GeneradorServices ps = new Familia_GeneradorServices();
                    ps.getFamilia_Generador().setDescripcion(map.get("caracteristicas").toString());
                    ps.getFamilia_Generador().setId_Familia(familiaServices.getFamilia().getId());
                    ps.getFamilia_Generador().setId_Generador(generadorServices.getGenerador().getId());
                    ps.save();
                    res.put("msg", "OK");
                    res.put("data", "Equipo registrada correctamente");
                    return Response.ok(res).build();
                } else {
                    res.put("msg", "Error");
                    res.put("data", "La persona o el modelo de generador no existen");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }

            } else {
                res.put("msg", "Error");
                res.put("data", "Faltan datos");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

        } catch (Exception e) {
            System.out.println("Error en sav data: " + e.toString());
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
            Familia_GeneradorServices ps = new Familia_GeneradorServices();
            ps.setFamilia_Generador(ps.get(Integer.parseInt(map.get("id").toString())));
            if (ps.getFamilia_Generador().getId() == null) {
                res.put("msg", "Error");
                res.put("data", "No existe");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            } else {
                if (map.get("familia") != null && map.get("generador") != null) {
                    FamiliaServices familiaServices = new FamiliaServices();
                    familiaServices.setFamilia(familiaServices.get(Integer.parseInt(map.get("familia").toString())));
                    GeneradorServices generadorServices = new GeneradorServices();
                    generadorServices.setGenerador(generadorServices.get(Integer.parseInt(map.get("generador").toString())));
                    
                    if (familiaServices.getFamilia().getId() != null && generadorServices.getGenerador().getId() != null) {
                        if (map.get("caracteristicas") != null) {
                            ps.getFamilia_Generador().setDescripcion(map.get("caracteristicas").toString());
                        }
                        ps.getFamilia_Generador().setId_Familia(familiaServices.getFamilia().getId());
                        ps.getFamilia_Generador().setId_Generador(generadorServices.getGenerador().getId());
                        
                        ps.update(); // Guarda los cambios
                        res.put("msg", "OK");
                        res.put("data", "Editado correctamente");
                        return Response.ok(res).build();
                    } else {
                        res.put("msg", "Error");
                        res.put("data", "La familia o el generador no existen");
                        return Response.status(Status.BAD_REQUEST).entity(res).build();
                    }
                } else {
                    res.put("msg", "Error");
                    res.put("data", "Faltan datos");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }
            }
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
        Familia_GeneradorServices ps = new Familia_GeneradorServices();

        try {
            if (id == null || id < 1) {
                map.put("status", "error");
                map.put("msg", "ID invalido. Debe ser un numero positivo.");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            boolean deleted = ps.delete(id);
            if (deleted) {
                map.put("status", "success");
                map.put("msg", "Familia eliminada correctamente.");
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
        Familia_GeneradorServices ps = new Familia_GeneradorServices();

        try {
            LinkedList<Familia_Generador> lista = ps.order(attribute, type);

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

}
