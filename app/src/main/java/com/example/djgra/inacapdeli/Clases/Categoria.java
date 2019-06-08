package com.example.djgra.inacapdeli.Clases;

public class Categoria {
    private int codigo, estado;
    private String nombre;

       public Categoria(int codigo, int estado, String nombre) {
        this.codigo = codigo;
        this.estado = estado;
        this.nombre = nombre;
    }
    public Categoria(int estado, String nombre) {
        this.estado = estado;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
