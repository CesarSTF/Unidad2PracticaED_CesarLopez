package com.practicaListas.rest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.practicaListas.controller.dao.services.HisotiralCrudServices;
import com.practicaListas.controller.excepcion.ListEmptyException;
import com.practicaListas.models.HistorialCrud;

@Path("/historial")
public class HistorialCrudApi {
    private HisotiralCrudServices hisotiralCrudServices = new HisotiralCrudServices();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllHistorialCrud() {
    HashMap<String, Object> map = new HashMap<>();
    try {
        HistorialCrud[] historiales = hisotiralCrudServices.listAll();
        if (historiales.length == 0) {
            throw new ListEmptyException("Error: No hay historial registrado.");
        }
        map.put("msg", "OK");
        map.put("data", historiales);
        return Response.ok(map).build();
    } catch (Exception e) {
        map.put("msg", "Error al obtener la lista de historiales: " + e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
    }
}

}
