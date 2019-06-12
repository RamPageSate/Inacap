package com.example.djgra.inacapdeli.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {

    private String nombre, foto, descripcion, sku;
    private int precio, stock, estado, id_fabricante, id_tipo, codigo;
    private ArrayList<Categoria> lstCategoriasProducto = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }


    public ArrayList<Categoria> getLstCategoriasProducto() {
        return lstCategoriasProducto;
    }

    public void setLstCategoriasProducto(ArrayList<Categoria> lstCategoriaProducto) {
        this.lstCategoriasProducto = lstCategoriaProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId_fabricante() {
        return id_fabricante;
    }

    public void setId_fabricante(int id_fabricante) {
        this.id_fabricante = id_fabricante;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
