package com.example.djgra.inacapdeli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogCategoria;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogFabricantes;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogSede;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogTipo;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PrincipalAdministrador extends AppCompatActivity {
    ImageButton btnCategoria, btnLstVendedores, btnSede, btnFabricante, btnProductos, btnTipo;
    private int btn = 0, posicionUdpdateDelete = 0;
    private static ArrayList<Categoria> lstCategorias = new ArrayList<>();
    private static ArrayList<Sede> lstSedes = new ArrayList<>();
    private static ArrayList<Tipo> lstTipo  = new ArrayList<>();
    private static ArrayList<Persona> lstPersonas = new ArrayList<>();
    private static ArrayList<Fabricante> lstFabricantes = new ArrayList<>();
    private AlertDialogFabricantes fabricantes;
    private AlertDialogCategoria categorias;
    private  AdapterPesonas adapterPesonas;
    ListView lstvVendedores;

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


// region categorias CRUD
        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Categorias", PrincipalAdministrador.this);
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


        //region productos
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cangando Productos", PrincipalAdministrador.this);
                BddProductos.getProducto(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
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
                                    BddCategoria.getCategoriaByProducto(producto.getCodigo(), PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            if (!response.toString().equals("[]")) {
                                                ArrayList<Categoria> categoriasProducto = new ArrayList<>();
                                                for (int x=0; x < response.length();x++){
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
                                    }, Functions.FalloInternet(PrincipalAdministrador.this,progressDialog,"No pudo Cargar"));
                                    ProductoActivity.lstProductos.add(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // falta traer las categorias asociadas a los productos
                        }
                        //traer categorias
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
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Vendedores", PrincipalAdministrador.this);
                BddPersonas.getVendedores(PrincipalAdministrador.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                Log.d("TAG_", "encotnó vendedor" + x);
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
                                    Log.d("TAG_", "encotnó vendedor" + vendedor.getNombre() + " " + vendedor.getApellido());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        final AlertDialog formLstVendedores = new AlertDialog.Builder(PrincipalAdministrador.this)
                                .setView(R.layout.lstvendedores)
                                .create();
                        formLstVendedores.setCanceledOnTouchOutside(false);
                        formLstVendedores.setCancelable(false);
                        formLstVendedores.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                lstvVendedores = formLstVendedores.findViewById(R.id.lstVendedores);
                                adapterPesonas = new AdapterPesonas(PrincipalAdministrador.this);
                                progressDialog.hide();
                                lstvVendedores.setAdapter(adapterPesonas);
                                final EditText etEmail = formLstVendedores.findViewById(R.id.etLstVendedorEmail);
                                Button btnGuardar = formLstVendedores.findViewById(R.id.btnAgregarListaVendedor);
                                ImageButton btnSalir = formLstVendedores.findViewById(R.id.btnlstVendedorAtras);
                                btnGuardar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        final ProgressDialog progressDialog = Functions.CargarDatos("Buscando Usuario", PrincipalAdministrador.this);
                                        int validarEmail = 0;
                                        for (int x = 0; x < lstPersonas.size(); x++) {
                                            if (etEmail.getText().toString().equals(lstPersonas.get(x).getCorreo())) {
                                                etEmail.setError("El VENDEDOR YA ESTA EN LA LISTA");
                                                validarEmail = 1;
                                                progressDialog.hide();
                                            }
                                        }
                                        if (validarEmail == 0) {
                                            BddPersonas.setVendedor(etEmail.getText().toString(), 2, 1, PrincipalAdministrador.this, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d("TAG_", "el usuario sera vendedor");
                                                    //ACTUALIZAR EL LISTVIEW
                                                    //traer un vendedor
                                                    BddPersonas.getPersona(etEmail.getText().toString(), PrincipalAdministrador.this, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                Log.d("TAG_", "tengo la persona ingresada completa");
                                                                JSONObject jsonRespuesta = new JSONObject(response);
                                                                Persona persona = new Persona();
                                                                persona.setCodigo(jsonRespuesta.getInt("persona_id"));
                                                                persona.setNombre(jsonRespuesta.getString("persona_nombre"));
                                                                persona.setApellido(jsonRespuesta.getString("persona_apellido"));
                                                                persona.setFoto(jsonRespuesta.getString("persona_foto"));
                                                                persona.setCorreo(jsonRespuesta.getString("persona_email"));
                                                                persona.setEstado(jsonRespuesta.getInt("persona_estado"));
                                                                persona.setSede(jsonRespuesta.getInt("id_sede"));
                                                                persona.setRol(jsonRespuesta.getInt("id_rol"));
                                                                lstPersonas.add(persona);
                                                                lstvVendedores.setAdapter(adapterPesonas);
                                                                lstvVendedores.deferNotifyDataSetChanged();
                                                                etEmail.setText("");
                                                                Toast.makeText(PrincipalAdministrador.this, "AGREGADO", Toast.LENGTH_SHORT).show();
                                                                progressDialog.hide();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                                btnSalir.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        lstPersonas.removeAll(lstPersonas);
                                        formLstVendedores.dismiss();

                                    }
                                });
                            }
                        });
                        formLstVendedores.show();
                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this, progressDialog, "No pudo Cargar"));
            }
        });
        //endregion   falta arreglar
//region SEDE CRUD
        btnSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando", PrincipalAdministrador.this);
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
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Fabricantes", PrincipalAdministrador.this);
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
                final ProgressDialog progressDialogF = Functions.CargarDatos(getString(R.string.alert_carga), PrincipalAdministrador.this);
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
                        progressDialogF.dismiss();
                        AlertDialogTipo alertTipo = new AlertDialogTipo(PrincipalAdministrador.this, lstTipo);
                        alertTipo.show();

                    }
                }, Functions.FalloInternet(PrincipalAdministrador.this,progressDialogF,"No pudo Cargar"));
            }
        });
        //endregion
    }


    public static void CargarListViewCategoria(final ListView lstView, final Context context, ArrayList<Categoria> list) {
        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstView.setItemsCanFocus(true);
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getEstado() == 1) {
                lstView.setItemChecked(x, true);
                lstView.deferNotifyDataSetChanged();
            }
        }
    }

    public static void CargarListViewSede(final ListView lstView, final Context context, ArrayList<Sede> list) {
        ArrayAdapter<Sede> adapt = new ArrayAdapter<Sede>(context, android.R.layout.simple_list_item_multiple_choice, list);
        lstView.setAdapter(adapt);
        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstView.setItemsCanFocus(true);
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getEstado() == 1) {
                lstView.setItemChecked(x, true);
                lstView.deferNotifyDataSetChanged();
            }
        }
    }

    class AdapterPesonas extends ArrayAdapter<Persona> {
        Activity context;

        public AdapterPesonas(Activity context) {
            super(context, R.layout.listviewvendedores, lstPersonas);
            this.context = context;
        }

        public View getView(final int posicion, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.listviewvendedores, null);
            TextView titulo = item.findViewById(R.id.tvNombreLstProducto);
            titulo.setText(lstPersonas.get(posicion).getNombre().toUpperCase() + " " + lstPersonas.get(posicion).getApellido().toUpperCase());
            TextView email = item.findViewById(R.id.tvPrecioLstProducto);
            email.setText(lstPersonas.get(posicion).getCorreo().toUpperCase());
            TextView sede = item.findViewById(R.id.tvSedeLstVendedor);
            sede.setText("SANTIAGO CENTRO");
            ImageView imgFoto = item.findViewById(R.id.imgLstVendedor);
            imgFoto.setId(100 + posicion);
            ImageButton btnQuitarVendedor = item.findViewById(R.id.btnQuitarVendedor);
            Switch swEstadoVendedor = item.findViewById(R.id.swEstadoVendedor);
            swEstadoVendedor.setId(300+posicion);
            if(lstPersonas.get(posicion).getEstado() == 1){
                swEstadoVendedor.setChecked(true);
            }else{
                swEstadoVendedor.setChecked(false);
            }
            swEstadoVendedor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //solo actualizo el estado del vendedor a la bd falta preguntar el estado actual para cambiar
                    Persona per = lstPersonas.get(posicion);//*#*#3646633#
                    if(per.getEstado() == 0){
                        lstPersonas.get(posicion).setEstado(1);
                        per.setEstado(1);
                    }else{
                        lstPersonas.get(posicion).setEstado(0);
                        per.setEstado(0);
                    }
                    BddPersonas.setVendedor(per.getCorreo(), 2, per.getEstado(), PrincipalAdministrador.this, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG_", "Cambio el estado del vendedor a 0 ");
                        }
                    });
                }
            });
            imgFoto.setImageBitmap(Functions.StringToBitMap(lstPersonas.get(posicion).getFoto()));
            btnQuitarVendedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //aqui digo que vendedor voy a quitar de vendedor
                    Persona per = lstPersonas.get(posicion);
                    final ProgressDialog progressDialog = Functions.CargarDatos("Quitando Vendedor", PrincipalAdministrador.this);
                    BddPersonas.setVendedor(per.getCorreo(), 1, 1, PrincipalAdministrador.this, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG_", "ya no es vendedor ");
                            lstPersonas.remove(posicion);
                            lstvVendedores.setAdapter(adapterPesonas);
                            lstvVendedores.deferNotifyDataSetChanged();
                            progressDialog.dismiss();
                            //enviar mensaje de confirmacion
                        }
                    });
                }
            });
            return item;
        }

    }

}
