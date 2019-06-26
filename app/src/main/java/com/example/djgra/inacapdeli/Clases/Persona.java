package com.example.djgra.inacapdeli.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Persona implements Serializable{
    private String nombre, apellido, correo, contrasena, foto, codigoQr; // hay que cambiar la sede
    private int codigo, saldo, estado, sede, rol;
    private ArrayList<Pedido> lstPedidos = new ArrayList<>();
    private ArrayList<Producto_Favorito> lstProductosFavoritos =  new ArrayList<>();
    public Persona() {
    }


    public ArrayList<Pedido> getLstPedidos() {
        return lstPedidos;
    }

    public void setLstPedidos(ArrayList<Pedido> lstPedidos) {
        this.lstPedidos = lstPedidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getSede() {
        return sede;
    }

    public void setSede(int sede) {
        this.sede = sede;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public void agregarPedido(Pedido pedido){
        lstPedidos.add(pedido);
    }

    public ArrayList<Pedido> lstPedidosPendientes(){
        ArrayList<Pedido> lista = new ArrayList<>();
        for(int x=0; x< lstPedidos.size(); x++){
            if(lstPedidos.get(x).getId_condicion_pedido() == 2){
                lista.add(lstPedidos.get(x));
            }
        }
        return lista;
    }

    public ArrayList<Pedido> lstPedidosEntregados(){
        ArrayList<Pedido> lista = new ArrayList<>();
        for(int x=0; x< lstPedidos.size(); x++){
            if(lstPedidos.get(x).getId_condicion_pedido() == 1){
                lista.add(lstPedidos.get(x));
            }
        }
        return lista;
    }

    public ArrayList<Producto_Favorito> getLstProductosFavoritos() {
        return lstProductosFavoritos;
    }

    public void setLstProductosFavoritos(ArrayList<Producto_Favorito> lstProductosFavoritos) {
        this.lstProductosFavoritos = lstProductosFavoritos;
    }

    public boolean ProductoFavorito(Producto_Favorito item){
        boolean ok = false;
        if(lstProductosFavoritos.isEmpty()){
            item.setLike(1);
            lstProductosFavoritos.add(item);
            ok = true;
        }else{
            for(Producto_Favorito pro : lstProductosFavoritos){
                if(pro.getId_producto() == item.getId_producto()){
                    lstProductosFavoritos.remove(pro);
                    ok = false;
                }else{
                    lstProductosFavoritos.add(item);
                    ok = true;
                }
            }
        }
        return  ok;
    }

    public void addProductoFavorito(Producto_Favorito pro){
        lstProductosFavoritos.add(pro);
    }

    public void terminarPedido(Pedido pedido){
        for(int x=0; x < lstPedidos.size(); x++){
            if(lstPedidos.get(x).getId_cliente() == pedido.getId_cliente()){
                lstPedidos.remove(pedido);
            }
        }
    }




}
