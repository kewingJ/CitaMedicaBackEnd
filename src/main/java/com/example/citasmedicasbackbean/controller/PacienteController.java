package com.example.citasmedicasbackbean.controller;

import com.example.citasmedicasbackbean.modelo.Cita;
import com.example.citasmedicasbackbean.service.PacienteService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

@Path("/appointment")
public class PacienteController {
    PacienteService pacienteService = new PacienteService();

    /**
     * URL: http://localhost:8080/appointment/today/12
     *
     * @param userId int
     * @return Response
     */
    @GET
    @Path("today/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitasPaciente(@PathParam("userId") int userId){
        return pacienteService.getCitasPaciente(userId);
    }

    /**
     * URL: http://localhost:8080/appointment/doctores
     *
     */
    @GET
    @Path("/doctores")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaDoctores(){
        return pacienteService.getListaDoctores();
    }

    /**
     * URL: http://localhost:8080/appointment Parameters in
     * Postman: {"usuarioPaciente":{
     *     "idUsuario": 12,
     *     "nombre": "Henry Hurtado",
     *     "password": "$2a$10$07CEBSTceqwklAFHVevhS.yvTQLynx.wRuRDqZ2mObQ/rmJua5tkO",
     *     "rol": "Paciente",
     *     "username": "hhurtado"
     * },"usuarioDoctor":{
     *     "idUsuario": 1,
     *     "nombre": "uno",
     *     "password": "$2a$10$07CEBSTceqwklAFHVevhS.yvTQLynx.wRuRDqZ2mObQ/rmJua5tkO",
     *     "rol": "Paciente",
     *     "username": "hhurtado"
     * },"fechaCita":"2021-12-24 06:00:00"}
     *
     * @param citaRequest
     * @return Response list NOTA: retorna el valor de "bien" cuando se agrega una nueva cita
     */
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCitaPaciente(Cita citaRequest) throws ParseException {
        return pacienteService.saveCitaPaciente(citaRequest);
    }

    /**
     * URL: http://localhost:8080/appointment/12
     *
     * @param citaId long
     * @return Response
     */
    @DELETE
    @Path("{citaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCitaPaciente(@PathParam("citaId") long citaId) throws ParseException{
        return pacienteService.deleteCitaPaciente(citaId);
    }
}
