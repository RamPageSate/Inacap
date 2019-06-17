package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Pedido;
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
    RecyclerView rcProductosValorados, rcCategorias, rcFavoritas;
    private Producto producto;
    private static int pagarActual = 0, prueba = 0;
    private static LinearLayout linearPagar;
    private static TextView tvMontoPagar;
    private static ArrayList<Producto> lstProducto = new ArrayList<>();
    private static ArrayList<Categoria> lstCategorias = new ArrayList<>();
    private static ArrayList<Producto> lstProductoFiltrados = new ArrayList<>();
    private static TextView tvCantidadArticulosCliente;
    private static Persona cliente = new Persona();
    private static Pedido pedidoCliente = new Pedido();
    private static  AdaptadorCategoriasCliente adaptadorCategorias;
    LinearLayout linearCategorias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cliente);
        linearCategorias = findViewById(R.id.linearCategoriaPrincipalCliente);
        tvMontoPagar = findViewById(R.id.tvPagarTotalClienteVerde);
        rcProductosValorados = (RecyclerView) findViewById(R.id.rcViewProducto1Cliente);
        rcCategorias = (RecyclerView) findViewById(R.id.rcCategoriassProductoCliente);
        rcFavoritas = (RecyclerView) findViewById(R.id.rcFavoritasCliente);
        rcFavoritas.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rcProductosValorados.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false ));
        rcCategorias.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rcProductosValorados.setHasFixedSize(true);
        rcCategorias.setHasFixedSize(true);
        rcProductosValorados.setItemViewCacheSize(lstProducto.size());
        linearPagar = findViewById(R.id.linearPDCTotal);
        fotoUsr = (ImageView) findViewById(R.id.imgFotoCliente);
        tvSedeActual = (TextView) findViewById(R.id.tvSedeActualCliente);
        tvSaldoActual = (TextView) findViewById(R.id.tvSaldoCliente);
        tvCantidadArticulosCliente = (TextView) findViewById(R.id.tvCantidadPagarDetalleCliente);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (Persona) bundle.getSerializable("usr");
            tvSaldoActual.setText(tvSaldoActual.getText() + "" + cliente.getSaldo());
            fotoUsr.setImageBitmap(Functions.StringToBitMap(cliente.getFoto()));
        }
        final ProgressDialog progressDialog = Functions.CargarDatos("Cangando Productos", PrincipalCliente.this);
        BddProductos.getProducto(PrincipalCliente.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            final Producto producto = new Producto();
                            producto.setCodigo(response.getJSONObject(x).getInt("producto_id"));
                            final ArrayList<Categoria> categoriasProducto = new ArrayList<>();
                            BddCategoria.getCategoriaByProducto(response.getJSONObject(x).getInt("producto_id"), PrincipalCliente.this, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (!response.toString().equals("[]")) {
                                        for (int x = 0; x < response.length(); ++x) {
                                            try {//falta agrgar las categorias asignadas al producto
                                                Categoria categoria = new Categoria(response.getJSONObject(x).getInt("categoria_id"), response.getJSONObject(x).getInt("categoria_estado"), response.getJSONObject(x).getString("categoria_nombre"));
                                                categoriasProducto.add(categoria);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        producto.setLstCategoriasProducto(categoriasProducto);
                                        lstProducto.add(producto);
                                        AdaptadorRecyclerViewProductoCliente adaptadorValoradas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(56,1,"")));
                                        rcProductosValorados.setAdapter(adaptadorValoradas);
                                        //hacer Condicion de que si esta vacia cambiar el nombre de Favoritas ademas rellenar la vista con otra categoria
                                        AdaptadorRecyclerViewProductoCliente adaptadorFavoritas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(54,1,"Favoritas")));
                                        rcFavoritas.setAdapter(adaptadorFavoritas);
                                    } // ya cargo las categorias del producto
                                }
                            },null);
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
                        Log.d("TAG_", "cargovista");
                        progressDialog.dismiss();
                    }
                }//terminare el proceso de cargar todos los productos
            }
        },Functions.FalloInternet(PrincipalCliente.this,progressDialog,"No Pudo Cargar"));
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
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, Functions.FalloInternet(PrincipalCliente.this, null, "No Pudo Cargar"));
        BddCategoria.getCategoria(PrincipalCliente.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            lstCategorias.add(new Categoria(response.getJSONObject(i).getInt("categoria_id"),
                                    response.getJSONObject(i).getInt("categoria_estado"),
                                    response.getJSONObject(i).getString("categoria_nombre")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adaptadorCategorias = new AdaptadorCategoriasCliente(lstCategorias);
                    rcCategorias.setAdapter(adaptadorCategorias);
                }
            }
        }, Functions.FalloInternet(PrincipalCliente.this,null,""));
        linearPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me tengo que enviar los productos pedidos el cliente
                Intent intent = new Intent(PrincipalCliente.this, DetallePagarCliente.class);
                intent.putExtra("pedido", pedidoCliente);
                startActivity(intent);
            }
        });

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
                    Log.d("TAG_", "PC ->"+lstProducto.get(x).getNombre()+ " " + lstProducto.get(x).getLstCategoriasProducto().get(c).getNombre());
                    lista.add(lstProducto.get(x));
                }
            }
        }
        return lista;
    }
    public static void agregarProductoPepido(Producto producto){
        pedidoCliente.agregarProductoListaPedido(producto);
    }
    public static void quitarProductoPedido(Producto producto){
        pedidoCliente.quitarProductoListaPedido(producto);
    }

    //region AdaptadorCategorias
    public class AdaptadorCategoriasCliente extends RecyclerView.Adapter<AdaptadorCategoriasCliente.ViewHolderCategoriasCliente> {
        ArrayList<Categoria> lstCategorias = new ArrayList<>();
        public AdaptadorCategoriasCliente(ArrayList<Categoria> lstCategorias) {
            this.lstCategorias = lstCategorias;
        }
        //muestro la vista
        @NonNull
        @Override
        public ViewHolderCategoriasCliente onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcategoriacliente, null, false);
            return new ViewHolderCategoriasCliente(view);
        }


        //leno los datos
        @Override
        public void onBindViewHolder(@NonNull ViewHolderCategoriasCliente holder, final int position) {
            holder.tvnombreCategoria.setText(lstCategorias.get(position).getNombre());
            holder.tvnombreCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG_", "escijo acete-> ");
                    Intent intent = new Intent(PrincipalCliente.this,ClienteProductosPorCategoria.class);
                    intent.putExtra("listaProducto",FiltrarListaPorCategoria(lstCategorias.get(position)));
                    intent.putExtra("categoria", lstCategorias.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return lstCategorias.size();
        }
        //creo los view
        public class ViewHolderCategoriasCliente extends RecyclerView.ViewHolder {
            TextView tvnombreCategoria;

            //referencio los view
            public ViewHolderCategoriasCliente(@NonNull View itemView) {
                super(itemView);
                tvnombreCategoria = itemView.findViewById(R.id.tvCategoriasProductosCliente);
            }
        }

    }
    //endregion
}
