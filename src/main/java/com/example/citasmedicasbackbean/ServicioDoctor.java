package com.example.citasmedicasbackbean;

import com.example.citasmedicasbackbean.modelo.Cita;
import com.example.citasmedicasbackbean.modelo.Disponibilidad;
import com.example.citasmedicasbackbean.modelo.Usuario;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/schedule")
public class ServicioDoctor {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    /**
     * URL: http://localhost:8080/schedule/1&24-12-2021&doctor o 1&24-12-2021&paciente
     *
     *
     * @param datos String
     * @return Response
     */
    @GET
    @Path("{datos}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List getCalendario(@PathParam("datos") String datos) throws ParseException{
        String[] parts = datos.split("&");
        String userId = parts[0];
        String fecha = parts[1];
        String rol = parts[2];
        List listaResultado = new ArrayList<>();
        if (rol.equals("doctor")){
            try {
                long idDoctor = Long.parseLong(userId);
                Date fechaCita = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);

                String jpql = "SELECT c FROM Cita c where c.usuarioDoctor.idUsuario = :idDoctor AND DATE(c.fechaCita) = :fechaCita";
                TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
                query.setParameter("idDoctor", idDoctor);
                query.setParameter("fechaCita", fechaCita);
                listaResultado = query.getResultList();
            } catch (NoResultException ex) {
                //ex.printStackTrace();
            }
        } else {
            try {
                long idDoctor = Long.parseLong(userId);
                Date fechaDisponibilidad = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
                //obtener el dia de la fecha
                DateFormat formatDia = new SimpleDateFormat("EEEE");
                String dia = upperCaseFirst(formatDia.format(fechaDisponibilidad));

                String jpql = "SELECT d FROM Disponibilidad d where d.usuarioDoctor.idUsuario = :idDoctor AND d.dia = :dia";
                TypedQuery<com.example.citasmedicasbackbean.modelo.Disponibilidad> query =
                        em.createQuery(jpql, com.example.citasmedicasbackbean.modelo.Disponibilidad.class);
                query.setParameter("idDoctor", idDoctor);
                query.setParameter("dia", dia);
                listaResultado = query.getResultList();
            } catch (NoResultException ex) {
                //ex.printStackTrace();
            }
        }
        return listaResultado;
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
    public Response saveDisponibilidad(Disponibilidad disponibilidadRequest) {
        Usuario usuarioDoctor = disponibilidadRequest.getUsuarioDoctor();
        String dia = disponibilidadRequest.getDia();
        String horaInicio = disponibilidadRequest.getHoraInicio();
        String horaFin = disponibilidadRequest.getHoraFin();

        //pasar datos al modelo
        Disponibilidad disponibilidad = new Disponibilidad(usuarioDoctor, dia, horaInicio, horaFin);
        try {
            em.getTransaction().begin();
            em.persist(disponibilidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok("bien").build();
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
