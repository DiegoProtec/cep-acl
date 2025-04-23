package com.desafio.cep.rest;

import com.desafio.cep.application.CepService;
import com.desafio.cep.dominio.CepVo;
import com.desafio.exception.negocio.NegocioException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/cep")
public class CepResource {

    @Inject
    CepService cepService;

    @GET
    @Path("/{cep}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscaCep(@PathParam("cep") String cep) throws NegocioException {
        CepVo cepVo = cepService.buscar(cep);
        return Response.ok(cepVo).build();
    }

    @GET
    public Response listarCeps() {
        List<CepVo> ceps = cepService.buscarTodos();
        return Response.ok(ceps).build();
    }

}
