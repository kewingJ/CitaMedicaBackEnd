package com.example.citasmedicasbackbean.controller;


import com.example.citasmedicasbackbean.service.InitService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("")
public class InitController {
    InitService initService = new InitService();

    @GET
    @Path("init")
    @Produces("text/plain")
    public Response iniciar() {
        return initService.iniciar();
    }

    @GET
    @Path("")
    @Produces("text/plain")
    public Response bienvenida() {
        return initService.bienvenida();
    }
}
