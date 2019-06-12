package com.example.djgra.inacapdeli;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.example.djgra.inacapdeli.R.layout.listviewproductos;

public class ProductoActivity extends AppCompatActivity {
    ImageButton btnSalir, btnAgregar;
    Spinner spCategorias;
    ListView lstvProductos;
    Producto productoSeleccionado ;
    public static ArrayList<Producto> lstProductos = new ArrayList<>();
    public static ArrayList<Categoria> lstCategoria = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        btnAgregar = findViewById(R.id.btnAddLstProductos);
        btnSalir = findViewById(R.id.btnSalirLstProducto);
        spCategorias = findViewById(R.id.spCategoriaLstProducto);
        lstvProductos = findViewById(R.id.lstvProductos);
        final AdapterProductos adapterProductos = new AdapterProductos(this);
        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<Categoria>(this,android.R.layout.simple_list_item_1,lstCategoria);
        lstvProductos.setAdapter(adapterProductos);
        spCategorias.setAdapter(adapterCategoria);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this,CrearEditarProducto.class);
                startActivity(intent);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductoActivity.this, PrincipalAdministrador.class);
                startActivity(i);
                finish();
            }
        });

        //faltan hacer los filtros------------------------------------------------------------------------------------------------ y transformar las fotos


    }

    class AdapterProductos extends ArrayAdapter<Producto> {
        Activity context;

        public AdapterProductos(Activity context) {
            super(context, R.layout.listviewproductos, lstProductos);
            this.context = context;
        }

        public View getView(final int posicion, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(listviewproductos, null);
            if (!lstProductos.isEmpty()) {
                TextView nombre = item.findViewById(R.id.tvNombreLstProducto);
                nombre.setText(""+lstProductos.get(posicion).getNombre());
                final TextView precio = item.findViewById(R.id.tvPrecioLstProducto);
                precio.setText("$ " + lstProductos.get(posicion).getPrecio());
                ImageButton btnEdit = item.findViewById(R.id.btnEditProducto);
                TextView descripcion = item.findViewById(R.id.tvDescripcionListViewProducto);
                descripcion.setText(""+ lstProductos.get(posicion).getDescripcion());
                Switch swEstado = item.findViewById(R.id.swEstadoListViewProducto);
                swEstado.setId(300+posicion);
                if(lstProductos.get(posicion).getEstado() == 1){
                    swEstado.setChecked(true);
                }else{
                    swEstado.setChecked(false);
                }
                swEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(context, "Cambio estado " , Toast.LENGTH_SHORT).show();
                    }
                });
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productoSeleccionado = (Producto) lstvProductos.getItemAtPosition(posicion);
                        Intent intent = new Intent(ProductoActivity.this,CrearEditarProducto.class);
                        intent.putExtra("producto",productoSeleccionado);
                        startActivity(intent);
                        //abria que sobreescrbir para recibir respuesta
                    }
                });
            }


            return item;
        }
    }
}
