package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorTipo extends BaseAdapter {
    private ArrayList<Tipo> lista;
    private Activity context;
    private EditText editText;
    private ImageButton btnGuardar;

    public AdaptadorTipo(ArrayList<Tipo> lista, Activity context, EditText et, ImageButton btnGuardar) {
        this.lista = lista;
        this.context = context;
        this.editText = et;
        this.btnGuardar = btnGuardar;
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
        return lista.get(position).getId();
    }

    @Override
    public View getView(final int posicion, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View item = inflater.inflate(R.layout.listview_administracion, null);
        TextView tvActivo = item.findViewById(R.id.tvActivo);
        final TextView nombre = item.findViewById(R.id.tvNombreVendedor);
        nombre.setText("" + lista.get(posicion).getNombre());
        ImageButton btnEdit = item.findViewById(R.id.btnEditProducto);
        final Switch swEstado = item.findViewById(R.id.swEstadoListViewProducto);
        swEstado.setId(300 + posicion);
        if (lista.get(posicion).getEstado() == 1) {
            swEstado.setChecked(true);
            tvActivo.setText("Activo");
        } else {
            swEstado.setChecked(false);
            tvActivo.setText("Desactivado");
        }
        swEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "Cambio estado ", Toast.LENGTH_SHORT).show();
                final Tipo tipo = new Tipo();
                if (isChecked) {
                    tipo.setEstado(1);
                } else {
                    tipo.setEstado(0);
                }
                tipo.setId(lista.get(posicion).getId());
                tipo.setNombre(lista.get(posicion).getNombre());

                final ProgressDialog dialog = Functions.CargarDatos("Actualizando estado", context);
                dialog.show();
                dialog.setCancelable(false);
                BddTipo.updateTipo(tipo, context, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        lista.get(posicion).setEstado(tipo.getEstado());
                        AdaptadorTipo.super.notifyDataSetChanged();
                    }
                }, Functions.FalloInternet(context, dialog, "No se pudo actualizar"));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(lista.get(posicion).getNombre());
                btnGuardar.setImageResource(R.drawable.changeblue);

            }
        });
        return item;
    }
}
