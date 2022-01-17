package com.example.citasmedicasbackbean.service;

import com.example.citasmedicasbackbean.modelo.Cita;
import com.example.citasmedicasbackbean.modelo.Disponibilidad;
import com.example.citasmedicasbackbean.modelo.Usuario;
import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DoctorService {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    /**
     * @param userId String
     * @param fecha String
     * @param rol String
     * @return envia las citas para el doctor y la disponibilidad de un doctor para paciente
     */
    public Response getCalendario(String userId, String fecha, String rol) throws ParseException{
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
                ex.printStackTrace();
            }
        }
        else {
            try {
                long idDoctor = Long.parseLong(userId);
                Date fechaDisponibilidad = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
                //obtener el dia de la fecha
                DateFormat formatDia = new SimpleDateFormat("EEEE");
                String dia = diaEs(upperCaseFirst(formatDia.format(fechaDisponibilidad)));

                String jpql = "SELECT d FROM Disponibilidad d where d.usuarioDoctor.idUsuario = :idDoctor AND d.dia = :dia";
                TypedQuery<com.example.citasmedicasbackbean.modelo.Disponibilidad> query =
                        em.createQuery(jpql, com.example.citasmedicasbackbean.modelo.Disponibilidad.class);
                query.setParameter("idDoctor", idDoctor);
                query.setParameter("dia", dia);
                listaResultado = query.getResultList();
            } catch (NoResultException ex) {
                ex.printStackTrace();
            }
        }
        return Response.ok(listaResultado).build();
    }

    /**
     * @param disponibilidadRequest
     * @return guarda una nueva disponibilidad
     */
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
        return Response.ok().status(Response.Status.CREATED).build();
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static String diaEs(String val) {
        String resultado = "";
        switch (val){
            case "Monday":
                resultado = "Lunes";
                break;
            case "Tuesday":
                resultado = "Martes";
                break;
            case "Wednesday":
                resultado = "Miercoles";
                break;
            case "Thursday":
                resultado = "Jueves";
                break;
            case "Friday":
                resultado = "Viernes";
                break;
            case "Saturday":
                resultado = "Sabado";
                break;
            case "Sunday":
                resultado = "Domingo";
                break;
            default: resultado = val;
            break;
        }
        return resultado;
    }
}
