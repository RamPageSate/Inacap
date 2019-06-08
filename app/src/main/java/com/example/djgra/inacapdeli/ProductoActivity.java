package com.example.djgra.inacapdeli;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Tipo;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.BddTipo;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.djgra.inacapdeli.R.layout.listviewproductos;

public class ProductoActivity extends AppCompatActivity {
    ImageButton btnSalir, btnAgregar;
    Spinner spCategorias;
    ListView lstvProductos;
    Bitmap bitmap;
    ImageView imgAgregarFoto;
    ArrayList<Fabricante> lstFabricantes  = new ArrayList<>();
    Producto productoSeleccionado ;
   // AdapterProductos adptProducto = new AdapterProductos(this);
    //
   boolean [] marcados;
    Boolean isActualizar = false;
    ArrayList<Tipo> lstTipo  = new ArrayList<>();
    public static ArrayList<Producto> lstProductos = new ArrayList<>();
    public static ArrayList<Categoria> lstCategoria = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        Producto producto = new Producto();
        producto.setNombre("anita");
        producto.setPrecio(50000);
        producto.setEstado(1);
        lstProductos.add(producto);
        btnAgregar = (ImageButton) findViewById(R.id.btnAddLstProductos);
        btnSalir = (ImageButton) findViewById(R.id.btnSalirLstProducto);
        spCategorias = (Spinner) findViewById(R.id.spCategoriaLstProducto);
        lstvProductos = (ListView) findViewById(R.id.lstvProductos);
        final AdapterProductos adapterProductos = new AdapterProductos(this);
        ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<Categoria>(this,android.R.layout.simple_list_item_1,lstCategoria);
        lstvProductos.setAdapter(adapterProductos);
        spCategorias.setAdapter(adapterCategoria);
        //if(!lstProductos.isEmpty()){
          //  lstvProductos.setAdapter(adptProducto);
        //}


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActualizar=false;
                CreateUpdateProducto();
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductoActivity.this, PrincipalAdministrador.class);
                startActivity(i);
            }
        });
    }

    class AdapterProductos extends ArrayAdapter<Producto> {
        Activity context;

       public AdapterProductos(Activity context){
         super(context,R.layout.listviewproductos,lstProductos);
            this.context = context;
        }

        public View getView(final int posicion, View view, ViewGroup parent){

            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(listviewproductos,null);
            if(!lstProductos.isEmpty()){
                TextView nombre = (TextView)item.findViewById(R.id.tvNombreLstProducto);
                nombre.setText(lstProductos.get(posicion).getNombre());
                TextView precio = (TextView)item.findViewById(R.id.tvPrecioLstProducto);
                precio.setText(""+ lstProductos.get(posicion).getPrecio());
                ImageButton btnEdit = (ImageButton) item.findViewById(R.id.btnEditProducto);
                TextView descripcion = (TextView) item.findViewById(R.id.tvDescripcionListViewProducto);
                Switch swEstado = (Switch) item.findViewById(R.id.swEstadoListViewProducto);
                for(int x = 0 ; x < lstProductos.size(); x++){
                    if(lstProductos.get(x).getEstado() == 1){
                        swEstado.setChecked(true);
                    }else{
                        swEstado.setChecked(false);
                    }
                }
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productoSeleccionado = (Producto) lstProductos.get(posicion);
                        isActualizar = true;
                        CreateUpdateProducto();
                    }
                });
            }


            return item;
        }
    }
    private void cargarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicacion"), 10);
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






    public void CreateUpdateProducto(){
        //cargar tipo y fabricantes
        final ProgressDialog progressDialog = Functions.CargarDatos("Espere..", ProductoActivity.this);
        BddFabricante.getFabricantes(ProductoActivity.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")){
                    for (int x = 0; x < response.length(); ++x) {
                        try {
                            Fabricante fabricante = new Fabricante();
                            fabricante.setCodigo(response.getJSONObject(x).getInt("fabricante_id"));
                            fabricante.setNombre(response.getJSONObject(x).getString("fabricante_nombre"));
                            lstFabricantes.add(fabricante);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("TAG_","entro a fabricantes");
                }
                //cargare tipo
                BddTipo.getTipo(ProductoActivity.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); ++x) {
                                try {
                                    Tipo tipo = new Tipo();
                                    tipo.setId(response.getJSONObject(x).getInt("tipo_id"));
                                    tipo.setNombre(response.getJSONObject(x).getString("tipo"));
                                    lstTipo.add(tipo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.d("TAG_","entro a tipos");
                            //AQUI ABRIRE LA VISTA
                            setContentView(R.layout.agregarproducto);
                            progressDialog.dismiss();
                            final Producto producto = new Producto();
                            final EditText etNombreProducto = (EditText) findViewById(R.id.etNombreProducto);
                            final EditText etDescripcionProducto = (EditText) findViewById(R.id.etDescripcionProducto);
                            final EditText etCodigoBarraProducto = (EditText) findViewById(R.id.etSkuProducto);
                            final EditText etPrecioProducto = (EditText) findViewById(R.id.etPrecioProductos);
                            Spinner spFabricante = (Spinner) findViewById(R.id.spFabricantesProductos);
                            Spinner spTipo = (Spinner) findViewById(R.id.spTipoProducto);
                            imgAgregarFoto = (ImageView) findViewById(R.id.imgAgregarFotoProducto);
                            Button btnAgregarCategrias = (Button) findViewById(R.id.btnCategoriasAgregarProducto);
                            ImageButton btnAgregarProducto = (ImageButton) findViewById(R.id.imgAgregarProducto);
                            ImageButton btnSalir = (ImageButton) findViewById(R.id.btnSalirProducto);
                            spFabricante.setAdapter(new ArrayAdapter<Fabricante>(ProductoActivity.this,android.R.layout.simple_list_item_1,lstFabricantes));
                            spTipo.setAdapter(new ArrayAdapter<Tipo>(ProductoActivity.this,android.R.layout.simple_list_item_1,lstTipo));
                            if(isActualizar){
                                etCodigoBarraProducto.setText(productoSeleccionado.getSku());
                                etDescripcionProducto.setText(productoSeleccionado.getDescripcion());
                                etNombreProducto.setText(productoSeleccionado.getNombre());
                                etPrecioProducto.setText(productoSeleccionado.getPrecio());
                                for(int x=0; x < lstFabricantes.size(); x++ ){
                                    if(lstFabricantes.get(x).getCodigo() == productoSeleccionado.getId_fabricante()){
                                        spFabricante.setSelection(x);
                                        producto.setId_fabricante(lstFabricantes.get(x).getCodigo());
                                    }
                                }
                                for(int x=0; x < lstTipo.size(); x++ ){
                                    if(lstTipo.get(x).getId() == productoSeleccionado.getId_tipo()){
                                        spTipo.setSelection(x);
                                        producto.setId_tipo(lstTipo.get(x).getId());
                                    }
                                }

                            }
                            btnAgregarCategrias.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // aqui tendre las categoriass
                                    marcados = categoriasSeleccionadas();

                                }
                            });
                            spFabricante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    producto.setId_fabricante(lstFabricantes.get(position).getCodigo());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    producto.setId_tipo(lstTipo.get(position).getId());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            //permitir subir una foto
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
                                    isActualizar=false;
                                }
                            });

                            btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //verifico los campos
                                    String nombre = "", descripcion="", sku="", foto = "";
                                    int precio = 0;
                                    int validador  =0 ;
                                    if(!etNombreProducto.getText().toString().isEmpty()){
                                        nombre = etNombreProducto.getText().toString().toUpperCase();
                                    }else{
                                        validador++;
                                        etNombreProducto.setError("");
                                    }
                                    if(!etDescripcionProducto.getText().toString().isEmpty()){
                                        descripcion = etDescripcionProducto.getText().toString().toUpperCase();
                                    }else{
                                        validador++;
                                        etDescripcionProducto.setError("");
                                    }
                                    if(!etCodigoBarraProducto.getText().toString().isEmpty()){
                                        sku = etCodigoBarraProducto.getText().toString().toUpperCase();
                                    }else{
                                        validador++;
                                        etCodigoBarraProducto.setError("");
                                    }
                                    if(!etPrecioProducto.getText().toString().isEmpty()){
                                        precio = Integer.parseInt(etPrecioProducto.getText().toString());
                                    }else{
                                        validador++;
                                        etPrecioProducto.setError("");
                                    }
                                    //recuperar las categorias marcadas

                                    producto.setId_categoria(48);

                                    //tendriamos que rescatar la foto
                                    //if(foto.isEmpty()){
                                      //  Toast.makeText(ProductoActivity.this, "Debe Agregar Foto", Toast.LENGTH_SHORT).show();
                                       // validador++;
                                    //}
                                    producto.setFoto("1");

                                    if(validador == 0){
                                        producto.setNombre(nombre);
                                        producto.setDescripcion(descripcion);
                                        producto.setSku(sku);
                                        producto.setPrecio(precio);
                                        producto.setFoto(foto);
                                        if(!isActualizar){
                                            final ProgressDialog progressDialog = Functions.CargarDatos("AGREGANDO....", ProductoActivity.this);
                                            BddProductos.setProducto(producto, ProductoActivity.this, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    //borrare la lista para cargar todos los productos
                                                    BddProductos.getProducto(ProductoActivity.this, new Response.Listener<JSONArray>() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            lstProductos.removeAll(lstProductos);
                                                            for (int x= 0; x < response.length(); x++){
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
                                                                    producto.setId_categoria(response.getJSONObject(x).getInt("categoria_id"));
                                                                    lstProductos.add(producto);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            Toast.makeText(ProductoActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                                                            //lstvProductos.setAdapter(adptProducto);
                                                            //lstvProductos.deferNotifyDataSetChanged();
                                                            etCodigoBarraProducto.setText("");
                                                            etDescripcionProducto.setText("");
                                                            etNombreProducto.setText("");
                                                            etPrecioProducto.setText("");
                                                            //restablecer la imagen
                                                            progressDialog.dismiss();
                                                        }
                                                    }, Functions.FalloInternet(ProductoActivity.this,progressDialog,"No pudo cargar"));
                                                }
                                            }, Functions.FalloInternet(ProductoActivity.this, progressDialog,"Vuelve a Intentar"));
                                        }else{
                                            //aqui va actualizar

                                        }
                                    }
                                }
                            });



                        }
                    }
                }, Functions.FalloInternet(ProductoActivity.this,progressDialog,"No pudo Conectarse"));
            }
        }, Functions.FalloInternet(ProductoActivity.this,progressDialog,"No pudo Conectarse"));
        if(!isActualizar){

        }
    }

    public boolean[] categoriasSeleccionadas(){
        int cantidadCategorias = lstCategoria.size();
        final boolean [] marcados = new boolean[cantidadCategorias];
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductoActivity.this);
        builder.setTitle("CATEGORIAS DEL PRODUCTO");
        String [] nombreCategoria = new String[cantidadCategorias];
        for (int x = 0; x < lstCategoria.size();x ++){
            String nombre = lstCategoria.get(x).getNombre();
            nombreCategoria[x] = (nombre);
        }
        builder.setMultiChoiceItems(nombreCategoria, marcados,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                marcados[which] = isChecked;
            }
        });
        builder.setPositiveButton("Listo",null);
        builder.setNegativeButton("Salir",null);
        builder.create().show();
        return  marcados;
    }
}