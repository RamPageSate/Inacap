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

    public AdaptadorPedidosQr(ArrayList<Pedido> lstPedido, Activity context) {
        this.lstPedido = lstPedido;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.retirarpedidoqr,null);
        TextView tvNumeroPedido = item.findViewById(R.id.tvNPedidoEntregarQR);
        TextView tvCantidaArticulo = item.findViewById(R.id.tvCantidadQr);
        CheckBox cbRetira = item.findViewById(R.id.cbRetiraQr);
        cbRetira.setId(300 + position);
        tvNumeroPedido.setText(String.valueOf(lstPedido.get(position).getCodigo()));
        tvCantidaArticulo.setText(String.valueOf(lstPedido.get(position).cantidadArticulos()));



        return item;
    }
}
