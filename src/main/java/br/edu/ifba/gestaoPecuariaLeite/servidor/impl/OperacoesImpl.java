package br.edu.ifba.gestaoPecuariaLeite.servidor.impl;

import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Leite;
import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Vaca;
import br.edu.ifba.gestaoPecuariaLeite.servidor.operacoes.Operacoes;

import java.util.*;

public class OperacoesImpl implements Operacoes<Vaca, Leite> {

    private Map<Vaca, List<Leite>> bancoDeDados = new TreeMap<>();
    private static final int PRODUCAO_ABAXO_MEDIA = 0;

    /**
     * Registra a ordenha de uma vaca, associando a quantidade de leite produzida.
     * Se a vaca ainda não existir no banco de dados, ela será adicionada manualmente.
     *
     * Complexidade: O(N), onde N é o número de vacas no banco de dados.
     *
     * @param vaca   A vaca monitorada.
     * @param leite  A quantidade de leite produzida.
     */
    @Override
    public void gravarOrdenha(Vaca vaca, Leite leite) {
        List<Leite> leituras = bancoDeDados.get(vaca);

        // Se a vaca não estiver no banco de dados, inicializa a lista e adiciona a vaca.
        if (leituras == null) {
            leituras = new ArrayList<>();
            bancoDeDados.put(vaca, leituras);
        }

        // Adiciona a leitura de leite à lista.
        leituras.add(leite);
    }

    /**
     * Retorna a lista de vacas que possuem registros de ordenha.
     *
     * Complexidade: O(N), onde N é o número de vacas no banco de dados.
     *
     * @return Lista de vacas com ordenhas registradas.
     */
    @Override
    public List<Vaca> detectarVacasOrdenhadas() {
        List<Vaca> vacasOrdenhadas = new ArrayList<>();

        // Percorre o banco de dados e verifica manualmente registros de ordenha.
        for (Map.Entry<Vaca, List<Leite>> entry : bancoDeDados.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                vacasOrdenhadas.add(entry.getKey());
            }
        }

        return vacasOrdenhadas;
    }
}
