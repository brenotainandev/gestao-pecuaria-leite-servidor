package br.edu.ifba.gestaoPecuariaLeite.servidor.impl;

import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Leite;
import br.edu.ifba.gestaoPecuariaLeite.servidor.modelo.Vaca;
import br.edu.ifba.gestaoPecuariaLeite.servidor.operacoes.Operacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OperacoesImpl implements Operacoes<Vaca, Leite> {
    private Map<Vaca, List<Leite>> bancoDeDados = new TreeMap<>();

    /**
     * complexidade linear, O(N)
     */
    @Override
    public void gravarOrdenha(Vaca monitorado, Leite sensor) {
        List<Leite> leituras = new ArrayList<>();;

        if (bancoDeDados.containsKey(monitorado)) {
            leituras = bancoDeDados.get(monitorado);
        } else {
            bancoDeDados.put(monitorado, leituras);
        }

        leituras.add(sensor);
    }

    /**
     * complexidade linear, O(N)
     */
    @Override
    public List<Vaca> detectarVacasOrdenhadas() {
        List<Vaca> vacasOrdenhadas = new ArrayList<>();

        for (Vaca vaca : bancoDeDados.keySet()) {
            List<Leite> leituras = bancoDeDados.get(vaca);
            if (!leituras.isEmpty()) {
                vacasOrdenhadas.add(vaca);
            }
        }
        return vacasOrdenhadas;
    }

}
