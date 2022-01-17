package com.example.citasmedicasbackbean.controller;

import com.example.citasmedicasbackbean.modelo.Usuario;
import com.example.citasmedicasbackbean.service.AuthService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    AuthService authService = new AuthService();

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
    public Response signup(Usuario usuarioRequest) {
        return authService.signup(usuarioRequest);
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
    public Response login(Usuario usuarioRequest){
        return authService.login(usuarioRequest);
    }
}
