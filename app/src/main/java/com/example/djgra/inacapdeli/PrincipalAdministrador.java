package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogCategoria;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogFabricantes;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogSede;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogTipo;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogVendedores;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddSede;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class PrincipalAdministrador extends AppCompatActivity {
    ImageButton btnCategoria, btnLstVendedores, btnSede, btnFabricante, btnProductos, btnTipo, btnCerrarSesion;
    private int btn = 0, posicionUdpdateDelete = 0;
    private static ArrayList<Categoria> lstCategorias = new ArrayList<>();
    private static ArrayList<Sede> lstSedes = new ArrayList<>();
    private static ArrayList<Tipo> lstTipo  = new ArrayList<>();
    private static ArrayList<Persona> lstPersonas = new ArrayList<>();
    private static ArrayList<Fabricante> lstFabricantes = new ArrayList<>();
    private AlertDialogFabricantes fabricantes;
    private AlertDialogCategoria categorias;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_administrador);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnCategoria = findViewById(R.id.btnAdCategoria);
        btnTipo = findViewById(R.id.btnTipo);
        btnProductos = findViewById(R.id.btnProductos);
        btnFabricante = findViewById(R.id.btnFabricante);
        btnLstVendedores = findViewById(R.id.btnLstVendedores);
        btnSede = findViewById(R.id.btnSede);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesionAdministrador);

// region categorias CRUD
        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Functions.CargarDatos("Cargando Categorias", PrincipalAdministrador.this);
                BddCategoria.getCategoria(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    lstCategorias.add(new Categoria(response.getJSONObject(i).getInt("categoria_id"),
                                            response.getJSONObject(i).getInt("categoria_estado"),
                                            response.getJSONObject(i).getString("categoria_nombre")));
                                    ProductoActivity.lstCategoria.add(lstCategorias.get(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressDialog.hide();
                        Collections.reverse(lstCategorias);
                        categorias = new AlertDialogCategoria(PrincipalAdministrador.this, lstCategorias);

                        categorias.show();

                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No pudo actualizar"));


            }
        });
        //endregion CRUD

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("usuarioConectado", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorPreferencias = preferences.edit();
                editorPreferencias.clear();
                editorPreferencias.commit();
                Intent intent =  new Intent(PrincipalAdministrador.this, Inicio.class);
                startActivity(intent);
            }
        });

        //region productos
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Functions.CargarDatos("Cargando Productos", PrincipalAdministrador.this);
                BddProductos.getProducto(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); x++) {
                                try {
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
                                    BddCategoria.getCategoriaByProducto(producto.getCodigo(), PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            if (!response.toString().equals("[]")) {
                                                ArrayList<Categoria> categoriasProducto = new ArrayList<>();
                                                for (int x=0; x < response.length();x++){
                                                    try {
                                                        Categoria categoria = new Categoria(response.getJSONObject(x).getInt("categoria_id"), response.getJSONObject(x).getInt("categoria_estado"), response.getJSONObject(x).getString("categoria_nombre"));
                                                        categoriasProducto.add(categoria);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                producto.setLstCategoriasProducto(categoriasProducto);
                                            }
                                        }
                                    }, Functions.FalloInternet(PrincipalAdministrador.this,progressDialog,"No pudo Cargar"));
                                    ProductoActivity.lstProductos.add(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        BddCategoria.getCategoria(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (!response.toString().equals("[]")) {
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            Categoria categoria = new Categoria(response.getJSONObject(i).getInt("categoria_id"), response.getJSONObject(i).getInt("categoria_estado"), response.getJSONObject(i).getString("categoria_nombre"));
                                            ProductoActivity.lstCategoria.add(categoria);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                Intent intent = new Intent(PrincipalAdministrador.this, ProductoActivity.class);
                                startActivity(intent);
                            }
                        }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No Pudo Cargar"));
                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No pudo Cargar "));
            }
        });

        //endregion




//region vendedores

        btnLstVendedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progressDialog = Functions.CargarDatos(getString(R.string.alert_carga), PrincipalAdministrador.this);
                BddPersonas.getVendedores(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    int codigo = response.getJSONObject(x).getInt("persona_id");
                                    String nombre = response.getJSONObject(x).getString("persona_nombre");
                                    String apellido = response.getJSONObject(x).getString("persona_apellido");
                                    String email = response.getJSONObject(x).getString("persona_email");
                                    int estado = response.getJSONObject(x).getInt("persona_estado");
                                    String foto = response.getJSONObject(x).getString("persona_foto");
                                    int rol = response.getJSONObject(x).getInt("id_rol");
                                    int sede = response.getJSONObject(x).getInt("id_sede");
                                    Persona vendedor = new Persona();
                                    vendedor.setCodigo(codigo);
                                    vendedor.setNombre(nombre);
                                    vendedor.setFoto(foto);
                                    vendedor.setApellido(apellido);
                                    vendedor.setCorreo(email);
                                    vendedor.setEstado(estado);
                                    vendedor.setRol(rol);
                                    vendedor.setSede(sede);
                                    lstPersonas.add(vendedor);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        BddPersonas.getPersonaEmail(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> lstEmail = new ArrayList<>();
                                if (!response.toString().equals("[]")) {
                                    for (int x = 0; x < response.length(); ++x) {
                                        try {
                                            lstEmail.add(response.getJSONObject(x).getString("persona_email"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                                Collections.reverse(lstPersonas);
                                Collections.reverse(lstEmail);
                                AlertDialogVendedores vendedores = new AlertDialogVendedores(PrincipalAdministrador.this, lstPersonas, lstEmail);
                                vendedores.show();
                            }
                        }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No se pudo Cargar"));


                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No pudo Cargar"));
            }
        });
        //endregion   falta arreglar
//region SEDE CRUD
        btnSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Functions.CargarDatos("Cargando...", PrincipalAdministrador.this);

                BddSede.getSede(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    lstSedes.add(new Sede(response.getJSONObject(x).getInt("sede_id"),
                                            response.getJSONObject(x).getInt("sede_estado"),
                                            response.getJSONObject(x).getString("sede_direccion")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressDialog.hide();
                        AlertDialogSede alertDialogSede = new AlertDialogSede(PrincipalAdministrador.this, lstSedes);
                        alertDialogSede.show();
                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, ""));
            }
        });
        //endregion


        //se agrega mas
        //region fabricante
        btnFabricante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Functions.CargarDatos("Cargando Fabricantes", PrincipalAdministrador.this);
                BddFabricante.getFabricantes(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    Fabricante fabricante = new Fabricante();
                                    fabricante.setCodigo(response.getJSONObject(x).getInt("fabricante_id"));
                                    fabricante.setNombre(response.getJSONObject(x).getString("fabricante_nombre"));
                                    fabricante.setEstado(response.getJSONObject(x).getInt("fabricante_estado"));
                                    lstFabricantes.add(fabricante);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressDialog.hide();
                        Collections.reverse(lstFabricantes);
                        fabricantes = new AlertDialogFabricantes(PrincipalAdministrador.this, lstFabricantes);

                        fabricantes.show();

                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No Pudo Cargar"));
            }
        });
        //endregion


        //AQUI HAY TRABAJO
        //region tipo
        btnTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Functions.CargarDatos(getString(R.string.alert_carga), PrincipalAdministrador.this);
                BddTipo.getTipo(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    Tipo tipo = new Tipo();
                                    tipo.setId(response.getJSONObject(x).getInt("tipo_id"));
                                    tipo.setNombre(response.getJSONObject(x).getString("tipo"));
                                    tipo.setEstado(response.getJSONObject(x).getInt("tipo_estado"));
                                    lstTipo.add(tipo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressDialog.dismiss();
                        AlertDialogTipo alertTipo = new AlertDialogTipo(PrincipalAdministrador.this, lstTipo);
                        alertTipo.show();

                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No pudo Cargar"));
            }
        });
        //endregion
    }
}
