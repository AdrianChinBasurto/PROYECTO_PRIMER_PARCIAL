package com.example.proyecto_primer_parcial;

public class Mensaje {
    private int id;
    private String texto;

    public Mensaje(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

