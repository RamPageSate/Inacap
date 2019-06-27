package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorProductosPorPreparar extends BaseAdapter {
    ArrayList<Producto> listaProducto;
    Activity context;
    Button btnTerminarPedido;

    public AdaptadorProductosPorPreparar(ArrayList<Producto> listaProducto, Button btnTerminarPedido, Activity context) {
        this.listaProducto = listaProducto;
        this.context = context;
        this.btnTerminarPedido = btnTerminarPedido;
    }

    @Override
    public int getCount() {
        return listaProducto.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProducto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaProducto.get(position).getCodigo();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.prepararpedidoproductos, null);
        final TextView tvNombre = item.findViewById(R.id.tvProductoPrepararPedido);
        TextView tvCantidad = item.findViewById(R.id.tvCantidadProductoPrepararPedido);
        RadioGroup rdGrupo = item.findViewById(R.id.rdGrupo);
        final RadioButton rdFalta = item.findViewById(R.id.rdFalta);
        final RadioButton rdListo = item.findViewById(R.id.rdListo);
        tvNombre.setText(listaProducto.get(position).getNombre());
        tvCantidad.setText(String.valueOf(listaProducto.get(position).getCantidad()));
        rdFalta.setId(200 + position);
        rdListo.setId(400 + position);
        rdGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rdListo.isChecked()){
                    rdListo.setChecked(true);
                    listaProducto.get(position).setEstado(0);
                    rdFalta.setChecked(false);
                    if(habilitarBotonTerminar() == false){
                        btnTerminarPedido.setEnabled(true);
                        btnTerminarPedido.setBackgroundColor(Color.parseColor("#972CD6"));

                    }
                }else{
                    listaProducto.get(position).setEstado(1);
                    rdListo.setChecked(false);
                    if(habilitarBotonTerminar() == true){
                        btnTerminarPedido.setEnabled(false);
                        btnTerminarPedido.setBackgroundColor(Color.parseColor("#808080"));
                    }
                }
            }
        });
        return item;
    }

    private boolean habilitarBotonTerminar(){
        boolean ok = false;
        for (int x=0;  x < listaProducto.size(); x++){
            if(listaProducto.get(x).getEstado() == 1){
                ok = true;
                break;
            }
        }
        return ok;
    }
}
