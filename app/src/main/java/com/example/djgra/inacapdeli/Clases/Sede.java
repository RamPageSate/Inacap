package com.example.djgra.inacapdeli.Clases;

public class Sede {

    private int codigo, estado;
    private String direccion;

    public Sede(int codigo, int estado, String direccion) {
        this.codigo = codigo;
        this.estado = estado;
        this.direccion = direccion;
    }

    public Sede() {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return direccion.toString().toUpperCase();
    }
}
