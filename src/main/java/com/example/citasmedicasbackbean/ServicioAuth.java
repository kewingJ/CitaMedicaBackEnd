package com.example.citasmedicasbackbean;

import com.example.citasmedicasbackbean.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class ServicioAuth {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("citasMedicas");
    EntityManager em = emf.createEntityManager();

    /**
     * URL: http://localhost:8080/auth/signup Parameters in
     * Postman: {"nombre":"Kiwi","username":"kiwi","password":"Holamundo","rol":"Paciente"}
     *
     * @param usuarioRequest
     * @return Response list NOTA: retorna el valor de "bien" cuando se agrega un nuevo usuario
     */
    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> signup(Usuario usuarioRequest) {
        String nombre = usuarioRequest.getNombre();
        String username = usuarioRequest.getUsername();
        HashMap<String, String> map = new HashMap<>();
        //encriptar con jbcrypt
        String password = BCrypt.hashpw(usuarioRequest.getPassword(), BCrypt.gensalt(10));
        String rol = usuarioRequest.getRol();

        //pasar datos al modelo
        Usuario usuario = new Usuario(nombre, username, password, rol);
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("mensaje", "Bien");
        return map;
    }

    /**
     * URL: http://localhost:8080/auth/login Parameters in
     * Postman: {"username":"kiwi","password":"Holamundo"}
     *
     * @param usuarioRequest
     * @return Response list NOTA: retorna los datos del usuario si existe en caso contrario
     * retorna null
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario login(Usuario usuarioRequest) {
        Usuario usuario = null;
        try {
            String user = usuarioRequest.getUsername();
            String pass = usuarioRequest.getPassword();

            String jpql = "SELECT u FROM Usuario u where u.username = :user";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("user", user);
            if (query.getSingleResult() != null){
                if(BCrypt.checkpw(pass, query.getSingleResult().getPassword())){
                    usuario = query.getSingleResult();
                }
            }
        } catch (NoResultException ex) {
            //ex.printStackTrace();
        }
        return usuario;
    }
}
