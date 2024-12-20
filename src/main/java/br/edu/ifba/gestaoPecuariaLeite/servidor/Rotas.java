package br.edu.ifba.gestaoPecuariaLeite.servidor;

import br.edu.ifba.gestaoPecuariaLeite.servidor.impl.OperacoesImpl;
import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Leite;
import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Vaca;
import br.edu.ifba.gestaoPecuariaLeite.servidor.operacoes.Operacoes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Path("gestao-leite")
public class Rotas {

    private static Operacoes<Vaca, Leite> operacoes = null;
    public static Operacoes<Vaca, Leite> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }
        return operacoes;
    }

    @GET
    @Path("informacoes")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInformacoes() {
        return "Servidor de gestão de pecuária de leite, versão 1.0";
    }

    @GET
    @Path("ordenha/{idVaca}/{nomeVaca}/{quantidadeLeite}/{producaoBaixa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String ordenha(
            @PathParam("idVaca") String idVaca,
            @PathParam("nomeVaca") String nomeVaca,
            @PathParam("quantidadeLeite") Integer quantidadeLeite,
            @PathParam("producaoBaixa") Integer producaoBaixa
    ) {
        try {
            idVaca = URLDecoder.decode(idVaca, StandardCharsets.UTF_8.toString());
            nomeVaca = URLDecoder.decode(nomeVaca, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Erro ao decodificar parâmetros: " + e.getMessage();
        }
        Vaca vaca = new Vaca(idVaca, nomeVaca, true);
        Leite leite = new Leite(quantidadeLeite);
        try {
            getOperacoes().gravarOrdenha(vaca, leite);
            System.out.println("recebidos dados de ordenha: " + vaca + ", " + leite);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao gravar ordenha: " + e.getMessage();
        }
        return "ok";
    }

    @GET
    @Path("vacas-ordenhadas")
    @Produces(MediaType.TEXT_PLAIN)
    public String vacasOrdenhadas() {
        return getOperacoes().detectarVacasOrdenhadas().toString();
    }
}