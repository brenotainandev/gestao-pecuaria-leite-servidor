package br.edu.ifba.gestaoPecuariaLeite.servidor.operacoes;

import java.util.List;

public interface Operacoes<Monitorado, Sensor> {
    void gravarOrdenha(Monitorado monitorado, Sensor sensor);
    List<Monitorado> detectarVacasOrdenhadas();
}
