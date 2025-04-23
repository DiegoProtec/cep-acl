package com.desafio.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class IndexResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index() {
        var pagina = """
                <h1>Bem vindo!</h1>
                
                <h2>API de serviços para gerenciamento de CEP </h2>
                """;
        return Response.ok(pagina).build();
    }

}
