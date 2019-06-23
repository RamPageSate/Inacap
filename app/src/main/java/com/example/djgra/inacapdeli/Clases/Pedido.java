package com.example.djgra.inacapdeli.Clases;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public class Pedido implements Serializable {
    private int codigo, pedido_estado, id_cliente, id_condicion_pedido, id_vendedor;
    private String fechaPedido;

    ArrayList<Producto> lstProductoPedido = new ArrayList<>();
    public Pedido() {
    }

    public int getPedido_estado() {
        return pedido_estado;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public void setPedido_estado(int pedido_estado) {
        this.pedido_estado = pedido_estado;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_condicion_pedido() {
        return id_condicion_pedido;
    }

    public void setId_condicion_pedido(int id_condicion_pedido) {
        this.id_condicion_pedido = id_condicion_pedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
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



    public int totalPagarPedido(){
        int total = 0;
        for (int x=0; x< lstProductoPedido.size(); x++){
            total = total + (lstProductoPedido.get(x).getPrecio() * lstProductoPedido.get(x).getCantidad());
        }
        return total;
    }

    public int cantidadArticulos(){
        int total = 0;
        for (int x=0; x< lstProductoPedido.size(); x++){
            total = total + (lstProductoPedido.get(x).getCantidad());
        }
        return total;
    }

    public ArrayList<Producto> PedidoComprado(){
        ArrayList<Producto> lstPedido = lstProductoPedido;
        for (int x=0; x < lstPedido.size(); x++){
               agregarProductoListaPedido(lstPedido.get(x));
        }
        return  lstProductoPedido;
    }

    public void agregarProductoListaPedido(Producto producto) {
        if (lstProductoPedido.isEmpty()) {
            producto.setCantidad(1);
            lstProductoPedido.add(producto);
        }else{
            boolean existe = false;
            for(Producto pro : lstProductoPedido){
                if(pro.getCodigo() == producto.getCodigo()){
                    pro.setCantidad(pro.getCantidad() + 1);
                    existe= true;
                }
            }
            if(existe == false){
                producto.setCantidad(1);
                lstProductoPedido.add(producto);
            }
        }
    }
    public boolean quitarProductoListaPedido(Producto producto){
        boolean ok = false;
        for (int x=0; x < lstProductoPedido.size();x++){
            if(lstProductoPedido.get(x).getCodigo() == producto.getCodigo()){
                if(lstProductoPedido.get(x).getCantidad() == 1){
                    lstProductoPedido.remove(x);
                    ok = true;
                }else{
                    lstProductoPedido.get(x).setCantidad(lstProductoPedido.get(x).getCantidad() - 1);
                    ok = false;
                }
                break;
            }
        }
        return ok;
    }

    public static ArrayList<Producto> listaProductosFinal(ArrayList<Producto> lst){
        ArrayList<Producto> lista = new ArrayList<>();
        for(int x=0 ; x <lst.size(); x++){
            if(lst.get(x).getCantidad() > 1){
                for(int c=0; c < lst.get(x).getCantidad(); c++){
                    lista.add(lst.get(x));
                }
            }else{
                lista.add(lst.get(x));
            }
        }
        for (int x=0; x < lista.size(); x++ ){
            lista.get(x).setCantidad(1);
        }
        return lista;
    }

    public static ArrayList<Producto> filtrarPedidos(ArrayList<Producto> lst){
        ArrayList<Producto> lista = new ArrayList<>();
        for (int x=0; x < lst.size(); x++){
            if(lista.isEmpty()){
                lista.add(lst.get(x));
            }else{
                for(int c=0; c < lista.size(); c++){
                    if(lista.get(c).getCodigo() != lst.get(x).getCodigo()){
                        lista.add(lst.get(x));
                    }
                }
            }
        }

        return lista;
    }

}
