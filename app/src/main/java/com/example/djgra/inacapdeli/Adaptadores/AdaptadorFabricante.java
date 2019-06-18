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
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorFabricante extends BaseAdapter {
    private final Activity context;
    private ArrayList<Fabricante> lista;
    private EditText editText;
    private ImageButton btnGuardar;

    public AdaptadorFabricante(Activity context, ArrayList<Fabricante> lista, EditText et, ImageButton btnGuardar) {
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
        return lista.get(position).getCodigo();
    }

    @Override
    public View getView(final int posicion, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View item = inflater.inflate(R.layout.listview_administracion, null);
        if (!lista.isEmpty()) {

            TextView tvActivo = item.findViewById(R.id.tvActivo);
            final TextView nombre = item.findViewById(R.id.tvNombreLstProducto);
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
                    final Fabricante fabricante = new Fabricante();
                    if (isChecked) {
                        fabricante.setEstado(1);
                    } else {
                        fabricante.setEstado(0);
                    }
                    fabricante.setCodigo(lista.get(posicion).getCodigo());
                    fabricante.setNombre(lista.get(posicion).getNombre());

                    final ProgressDialog dialog = Functions.CargarDatos("Actualizando estado", context);
                    dialog.show();
                    dialog.setCancelable(false);
                    BddFabricante.updateFabricante(fabricante, context, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            lista.get(posicion).setEstado(fabricante.getEstado());
                            AdaptadorFabricante.super.notifyDataSetChanged();
                        }
                    }, Functions.FalloInternet(context, dialog, "Fallo la conexion"));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(lista.get(posicion).getNombre());
                    btnGuardar.setImageResource(R.drawable.changeblue);

                }
            });
        }
        return item;
    }
}
