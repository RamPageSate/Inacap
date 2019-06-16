package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.CrearEditarProducto;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;


public class AdaptadorProductosAdministrador extends BaseAdapter {
    private final Activity context;
    private ArrayList<Producto> lstProducto;
    private Producto productoSeleccionado;

    public AdaptadorProductosAdministrador(Activity context, ArrayList<Producto> lstProducto) {
        this.lstProducto = lstProducto;
        this.context = context;

    }


    @Override
    public int getCount() {
        return lstProducto.size();
    }

    @Override
    public Object getItem(int position) {
        return lstProducto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int posicion, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.listviewproductos, null);
        if (!lstProducto.isEmpty()) {
            ImageView imagen = item.findViewById(R.id.imgLstVendedor);
            imagen.setId(300 + posicion);
            TextView nombre = item.findViewById(R.id.tvNombreLstProducto);
            nombre.setText("" + lstProducto.get(posicion).getNombre());
            final TextView precio = item.findViewById(R.id.tvPrecioLstProducto);
            precio.setText("$ " + lstProducto.get(posicion).getPrecio());
            ImageButton btnEdit = item.findViewById(R.id.btnEditProducto);
            TextView descripcion = item.findViewById(R.id.tvDescripcionListViewProducto);
            descripcion.setText("" + lstProducto.get(posicion).getDescripcion());
            Switch swEstado = item.findViewById(R.id.swEstadoListViewProducto);
            swEstado.setId(300 + posicion);
            if (lstProducto.get(posicion).getEstado() == 1) {
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
            imagen.setImageBitmap(Functions.StringToBitMap(lstProducto.get(posicion).getFoto()));
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productoSeleccionado = lstProducto.get(posicion);
                    Intent intent = new Intent(context, CrearEditarProducto.class);
                    intent.putExtra("producto", productoSeleccionado);
                    intent.putExtra("tipo", 7);
                    context.startActivityForResult(intent, 1);
                    //abria que sobreescrbir para recibir respuesta
                }
            });
        }

        return item;
    }
}
