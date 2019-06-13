package com.example.djgra.inacapdeli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

import static com.example.djgra.inacapdeli.R.layout.listviewproductos;

public class ProductoActivity extends AppCompatActivity {
    ImageButton btnSalir, btnAgregar;
    Spinner spCategorias;
    ListView lstvProductos;
    Producto productoSeleccionado ;
    private Date fecha;
    public static ArrayList<Producto> lstProductos = new ArrayList<>();
    public static ArrayList<Categoria> lstCategoria = new ArrayList<>();
    AdapterProductos adapterProductos;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        btnAgregar = findViewById(R.id.btnAddLstProductos);
        btnSalir = findViewById(R.id.btnSalirLstProducto);
        spCategorias = findViewById(R.id.spCategoriaLstProducto);
        lstvProductos = findViewById(R.id.lstvProductos);
        adapterProductos = new AdapterProductos(this,lstProductos);
        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<Categoria>(this,android.R.layout.simple_list_item_1,lstCategoria);
        lstvProductos.setAdapter(adapterProductos);
        spCategorias.setAdapter(adapterCategoria);




        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this,CrearEditarProducto.class);
                intent.putExtra("tipo",23);
                startActivityForResult(intent,1);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstProductos.removeAll(lstProductos);
                Intent i = new Intent(ProductoActivity.this, PrincipalAdministrador.class);
                startActivity(i);
                finish();
            }
        });

        spCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Categoria cat = (Categoria) spCategorias.getAdapter().getItem(position);
                for (int i = 0; i < lstProductos.size(); i++) {
                    lstProductos.get(i).getLstCategoriasProducto();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        fecha = new Date();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Date devuelta = new Date();
        if (fecha.getTime() - devuelta.getTime() < 5) {
            Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
        }
    }

    class AdapterProductos extends ArrayAdapter<Producto> {
        Activity context;

        public AdapterProductos(Activity context,final ArrayList<Producto> lst) {
            super(context, R.layout.listviewproductos, lst);
            this.context = context;
        }

        public View getView(final int posicion, View view, ViewGroup parent, ArrayList<Producto> lst) {

            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(listviewproductos, null);
            if (!lst.isEmpty()) {
                ImageView imagen = item.findViewById(R.id.imgLstVendedor);
                imagen.setId(300+posicion);
                TextView nombre = item.findViewById(R.id.tvNombreLstProducto);
                nombre.setText(""+lst.get(posicion).getNombre());
                final TextView precio = item.findViewById(R.id.tvPrecioLstProducto);
                precio.setText("$ " + lst.get(posicion).getPrecio());
                ImageButton btnEdit = item.findViewById(R.id.btnEditProducto);
                TextView descripcion = item.findViewById(R.id.tvDescripcionListViewProducto);
                descripcion.setText(""+ lst.get(posicion).getDescripcion());
                Switch swEstado = item.findViewById(R.id.swEstadoListViewProducto);
                swEstado.setId(300+posicion);
                if(lst.get(posicion).getEstado() == 1){
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
                imagen.setImageBitmap(Functions.StringToBitMap(lstProductos.get(posicion).getFoto()));
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productoSeleccionado = (Producto) lstvProductos.getItemAtPosition(posicion);
                        Intent intent = new Intent(ProductoActivity.this,CrearEditarProducto.class);
                        intent.putExtra("producto",productoSeleccionado);
                        intent.putExtra("tipo",7);
                        startActivityForResult(intent,1);
                        //abria que sobreescrbir para recibir respuesta
                    }
                });
            }


            return item;
        }
    }


    public void actualizarListView(){
        lstProductos.removeAll(lstProductos);
        final ProgressDialog progressDialog = Functions.CargarDatos("Cangando Productos", ProductoActivity.this);
        BddProductos.getProducto(ProductoActivity.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            Log.d("TAG_", "cargara los productos");
                            final Producto producto = new Producto();
                            producto.setCodigo(response.getJSONObject(x).getInt("producto_id"));
                            producto.setNombre(response.getJSONObject(x).getString("producto_nombre"));
                            producto.setFoto(response.getJSONObject(x).getString("producto_foto"));
                            producto.setDescripcion(response.getJSONObject(x).getString("producto_descripcion"));
                            producto.setSku(response.getJSONObject(x).getString("producto_sku"));
                            producto.setPrecio(response.getJSONObject(x).getInt("producto_precio"));
                            producto.setStock(response.getJSONObject(x).getInt("producto_stock"));
                            producto.setEstado(response.getJSONObject(x).getInt("producto_estado"));
                            producto.setId_fabricante(response.getJSONObject(x).getInt("id_fabricante"));
                            producto.setId_tipo(response.getJSONObject(x).getInt("id_tipo"));
                            BddCategoria.getCategoriaByProducto(producto.getCodigo(), ProductoActivity.this, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (!response.toString().equals("[]")) {
                                        ArrayList<Categoria> categoriasProducto = new ArrayList<>();
                                        for (int x = 0; x < response.length(); x++) {
                                            try {//falta agrgar las categorias asignadas al producto
                                                Categoria categoria = new Categoria(response.getJSONObject(x).getInt("categoria_id"), response.getJSONObject(x).getInt("categoria_estado"), response.getJSONObject(x).getString("categoria_nombre"));
                                                categoriasProducto.add(categoria);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        producto.setLstCategoriasProducto(categoriasProducto);
                                    }
                                }
                            }, Functions.FalloInternet(ProductoActivity.this, progressDialog, "No pudo Cargar"));
                            lstProductos.add(producto);
                            lstvProductos.setAdapter(adapterProductos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },Functions.FalloInternet(ProductoActivity.this,progressDialog,"no pudo Cargar"));
    }

}

