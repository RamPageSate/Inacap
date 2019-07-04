package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorPedidosQr extends BaseAdapter {
    ArrayList<Pedido> lstPedido;
    Activity context;
    CheckBox cbRetirarTodo;

    public AdaptadorPedidosQr(ArrayList<Pedido> lstPedido, Activity context, CheckBox cbRetirarTodo) {
        this.lstPedido = lstPedido;
        this.context = context;
        this.cbRetirarTodo = cbRetirarTodo;
    }

    @Override
    public int getCount() {
        return lstPedido.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstPedido.get(position).getCodigo();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.retirarpedidoqr,null);
        TextView tvNumeroPedido = item.findViewById(R.id.tvNPedidoEntregarQR);
        TextView tvCantidaArticulo = item.findViewById(R.id.tvCantidadQr);
        final CheckBox cbRetira = item.findViewById(R.id.cbRetiraQr);
        cbRetira.setId(300 + position);
        tvNumeroPedido.setText(String.valueOf(lstPedido.get(position).getCodigo()));
        tvCantidaArticulo.setText(String.valueOf(lstPedido.get(position).cantidadArticulos()));
        cbRetira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRetira.isChecked() == true){
                    lstPedido.get(position).setId_condicion_pedido(5);
                }
            }
        });
        cbRetirarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRetirarTodo.isChecked() == true){

                    cbRetira.setChecked(true);
                    //poner todos en condicion 5    solo marca 1
                }else{
                    cbRetira.setChecked(false);
                }
            }
        });

        return item;
    }
}
