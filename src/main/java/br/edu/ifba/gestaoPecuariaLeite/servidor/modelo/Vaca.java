package br.edu.ifba.gestaoPecuariaLeite.servidor.modelo;

public class Vaca implements Comparable<Vaca> {
    private String id = "";
    private String nome = "";
    private boolean estaOrdenhada = false;

    public Vaca(String id, String nome, boolean estaOrdenhada) {
        this.id = id;
        this.nome = nome;
        this.estaOrdenhada = estaOrdenhada;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isEstaOrdenhada() {
        return estaOrdenhada;
    }

    @Override
    public String toString() {
        return "id: " + id + ", nome: " + nome;
    }

    @Override
    public int compareTo(Vaca outraVaca) {
        return Integer.compare(Integer.parseInt(this.id), Integer.parseInt(outraVaca.id));
    }
}