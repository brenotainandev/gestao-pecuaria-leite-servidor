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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("gestao-leite")
public class Rotas {

    private static final String ALGORITMO_DE_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PRIVADA = "/home/breno/projects/complexidade_de_dlgoritmos/gestao-pecuaria-leite-servidor/chave/privada.chv";

    private PrivateKey chave = null;

    private PrivateKey getChavePrivada() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (chave == null) {
            File arquivo = new File(CAMINHO_CHAVE_PRIVADA);
            FileInputStream stream = new FileInputStream(arquivo);
            byte[] bytes = stream.readAllBytes();
            stream.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(ALGORITMO_DE_ENCRIPTACAO);
            chave = kf.generatePrivate(spec);
        }

        return chave;
    }

    private String desencriptar(byte[] encriptado) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITMO_DE_ENCRIPTACAO);
        cipher.init(Cipher.DECRYPT_MODE, getChavePrivada());

        byte[] desencriptado = cipher.doFinal(encriptado);

        return new String(desencriptado);
    }

    private static Operacoes<Vaca, Leite> operacoes = null;
    private static final int PRODUCAO_DENTRO_DA_MEDIA = 1;

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
    @Path("ordenha/{encriptado}")
    @Produces(MediaType.TEXT_PLAIN)
    public String ordenha(@PathParam("encriptado") String encriptado) {
        try {
            String json = desencriptar(Base64.getUrlDecoder().decode(encriptado));

            ObjectMapper mapeador = new ObjectMapper();
            JsonNode dicionario = mapeador.readTree(json);

            String idVaca = dicionario.get("id").asText();
            String nomeVaca = dicionario.get("nome").asText();
            int quantidadeLeite = dicionario.get("quantidade").asInt();
            int producaoBaixa = dicionario.get("producaoBaixa").asInt();

            boolean producaoOk = producaoBaixa == PRODUCAO_DENTRO_DA_MEDIA;
            Vaca vaca = new Vaca(idVaca, nomeVaca, producaoOk);
            Leite leite = new Leite(quantidadeLeite);

            getOperacoes().gravarOrdenha(vaca, leite);
            System.out.println("recebidos dados de ordenha: " + vaca + ", " + leite);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar ordenha: " + e.getMessage();
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