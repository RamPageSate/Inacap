package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddSede;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PrincipalCliente extends AppCompatActivity {
    ImageView fotoUsr;
    TextView tvSedeActual, tvSaldoActual;
    RecyclerView rc;
    private Producto producto;
    public static int pagarActual = 0, prueba = 0;
    public static LinearLayout linearPagar;
    public static TextView tvMontoPagar;
    public static ArrayList<Producto> lstProducto = new ArrayList<>();
    private static TextView tvCantidadArticulosCliente;
    public static Persona cliente = new Persona();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cliente);
        tvMontoPagar = findViewById(R.id.tvTotalPagarCliente);
        rc = (RecyclerView) findViewById(R.id.rcViewProducto1Cliente);
        rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        linearPagar = findViewById(R.id.linearPagar);
        fotoUsr = (ImageView) findViewById(R.id.imgFotoCliente);
        tvSedeActual = (TextView) findViewById(R.id.tvSedeActualCliente);
        tvSaldoActual = (TextView) findViewById(R.id.tvSaldoCliente);
        tvCantidadArticulosCliente = (TextView) findViewById(R.id.tvCantidadArticulosCliente);
        final ProgressDialog progressDialog = Functions.CargarDatos("Cargando...", this);
        BddProductos.getProducto(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            Log.d("TAG_", "cargara los productos");
                            producto = new Producto();
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (producto.getEstado() != 0) {
                            lstProducto.add(producto);
                            Log.d("TAG_", "" + producto.getNombre());
                        }
                    }
                    for (int c = 0; c < lstProducto.size(); c++) {
                        BddCategoria.getCategoriaByProducto(lstProducto.get(c).getCodigo(), PrincipalCliente.this, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (!response.toString().equals("[]")) {
                                    final ArrayList<Categoria> categoriasProducto = new ArrayList<>();
                                    for (int x = 0; x < response.length(); x++) {
                                        try {
                                            Categoria categoria = new Categoria(response.getJSONObject(x).getInt("categoria_id"), response.getJSONObject(x).getInt("categoria_estado"), response.getJSONObject(x).getString("categoria_nombre"));
                                            categoriasProducto.add(categoria);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        lstProducto.get(prueba).setLstCategoriasProducto(categoriasProducto);
                                        Log.d("TAG_", "cago catergoria" + lstProducto.get(prueba).getCodigo());
                                    }
                                    prueba++;
                                }

                                if(lstProducto.size() == prueba){
                                    Categoria cate = new Categoria(56, 1, "asd");
                                    AdaptadorRecyclerViewProductoCliente adapter = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(cate));
                                    rc.setAdapter(adapter);
                                    Log.d("TAG_", "Cargp Vista");
                                    progressDialog.dismiss();
                                }
                            }
                        }, Functions.FalloInternet(PrincipalCliente.this, progressDialog, "No pudo Cargar"));
                    }
                }


            }
        }, Functions.FalloInternet(PrincipalCliente.this, progressDialog, "sin Conexion"));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (Persona) bundle.getSerializable("usr");
            tvSaldoActual.setText(tvSaldoActual.getText() + "" + cliente.getSaldo());
            fotoUsr.setImageBitmap(Functions.StringToBitMap(cliente.getFoto()));
        }

        BddSede.getSede(PrincipalCliente.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); ++x) {
                        try {
                            final Sede sede = (new Sede(response.getJSONObject(x).getInt("sede_id"),
                                    response.getJSONObject(x).getInt("sede_estado"),
                                    response.getJSONObject(x).getString("sede_direccion")));
                            if (sede.getCodigo() == cliente.getSede()) {
                                tvSedeActual.setText(sede.getDireccion().toUpperCase());
                                progressDialog.dismiss();
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, Functions.FalloInternet(PrincipalCliente.this, progressDialog, "No Pudo Cargar"));
    }


    public static void pagoTotal(int precio) {
        pagarActual = pagarActual + precio;
        if (pagarActual > 0) {
            linearPagar.setVisibility(View.VISIBLE);
            tvMontoPagar.setText(String.valueOf(pagarActual));
        }
    }

    public static int descontarTotalCompra(int precio) {
        pagarActual = pagarActual - precio;
        tvMontoPagar.setText(String.valueOf(pagarActual));
        if (pagarActual == 0) {
            linearPagar.setVisibility(View.INVISIBLE);
            tvMontoPagar.setText("");
        }
        return pagarActual;
    }

    public static void agregarCantidadArticulos() {
        int cantidadArticulos = 1 + Integer.parseInt(tvCantidadArticulosCliente.getText().toString());
        tvCantidadArticulosCliente.setText("" + cantidadArticulos);
    }

    public static void descontarCantidadArticulos() {
        int cantidadArticulos = Integer.parseInt(tvCantidadArticulosCliente.getText().toString()) - 1;
        tvCantidadArticulosCliente.setText("" + cantidadArticulos);
    }

    public static ArrayList<Producto> FiltrarListaPorCategoria(Categoria categoria) {
        ArrayList<Producto> lista = new ArrayList<>();
        for (int x = 0; x < lstProducto.size(); x++) {
            Log.d("TAG_", "Producto -> "+lstProducto.get(x).getNombre());
            for (int c = 0; c < lstProducto.get(x).getLstCategoriasProducto().size(); c++) {
                if (lstProducto.get(x).getLstCategoriasProducto().get(c).getCodigo() == categoria.getCodigo()) {
                    Log.d("TAG_", ""+lstProducto.get(x).getNombre()+ " " + lstProducto.get(x).getLstCategoriasProducto().get(c).getNombre());
                    lista.add(lstProducto.get(x));
                }
            }
        }
        return lista;
    }
}
