package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorCategoriasCliente;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
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
    private static LinearLayout linearPagar;
    ImageButton btnDatosPersonales, btnHistorial;
    private static ArrayList<Producto> lstProducto = new ArrayList<>();
    public static ArrayList<Categoria> lstCategorias = new ArrayList<>();
    public static TextView tvCantidadArticulosCliente, tvMontoPagar;
    public static Persona clientePrincipal = new Persona();
    public static Pedido pedidoCliente = new Pedido();
    private static AdaptadorCategoriasCliente adaptadorCategorias;
    LinearLayout linearCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        linearCategorias = findViewById(R.id.linearCategoriaPrincipalCliente);
        btnHistorial = findViewById(R.id.btnHistorial);
        tvMontoPagar = findViewById(R.id.tvPagarTotalClienteVerde);
        rcProductosValorados = (RecyclerView) findViewById(R.id.rcViewProducto1Cliente);
        rcCategorias = (RecyclerView) findViewById(R.id.rcCategoriassProductoCliente);
        rcFavoritas = (RecyclerView) findViewById(R.id.rcFavoritasCliente);
        rcFavoritas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcProductosValorados.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        linearPagar = findViewById(R.id.linearPDCTotal);
        fotoUsr = (ImageView) findViewById(R.id.imgFotoCliente);
        tvSedeActual = (TextView) findViewById(R.id.tvSedeActualCliente);
        btnDatosPersonales = findViewById(R.id.btnDatosPersonales);
        tvSaldoActual = (TextView) findViewById(R.id.tvSaldoCliente);
        tvCantidadArticulosCliente = (TextView) findViewById(R.id.tvCantidadPagarDetalleCliente);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clientePrincipal = (Persona) bundle.getSerializable("usr");
            tvSaldoActual.setText(tvSaldoActual.getText() + "" + clientePrincipal.getSaldo());
            fotoUsr.setImageBitmap(Functions.StringToBitMap(clientePrincipal.getFoto()));
            pedidoCliente.setId_cliente(clientePrincipal.getCodigo());

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
                                            try {
                                                Categoria categoria = new Categoria(response.getJSONObject(x).getInt("categoria_id"), response.getJSONObject(x).getInt("categoria_estado"), response.getJSONObject(x).getString("categoria_nombre"));
                                                categoriasProducto.add(categoria);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        producto.setLstCategoriasProducto(categoriasProducto);
                                        lstProducto.add(producto);
                                        AdaptadorRecyclerViewProductoCliente adaptadorValoradas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(51, 1, "")), tvCantidadArticulosCliente, tvMontoPagar, pedidoCliente, PrincipalCliente.this, linearPagar, clientePrincipal,null);
                                        rcProductosValorados.setAdapter(adaptadorValoradas);
                                        //hacer Condicion de que si esta vacia cambiar el nombre de Favoritas ademas rellenar la vista con otra categoria
                                        AdaptadorRecyclerViewProductoCliente adaptadorFavoritas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(74, 1, "Favoritas")), tvCantidadArticulosCliente, tvMontoPagar, pedidoCliente, PrincipalCliente.this, linearPagar, clientePrincipal,null);
                                        rcFavoritas.setAdapter(adaptadorFavoritas);
                                    }
                                }
                            }, null);
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
                        progressDialog.dismiss();
                    }
                }//terminare el proceso de cargar todos los productos
            }
        }, Functions.FalloInternet(PrincipalCliente.this, progressDialog, "No Pudo Cargar"));
        BddSede.getSede(PrincipalCliente.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.toString().equals("[]")) {
                    for (int x = 0; x < response.length(); ++x) {
                        try {
                            final Sede sede = (new Sede(response.getJSONObject(x).getInt("sede_id"),
                                    response.getJSONObject(x).getInt("sede_estado"),
                                    response.getJSONObject(x).getString("sede_direccion")));
                            if (sede.getCodigo() == clientePrincipal.getSede()) {
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
                    adaptadorCategorias = new AdaptadorCategoriasCliente(lstCategorias, PrincipalCliente.this, pedidoCliente);
                    rcCategorias.setAdapter(adaptadorCategorias);
                }
            }
        }, Functions.FalloInternet(PrincipalCliente.this, null, ""));
        linearPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me tengo que enviar los productos pedidos el cliente
                Intent intent = new Intent(PrincipalCliente.this, DetallePagarCliente.class);
                intent.putExtra("pedido", pedidoCliente);
                startActivityForResult(intent, 7);
            }
        });

        btnDatosPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalCliente.this, DatosCliente.class);
                i.putExtra("cliente",clientePrincipal);;
                i.putExtra("code",1);
                startActivityForResult(i,12);
            }
        });

        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cangando Historial", PrincipalCliente.this);
                BddPedido.getPedidoByCliente(clientePrincipal.getCodigo(), PrincipalCliente.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.equals("[]")) {
                            for (int x = 0; x < response.length(); x++) {
                                try {
                                    final Pedido pedido = new Pedido();
                                    pedido.setCodigo(response.getJSONObject(x).getInt("pedido_id"));
                                    BddProductos.getProductoByPedido(pedido.getCodigo(), PrincipalCliente.this, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            if (!response.equals("[]")) {
                                                ArrayList<Producto> lista = new ArrayList<>();
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
                                                        lista.add(producto);
                                                        Log.d("TAG_", "CP-> " + producto.getNombre());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                pedido.setLstProductoPedido(lista);
                                                clientePrincipal.agregarPedido(pedido);
                                            }
                                        }
                                    }, Functions.FalloInternet(PrincipalCliente.this, progressDialog, "No pudo Cargar"));
                                    pedido.setFechaPedido(response.getJSONObject(x).getString("pedido_fecha_hora"));
                                    pedido.setPedido_estado(response.getJSONObject(x).getInt("pedido_estado"));
                                    pedido.setId_cliente(response.getJSONObject(x).getInt("id_cliente"));
                                    pedido.setId_vendedor(response.getJSONObject(x).getInt("id_vendedor"));
                                    pedido.setId_condicion_pedido(response.getJSONObject(x).getInt("id_condicion_pedido"));
                                    Log.d("TAG_", "entro Pedidos");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressDialog.dismiss();
                        Intent i = new Intent(PrincipalCliente.this, PedidosCliente.class);
                        startActivity(i);
                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 23) {
            pedidoCliente = ClienteProductosPorCategoria.pedido;
        }
        if (resultCode == 7) {
            pedidoCliente = DetallePagarCliente.pedido;
        }
        AdaptadorRecyclerViewProductoCliente adaptadorValoradas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(51, 1, "")), tvCantidadArticulosCliente, tvMontoPagar, pedidoCliente, PrincipalCliente.this, linearPagar, clientePrincipal,null);
        AdaptadorRecyclerViewProductoCliente adaptadorFavoritas = new AdaptadorRecyclerViewProductoCliente(FiltrarListaPorCategoria(new Categoria(74, 1, "Favoritas")), tvCantidadArticulosCliente, tvMontoPagar, pedidoCliente, PrincipalCliente.this, linearPagar, clientePrincipal,null);
        rcFavoritas.setItemViewCacheSize(FiltrarListaPorCategoria(new Categoria(74, 1, "Favoritas")).size());
        rcFavoritas.setHasFixedSize(true);
        rcFavoritas.setAdapter(adaptadorFavoritas);
        rcProductosValorados.setItemViewCacheSize(FiltrarListaPorCategoria(new Categoria(51, 1, "")).size());
        rcProductosValorados.setHasFixedSize(true);
        rcProductosValorados.setAdapter(adaptadorValoradas);
        adaptadorCategorias = new AdaptadorCategoriasCliente(lstCategorias, PrincipalCliente.this, pedidoCliente);
        rcCategorias.setAdapter(adaptadorCategorias);
        if (!pedidoCliente.getLstProductoPedido().isEmpty()) {
            linearPagar.setVisibility(View.VISIBLE);
        } else {
            linearPagar.setVisibility(View.INVISIBLE);
        }
    }
    public static ArrayList<Producto> FiltrarListaPorCategoria(Categoria categoria) {
        ArrayList<Producto> lista = new ArrayList<>();
        for (int x = 0; x < lstProducto.size(); x++) {
            if(categoria.getCodigo() == 48){
                for (int c=0; c < clientePrincipal.getLstProductosFavoritos().size(); c++){
                    if(lstProducto.get(x).getCodigo() == clientePrincipal.getLstProductosFavoritos().get(c).getId_producto()){
                       lista.add(lstProducto.get(x));
                    }
                }
            }else{
                for (int c = 0; c < lstProducto.get(x).getLstCategoriasProducto().size(); c++) {
                    if (lstProducto.get(x).getLstCategoriasProducto().get(c).getCodigo() == categoria.getCodigo()) {
                        lista.add(lstProducto.get(x));
                    }
                }
            }
        }
        return lista;
    }



}
