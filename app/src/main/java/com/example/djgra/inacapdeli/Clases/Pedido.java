package com.example.djgra.inacapdeli.Clases;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {
    int codigo;
    ArrayList<Producto> lstProductoPedido = new ArrayList<>();

    public Pedido() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public ArrayList<Producto> getLstProductoPedido() {
        return lstProductoPedido;
    }

    public void setLstProductoPedido(ArrayList<Producto> lstProductoPedido) {
        this.lstProductoPedido = lstProductoPedido;
    }

    public void agregarProductoListaPedido(Producto producto) {
        if (lstProductoPedido.isEmpty()) {
            producto.setCantidad(1);
            lstProductoPedido.add(producto);
            Log.d("TAG_","agreggo primer prodcuto ");
        }else{
            boolean existe = false;
            for(Producto pro : lstProductoPedido){
                if(pro.getCodigo() == producto.getCodigo()){
                    pro.setCantidad(pro.getCantidad() + 1);
                    existe= true;
                    Log.d("TAG_","repitio prodcuto ");
                }
            }
            if(existe == false){
                producto.setCantidad(1);
                lstProductoPedido.add(producto);
                Log.d("TAG_","agreggo prodcuto nuevo");
            }
        }
    }
    public void quitarProductoListaPedido(Producto producto){
        for (int x=0; x < lstProductoPedido.size();x++){
            if(lstProductoPedido.get(x).getCodigo() == producto.getCodigo()){
                if(producto.getCantidad() == 1){
                    lstProductoPedido.remove(x);
                }else{
                    lstProductoPedido.get(x).setCantidad(lstProductoPedido.get(x).getCantidad() - 1);
                }
                break;
            }
        }
    }
}
