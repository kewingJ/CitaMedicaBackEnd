package com.example.citasmedicasbackbean.controller;

import com.example.citasmedicasbackbean.modelo.Disponibilidad;
import com.example.citasmedicasbackbean.service.DoctorService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

@Path("/schedule")
public class DoctorController {
    DoctorService doctorService = new DoctorService();

    /**
     * URL: http://localhost:8080/schedule?idUser=1&fecha=06-01-2022&rol=paciente
     *
     *
     * @param idUser String
     * @param fecha String
     * @param rol String
     * @return Response
     */
    @GET
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalendario(@QueryParam("idUser") String idUser,
                                  @QueryParam("fecha") String fecha,
                                  @QueryParam("rol") String rol) throws ParseException {
        return doctorService.getCalendario(idUser, fecha, rol);
    }

    /**
     * URL: http://localhost:8080/appointment Parameters in
     * {"usuarioDoctor":{
     *     "idUsuario": 1,
     *     "nombre": "uno",
     *     "password": "$2a$10$07CEBSTceqwklAFHVevhS.yvTQLynx.wRuRDqZ2mObQ/rmJua5tkO",
     *     "rol": "Paciente",
     *     "username": "hhurtado"
     * },"dia":"Martes","horaInicio":"9:00AM","horaFin":"5:30PM"}
     * @param disponibilidadRequest
     * @return Response list NOTA: retorna el valor de "bien" cuando se agrega una nueva cita
     */
    @POST
    @Path("new-day")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDisponibilidad(Disponibilidad disponibilidadRequest){
        return doctorService.saveDisponibilidad(disponibilidadRequest);
    }
}
