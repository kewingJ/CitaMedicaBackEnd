package com.example.citasmedicasbackbean.segurity;

import com.example.citasmedicasbackbean.utils.ValidarRolUsuario;
import org.glassfish.jersey.internal.util.Base64;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

@Provider
public class SecuriyFilter implements ContainerRequestFilter {
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PACIENTE = "appointment"; //(protegido, roles → paciente)
    private static final String SECURED_URL_DOCTOR = "schedule"; //(protegido, roles → doctor)
    ValidarRolUsuario validarRolUsuario = new ValidarRolUsuario();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(requestContext.getUriInfo().getPath().contains(SECURED_URL_PACIENTE) || requestContext.getUriInfo().getPath().contains(SECURED_URL_DOCTOR)){
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0){
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                //
                String username = tokenizer.countTokens() > 0 ? tokenizer.nextToken() : "";
                String password = tokenizer.countTokens() > 0 ? tokenizer.nextToken() : "";

                //verificar en la base de datos si el usuario es paciente
                if (!"".equals(username) && !"".equals(password)){
                    //metodo para verificar el rol del usuario
                    String rol = validarRolUsuario.obtenerRolUsuario(username, password);
                    if (rol.equals("Paciente") && requestContext.getUriInfo().getPath().contains(SECURED_URL_PACIENTE)){
                        return;
                    } else if(rol.equals("doctor") && requestContext.getUriInfo().getPath().contains(SECURED_URL_DOCTOR)){
                        return;
                    } else if (rol.equals("Paciente") && requestContext.getUriInfo().getPath().equals("/"+SECURED_URL_DOCTOR)){
                        return;
                    }
                }
            }
            Response unauthorizedStatus = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Acceso denegado")
                    .build();

            requestContext.abortWith(unauthorizedStatus);
        }
    }
}
