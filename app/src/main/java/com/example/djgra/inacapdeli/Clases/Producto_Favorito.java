package com.example.djgra.inacapdeli.Clases;

import java.io.Serializable;

public class Producto_Favorito implements Serializable {
    int id_cliente, id_producto, like;


    public Producto_Favorito() {
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }




}
