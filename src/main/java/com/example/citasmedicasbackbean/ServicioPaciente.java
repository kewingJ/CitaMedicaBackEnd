package com.example.citasmedicasbackbean;

import com.example.citasmedicasbackbean.modelo.Usuario;
import com.example.citasmedicasbackbean.modelo.Cita;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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
     * URL: http://localhost:8080/appointment Parameters in
     * Postman: {"usuarioPaciente":"12","usuarioDoctor":"1","fechaCita":"2021-12-24 06:00:00"}
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
    public int deleteCitaPaciente(@PathParam("citaId") long citaId) {
        int deletedCount = 0;
        try {
            //obtener los datos de la cita a borrar
            String jpql = "SELECT c FROM Cita c where c.idCita = :citaId";
            TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
            query.setParameter("citaId", citaId);
            Cita cita = query.getSingleResult();

            //proceder a borrar cita
            em.getTransaction().begin();
            em.remove(cita);
            em.getTransaction().commit();
            emf.close();
            em.close();
        } catch (NoResultException ex) {
            //ex.printStackTrace();
        }
        return deletedCount;
    }
}