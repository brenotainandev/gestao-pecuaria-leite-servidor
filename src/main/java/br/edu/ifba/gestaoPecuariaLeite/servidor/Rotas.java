package br.edu.ifba.gestaoPecuariaLeite.servidor;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("gestao-leite")
public class Rotas {

    @GET
    @Path("informacoes")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInformacoes() {
        return "Servidor de gestão de pecuária de leite, versão 1.0";
    }

}
