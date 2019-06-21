package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorVendedor extends BaseAdapter {
    private ArrayList<Persona> lista;
    private Activity context;

    public AdaptadorVendedor(Activity context, ArrayList<Persona> lista) {
        this.lista = lista;
        this.context = context;

    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getCodigo();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View item = inflater.inflate(R.layout.listviewvendedores, null);
        ImageView imgVendedor = item.findViewById(R.id.imgLstVendedor);
        TextView nombre = item.findViewById(R.id.tvNombreVendedor);
        TextView email = item.findViewById(R.id.tvEmailVendedor);
        TextView sede = item.findViewById(R.id.tvSedeLstVendedor);
        ImageButton swEstado = item.findViewById(R.id.swEstadoVendedor);
        ImageButton btnQuitar = item.findViewById(R.id.btnQuitarVendedor);
        TextView estado = item.findViewById(R.id.tvEstadoVendedor);
        swEstado.setId(300 + position);
        imgVendedor.setImageBitmap(Functions.StringToBitMap(lista.get(position).getFoto()));
        nombre.setText(lista.get(position).getNombre() + " " + lista.get(position).getApellido());
        email.setText(lista.get(position).getCorreo());

        sede.setText(lista.get(position).getSede());
        return item;
    }
}
