package com.example.citasmedicasbackbean.service;

import com.example.citasmedicasbackbean.modelo.Usuario;
import com.example.citasmedicasbackbean.modelo.Cita;
import com.example.citasmedicasbackbean.utils.ValidarGuardarCita;

import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class PacienteService {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    /**
     * @param userId int
     * @return Response
     */
    public Response getCitasPaciente(int userId) {
        List<Cita> listaCitas = new ArrayList<>();
        try {
            long idPaciente = userId;

            String jpql = "SELECT c FROM Cita c where c.usuarioPaciente.idUsuario = :idPaciente";
            TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
            query.setParameter("idPaciente", idPaciente);
            listaCitas = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return Response.ok(listaCitas).build();
    }

    /**
     *
     * @return Response
     */
    public Response getListaDoctores() {
        List<Usuario> listaDoctores = new ArrayList<>();
        try {
            String jpql = "SELECT u FROM Usuario u where u.rol = :rol";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("rol", "doctor");
            listaDoctores = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return Response.ok(listaDoctores).build();
    }

    /**
     * @param citaRequest
     * @return Response
     */
    public Response saveCitaPaciente(Cita citaRequest) throws ParseException {
        Usuario usuarioPaciente = citaRequest.getUsuarioPaciente();
        Usuario usuarioDoctor = citaRequest.getUsuarioDoctor();
        String fechaCita = citaRequest.getFechaCita();
        //formato de fecha
        Date fechaCitaQuery = new SimpleDateFormat("yyyy-MM-dd").parse(fechaCita);

        //metodo donde se validan las reglas de negocio
        ValidarGuardarCita validarGuardarCita = new ValidarGuardarCita();
        String mensaje = validarGuardarCita.validar(usuarioPaciente, usuarioDoctor, fechaCita, fechaCitaQuery);

        //pasar datos al modelo
        Cita cita = new Cita(usuarioPaciente, usuarioDoctor, fechaCita);
        try {
            if (mensaje.equals("")){
                em.getTransaction().begin();
                em.persist(cita);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mensaje.equals("")){
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.ok(mensaje).build();
        }
    }

    /**
     * @param citaId long
     * @return Response
     */
    public Response deleteCitaPaciente(long citaId) throws ParseException {
        String mensaje = "";
        LocalDateTime myDateObj = LocalDateTime.now();
        try {
            //obtener los datos de la cita a borrar
            String jpql = "SELECT c FROM Cita c where c.idCita = :citaId";
            TypedQuery<Cita> query = em.createQuery(jpql, Cita.class);
            query.setParameter("citaId", citaId);
            Cita cita = query.getSingleResult();

            //verificar si la cancelacion no es media hora antes de la cita
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fecha1 = cita.getFechaCita();
            String fecha2 = myDateObj.format(myFormatObj);
            //
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            boolean rangoMinutos = RangoFechasMinutos(formato.parse(fecha1), formato.parse(fecha2));
            if(rangoMinutos) {
                //proceder a borrar cita
                em.getTransaction().begin();
                em.remove(cita);
                em.getTransaction().commit();
                emf.close();
                em.close();
            } else {
                mensaje = "No puede cancelar la cita media hora antes";
            }
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return Response.ok(mensaje).build();
    }

    public static boolean RangoFechasMinutos(Date endDate, Date nowDate) {
        boolean bandera = false;
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // Obtener la diferencia de tiempo de milisegundos entre dos tiempos
        long diff = endDate.getTime() - nowDate.getTime();
        // calcula la diferencia en días
        long day = diff / nd;
        // calcula la diferencia en horas
        long hour = diff % nd / nh;
        // Calcula la diferencia en minutos
        long min = diff % nd % nh / nm;

        if (day > 0 || hour > 0 || min > 30){
            bandera = true;
        }
        return bandera;
    }


}