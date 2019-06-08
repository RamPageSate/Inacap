package com.example.djgra.inacapdeli.Clases;

public class Tipo {
    private int id;
    private String nombre;

    public Tipo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre.toUpperCase();
    }
}
