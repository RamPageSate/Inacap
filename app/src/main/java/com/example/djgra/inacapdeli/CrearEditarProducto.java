package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CrearEditarProducto extends AppCompatActivity {
    ImageView imgAgregarFoto;
    public static ArrayList<Tipo> listaTipo = new ArrayList<>();
    public static ArrayList<Fabricante> listaFabricante = new ArrayList<>();
    public static ArrayList<Categoria> listaCategoria = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_editar_producto);
        final ProgressDialog progressDialog = Functions.CargarDatos("Espere..", CrearEditarProducto.this);
        BddTipo.getTipo(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")){
                    for (int x = 0; x < response.length(); ++x) {
                        try {
                            Fabricante fabricante = new Fabricante();
                            fabricante.setCodigo(response.getJSONObject(x).getInt("fabricante_id"));
                            fabricante.setNombre(response.getJSONObject(x).getString("fabricante_nombre"));
                            listaFabricante.add(fabricante);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                BddTipo.getTipo(CrearEditarProducto.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    Tipo tipo = new Tipo();
                                    tipo.setId(response.getJSONObject(x).getInt("tipo_id"));
                                    tipo.setNombre(response.getJSONObject(x).getString("tipo"));
                                    listaTipo.add(tipo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        BddCategoria.getCategoria(CrearEditarProducto.this, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        listaCategoria.add(new Categoria(response.getJSONObject(i).getInt("categoria_id"),
                                                response.getJSONObject(i).getInt("categoria_estado"),
                                                response.getJSONObject(i).getString("categoria_nombre")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, Functions.FalloInternet(CrearEditarProducto.this,progressDialog,"No pudo Cargar"));
                    }
                }, Functions.FalloInternet(CrearEditarProducto.this,progressDialog,"No pudo Cargar"));
            }
        }, Functions.FalloInternet(CrearEditarProducto.this,progressDialog,"No pudo Cargar"));
        final EditText etNombreProducto =(EditText) findViewById(R.id.etNombreProducto);
        final EditText etDescripcionProducto = (EditText) findViewById(R.id.etDescripcionProducto);
        final EditText etCodigoBarraProducto = (EditText) findViewById(R.id.etSkuProducto);
        final EditText etPrecioProducto = (EditText) findViewById(R.id.etPrecioProductos);
        Spinner spFabricante = (Spinner) findViewById(R.id.spFabricantesProductos);
        Spinner spTipo =(Spinner) findViewById(R.id.spTipoProducto);
        imgAgregarFoto = (ImageView) findViewById(R.id.imgAgregarFotoProducto);
        Button btnAgregarCategrias = (Button) findViewById(R.id.btnCategoriasAgregarProducto);
        ImageButton btnAgregarProducto = (ImageButton) findViewById(R.id.imgAgregarProducto);
        ImageButton btnSalir = (ImageButton) findViewById(R.id.btnSalirProducto);
        spFabricante.setAdapter(new ArrayAdapter<Fabricante>(CrearEditarProducto.this,android.R.layout.simple_list_item_1,listaFabricante));
        spTipo.setAdapter(new ArrayAdapter<Tipo>(CrearEditarProducto.this,android.R.layout.simple_list_item_1,listaTipo));



    }
}
