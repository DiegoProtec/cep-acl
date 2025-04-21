package com.desafio.cep.client;

import com.desafio.cep.dominio.CepVo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "viacep-api")
public interface ViaCepClient {

    @GET
    @Path("{cep}/json")
    @Produces(MediaType.APPLICATION_JSON)
    CepVo buscaCep(@PathParam("cep") String cep);

}
