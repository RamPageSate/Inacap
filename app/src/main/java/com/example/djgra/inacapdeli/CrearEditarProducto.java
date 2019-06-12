package com.example.djgra.inacapdeli;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.djgra.inacapdeli.ProductoActivity.lstCategoria;

public class CrearEditarProducto extends AppCompatActivity {
    ImageView imgAgregarFoto;
    Bitmap bitmap;
    boolean[] marcados;
    TextView tvTitulo;
    public static ArrayList<Tipo> listaTipo = new ArrayList<>();
    public static ArrayList<Fabricante> listaFabricante = new ArrayList<>();
    public static ArrayList<Categoria> listaCategoria = new ArrayList<>();
    public static ArrayList<Categoria> categoriasSeleccionadas = new ArrayList<>();
    public static ArrayList<Producto> ProductosAgregados = new ArrayList<>();
    private Producto productoSeleccionado = new Producto();
    private String fotoDefault = "";
    private Tipo tipoSeleccionado = new Tipo();
    private Fabricante fabricanteSeleccionado = new Fabricante();
    boolean isActualizar = false;
    EditText etPrecioProducto, etCodigoBarraProducto, etNombreProducto, etDescripcionProducto;
    ImageButton btnAgregarProducto, btnSalir;
    Spinner spFabricante, spTipo;
    Button btnAgregarCategrias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_editar_producto);
        final ProgressDialog progressDialogF = Functions.CargarDatos("Espere..", CrearEditarProducto.this);
        BddFabricante.getFabricantes(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); ++x) {
                        try {
                            Fabricante fabricante = new Fabricante();
                            fabricante.setCodigo(response.getJSONObject(x).getInt("fabricante_id"));
                            fabricante.setNombre(response.getJSONObject(x).getString("fabricante_nombre"));
                            CrearEditarProducto.listaFabricante.add(fabricante);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spFabricante.setAdapter(new ArrayAdapter<Fabricante>(CrearEditarProducto.this, android.R.layout.simple_list_item_1, listaFabricante));
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
                                    CrearEditarProducto.listaTipo.add(tipo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            spTipo.setAdapter(new ArrayAdapter<Tipo>(CrearEditarProducto.this, android.R.layout.simple_list_item_1, listaTipo));
                        }
                        BddCategoria.getCategoria(CrearEditarProducto.this, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        CrearEditarProducto.listaCategoria.add(new Categoria(response.getJSONObject(i).getInt("categoria_id"),
                                                response.getJSONObject(i).getInt("categoria_estado"),
                                                response.getJSONObject(i).getString("categoria_nombre")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                progressDialogF.hide();
                            }
                        }, Functions.FalloInternet(CrearEditarProducto.this, progressDialogF, "No pudo Cargar"));
                    }
                }, Functions.FalloInternet(CrearEditarProducto.this, progressDialogF, "No pudo Cargar"));
            }
        }, Functions.FalloInternet(CrearEditarProducto.this, progressDialogF, "No pudo Cargar"));
        etNombreProducto = findViewById(R.id.etNombreProducto);
        etDescripcionProducto = findViewById(R.id.etDescripcionProducto);
        etCodigoBarraProducto = findViewById(R.id.etSkuProducto);
        etPrecioProducto = findViewById(R.id.etPrecioProductos);
        spFabricante = findViewById(R.id.spFabricantesProductos);
        spTipo = findViewById(R.id.spTipoProducto);
        imgAgregarFoto = findViewById(R.id.imgAgregarFotoProducto);
        btnAgregarCategrias = findViewById(R.id.btnCategoriasAgregarProducto);
        btnAgregarProducto = findViewById(R.id.imgAgregarProducto);
        btnSalir = findViewById(R.id.btnSalirProducto);
        tvTitulo = findViewById(R.id.tvTituloAgregarProducto);
        Bitmap bit = ((BitmapDrawable) imgAgregarFoto.getDrawable()).getBitmap();
        fotoDefault = Functions.getStringImage(bit);
        tvTitulo.setText("Registro Producto");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productoSeleccionado = (Producto) bundle.getSerializable("producto");
            etCodigoBarraProducto.setText(productoSeleccionado.getSku());
            etDescripcionProducto.setText(productoSeleccionado.getDescripcion());
            etNombreProducto.setText(productoSeleccionado.getNombre());
            etPrecioProducto.setText(String.valueOf(productoSeleccionado.getPrecio()));
            categoriasSeleccionadas.addAll(productoSeleccionado.getLstCategoriasProducto());
            tvTitulo.setText("Actualizar Producto");
            isActualizar = true;
            for (int x = 0; x < listaFabricante.size(); x++) {
                if (listaFabricante.get(x).getCodigo() == productoSeleccionado.getId_fabricante()) {
                    spFabricante.setSelection(x);
                }
            }
            for (int x = 0; x < listaTipo.size(); x++) {
                if (listaTipo.get(x).getId() == productoSeleccionado.getId_tipo()) {
                    spTipo.setSelection(x);
                }
            }
        }
        spFabricante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fabricanteSeleccionado = listaFabricante.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoSeleccionado = listaTipo.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
// esta devuelve categoriasSeleccionadas
//region btnAgregarCategorias

        btnAgregarCategrias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Categoria> cargarOpciones = listaCategoria;
                for (int x = 0; x < listaCategoria.size(); x++) {
                    Log.d("TAG_", "" + listaCategoria.get(x).getCodigo());
                }
                for (int x = 0; x < categoriasSeleccionadas.size(); x++) {
                    Log.d("TAG_", "cSele " + categoriasSeleccionadas.get(x).getCodigo());
                }
                final int cantidadCategorias = listaCategoria.size();
                marcados = new boolean[cantidadCategorias];
                AlertDialog.Builder builder = new AlertDialog.Builder(CrearEditarProducto.this);
                builder.setTitle("CATEGORIAS DEL PRODUCTO");
                String[] nombreCategoria = new String[cantidadCategorias];
                for (int x = 0; x < cantidadCategorias; x++) {
                    String nombre = cargarOpciones.get(x).getNombre();
                    nombreCategoria[x] = (nombre);
                }
                if (!categoriasSeleccionadas.isEmpty()) {
                    for (int x = 0; x < cargarOpciones.size(); x++) {
                        for (int c = 0; c < categoriasSeleccionadas.size(); c++) {
                            if (cargarOpciones.get(x).getCodigo() == categoriasSeleccionadas.get(c).getCodigo()) {//SECAE ya que no tienen la misma cantidad
                                marcados[x] = true;
                            }
                        }
                    }

                    //no marca las categorias de los productos a actualizaar y tampoco guarda el dato despues de seleccionar
                    builder.setMultiChoiceItems(nombreCategoria, marcados, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            Log.d("TAG_", "Marco con la lista seleccionada  " + which);
                            marcados[which] = isChecked;
                        }
                    });
                } else {
                    builder.setMultiChoiceItems(nombreCategoria, marcados, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            marcados[which] = isChecked;
                            Log.d("TAG_", "agregando porque esta vacia " + which);
                        }
                    });
                }
                builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categoriasSeleccionadas.removeAll(categoriasSeleccionadas);
                        for (int x = 0; x < cantidadCategorias; x++) {
                            if (marcados[x] == true) {
                                Categoria cate = new Categoria(lstCategoria.get(x).getCodigo(), lstCategoria.get(x).getEstado(), "" + lstCategoria.get(x).getNombre());
                                categoriasSeleccionadas.add(cate);
                                Log.d("TAG_", "agrego -> " + x);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Salir", null);
                builder.create().show();
            }
        });
//endregion     devu

//region btnAgregarProducto
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifico los campos
                Producto producto = new Producto();
                String nombre = "", descripcion = "", sku = "", foto = "";
                int precio = 0;
                int validador = 0;
                if (!etNombreProducto.getText().toString().isEmpty()) {
                    nombre = etNombreProducto.getText().toString().toUpperCase();
                } else {
                    validador++;
                    etNombreProducto.setError("");
                }
                if (!etDescripcionProducto.getText().toString().isEmpty()) {
                    descripcion = etDescripcionProducto.getText().toString().toUpperCase();
                } else {
                    validador++;
                    etDescripcionProducto.setError("");
                }
                if (!etCodigoBarraProducto.getText().toString().isEmpty()) {
                    sku = etCodigoBarraProducto.getText().toString().toUpperCase();
                } else {
                    validador++;
                    etCodigoBarraProducto.setError("");
                }
                if (!etPrecioProducto.getText().toString().isEmpty()) {
                    precio = Integer.parseInt(etPrecioProducto.getText().toString());
                } else {
                    validador++;
                    etPrecioProducto.setError("");
                }
                if(categoriasSeleccionadas.isEmpty()){
                    validador++;
                    Toast.makeText(CrearEditarProducto.this, "Seleccione Categoria", Toast.LENGTH_SHORT).show();
                }
                Bitmap bit = ((BitmapDrawable) imgAgregarFoto.getDrawable()).getBitmap();
                producto.setFoto(Functions.getStringImage(bit));
                if (producto.getFoto().equals(fotoDefault)) {
                    validador++;
                    Toast.makeText(CrearEditarProducto.this, "Ingrese Foto", Toast.LENGTH_SHORT).show();
                }
                if (validador == 0) {
                    producto.setNombre(nombre);
                    producto.setId_tipo(tipoSeleccionado.getId());
                    producto.setId_fabricante(fabricanteSeleccionado.getCodigo());
                    producto.setDescripcion(descripcion);
                    producto.setSku(sku);
                    producto.setPrecio(precio);
                    producto.setFoto(foto);
                    producto.setLstCategoriasProducto(categoriasSeleccionadas);
                    if (!isActualizar) {
                        final ProgressDialog progressDialog = Functions.CargarDatos("AGREGANDO....", CrearEditarProducto.this);
                        BddProductos.setProducto(producto, CrearEditarProducto.this, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                BddProductos.getProducto(CrearEditarProducto.this, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        for (int x = 0; x < response.length(); x++) {
                                            try {
                                                Producto producto = new Producto();
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
                                                //FALTA ENVIAR LAS CATEGORIAS SELECCIONADAS
                                                ProductosAgregados.add(producto);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Toast.makeText(CrearEditarProducto.this, "Agregado", Toast.LENGTH_SHORT).show();
                                        etCodigoBarraProducto.setText("");
                                        etDescripcionProducto.setText("");
                                        etNombreProducto.setText("");
                                        etPrecioProducto.setText("");
                                        //restablecer la imagen
                                        progressDialog.dismiss();
                                    }
                                }, Functions.FalloInternet(CrearEditarProducto.this, progressDialog, "No pudo cargar"));
                            }
                        }, Functions.FalloInternet(CrearEditarProducto.this, progressDialog, "Vuelve a Intentar"));
                    } else {
                        //aqui va actualizar

                    }
                }
            }
        });
        //endregion

    }


    private void cargarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la Aplicacion"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                //Configuración del mapa de bits en ImageView
                imgAgregarFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
