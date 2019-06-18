package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
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

import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorFabricante extends BaseAdapter {
    private final Activity context;
    private ArrayList<Fabricante> lista;
    private EditText editText;

    public AdaptadorFabricante(ArrayList<Fabricante> lista, Activity context, EditText et) {
        this.lista = lista;
        this.context = context;
        this.editText = et;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int posicion, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.listview_administracion, null);
        if (!lista.isEmpty()) {


            final TextView nombre = item.findViewById(R.id.tvNombreLstProducto);
            nombre.setText("" + lista.get(posicion).getNombre());
            ImageButton btnEdit = item.findViewById(R.id.btnEditProducto);
            Switch swEstado = item.findViewById(R.id.swEstadoListViewProducto);
            swEstado.setId(300 + posicion);
            if (lista.get(posicion).getEstado() == 1) {
                swEstado.setChecked(true);
            } else {
                swEstado.setChecked(false);
            }
            swEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(context, "Cambio estado ", Toast.LENGTH_SHORT).show();
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(lista.get(posicion).getNombre());
                    //abria que sobreescrbir para recibir respuesta
                }
            });
        }
        return item;
    }
}
