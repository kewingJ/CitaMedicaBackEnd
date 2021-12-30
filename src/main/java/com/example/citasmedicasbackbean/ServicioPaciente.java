package com.example.citasmedicasbackbean;

import com.example.citasmedicasbackbean.modelo.Usuario;
import com.example.citasmedicasbackbean.modelo.Cita;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/appointment")
public class ServicioPaciente {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

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
    public List<Cita> getCitasPaciente(@PathParam("userId") int userId) {
        List<Cita> listaCitas = new ArrayList<>();
        try {
            long idPaciente = userId;

            String jpql = "SELECT c FROM Cita c where c.usuarioPaciente.idUsuario = :idPaciente";
            TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
            query.setParameter("idPaciente", idPaciente);
            listaCitas = query.getResultList();
        } catch (NoResultException ex) {
            //ex.printStackTrace();
        }
        return listaCitas;
    }

    /**
     * URL: http://localhost:8080/appointment/doctores
     *
     */
    @GET
    @Path("/doctores")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getListaDoctores() {
        List<Usuario> listaDoctores = new ArrayList<>();
        try {
            String jpql = "SELECT u FROM Usuario u where u.rol = :rol";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("rol", "doctor");
            listaDoctores = query.getResultList();
        } catch (NoResultException ex) {
            //ex.printStackTrace();
        }
        return listaDoctores;
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
    public Response saveCitaPaciente(Cita citaRequest) {
        Usuario usuarioPaciente = citaRequest.getUsuarioPaciente();
        Usuario usuarioDoctor = citaRequest.getUsuarioDoctor();
        String fechaCita = citaRequest.getFechaCita();

        //pasar datos al modelo
        Cita cita = new Cita(usuarioPaciente, usuarioDoctor, fechaCita);
        try {
            em.getTransaction().begin();
            em.persist(cita);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok("bien").build();
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
    public Map<String, String> deleteCitaPaciente(@PathParam("citaId") long citaId) throws ParseException {
        HashMap<String, String> map = new HashMap<>();
        String mensaje = "";
        try {
            //obtener los datos de la cita a borrar
            String jpql = "SELECT c FROM Cita c where c.idCita = :citaId";
            TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
            query.setParameter("citaId", citaId);
            Cita cita = query.getSingleResult();

            //verificar si la cancelacion no es media hora antes de la cita
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fecha1 = cita.getFechaCita();
            String fecha2 = LocalDateTime.now().format(formatter);
            //
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaA = formato.parse(fecha1);
            Date fechaB = formato.parse(fecha2);
            long diff = fechaA.getTime() - fechaB.getTime();
            long day = diff/(24*60*60*1000);
            long hour = (diff/(60*60*1000)-day*24);
            long min = ((diff/(60*1000))-day*24*60-hour*60);
            if(day > 0 || hour > 0 || min > 30) {
                //proceder a borrar cita
                em.getTransaction().begin();
                em.remove(cita);
                em.getTransaction().commit();
                emf.close();
                em.close();
                mensaje = "bien";
            } else {
                mensaje = "No puede cancelar la cita media hora antes";
            }
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        map.put("mensaje", mensaje);
        return map;
    }
}