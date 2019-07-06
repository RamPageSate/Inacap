package com.example.djgra.inacapdeli;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosClientes;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogQr;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.Functions;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
public class PedidosCliente extends AppCompatActivity {

    RecyclerView rcPedidos;
    Persona cliente = new Persona();
    ImageView imagensinPedido;
    Button btnAnteriores, btnActivos;
    ArrayList<Pedido> lstPedidosPorRetirar = new ArrayList<>();
    ImageButton btnInicio;
    LinearLayout linearSeleccionarTodo;
    Button btnRetirar;
    int code=0;
    AdaptadorPedidosClientes adaptadorPedidosClientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pedidos_cliente);
        rcPedidos = findViewById(R.id.rcPedidosCliente);
        btnActivos = findViewById(R.id.btnPedidosActivosCliente);
        imagensinPedido = findViewById(R.id.fondossinpedidos);
        btnAnteriores = findViewById(R.id.btnPedidosAnterioresCliente);
        btnInicio = findViewById(R.id.btnInicioPedidos);
        btnRetirar = findViewById(R.id.btnRetirarPedidoActivo);
        linearSeleccionarTodo = findViewById(R.id.linearSeleccionarTodod);
        btnAnteriores.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            cliente = (Persona) bundle.getSerializable("cliente");
            code = bundle.getInt("code");
        }
        final ProgressDialog progressDialog = Functions.CargarDatos("Cangando Historial", PedidosCliente.this);
        BddPedido.getPedidoByCliente(cliente.getCodigo(), PedidosCliente.this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!response.equals("[]")) {
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            final Pedido pedido = new Pedido();
                            pedido.setCodigo(response.getJSONObject(x).getInt("pedido_id"));
                            BddProductos.getProductoByPedido(pedido.getCodigo(), PedidosCliente.this, new Response.Listener<JSONArray>() {
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
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        pedido.setLstProductoPedido(lista);
                                        Log.d("TAG_", "CP-> " + pedido.getCodigo());
                                        cliente.agregarPedido(pedido);
                                    }
                                }
                            }, Functions.FalloInternet(PedidosCliente.this, progressDialog, "No pudo Cargar"));
                            pedido.setFechaPedido(response.getJSONObject(x).getString("pedido_fecha_hora"));
                            pedido.setPedido_estado(response.getJSONObject(x).getInt("pedido_estado"));
                            pedido.setId_cliente(response.getJSONObject(x).getInt("id_cliente"));
                            pedido.setId_vendedor(response.getJSONObject(x).getInt("id_vendedor"));
                            pedido.setId_condicion_pedido(response.getJSONObject(x).getInt("id_condicion_pedido"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                    cargarVistaPedidoAnterior();
                }
            }
        });


        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code == 3){
                    PrincipalCliente.pedidoCliente = new Pedido();
                }
                onBackPressed();
                finish();
            }
        });

        btnActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivos.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btnAnteriores.setBackgroundColor(Color.parseColor("#B2BABB"));
                cargarVistaPedidosActivos();
                btnAnteriores.setEnabled(true);
            }
        });


        btnAnteriores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAnteriores.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btnActivos.setBackgroundColor(Color.parseColor("#B2BABB"));
                cargarVistaPedidoAnterior();

            }
        });
        btnRetirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialogQr alertDialogQr = new AlertDialogQr(PedidosCliente.this,cliente,"RETIRAR");
                    alertDialogQr.show();
            }
        });
    }
    private void cargarVistaPedidoAnterior(){
        linearSeleccionarTodo.setVisibility(View.INVISIBLE);
        if(!cliente.lstPedidosEntregados().isEmpty()){
            imagensinPedido.setVisibility(View.INVISIBLE);
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(cliente.lstPedidosEntregados(),PedidosCliente.this,"ENTREGADOS",lstPedidosPorRetirar);
            rcPedidos.setHasFixedSize(true);
            rcPedidos.setItemViewCacheSize(cliente.lstPedidosEntregados().size());
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            imagensinPedido.setImageDrawable(getResources().getDrawable(R.drawable.anteriores));
            imagensinPedido.setVisibility(View.VISIBLE);
        }

    }

    private void cargarVistaPedidosActivos(){
        if(!cliente.lstPedidosPendientes().isEmpty()){
            imagensinPedido.setVisibility(View.INVISIBLE);
            linearSeleccionarTodo.setVisibility(View.VISIBLE);
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(cliente.lstPedidosPendientes(),PedidosCliente.this,"PENDIENTES",lstPedidosPorRetirar);
            rcPedidos.setHasFixedSize(true);
            rcPedidos.setItemViewCacheSize(cliente.lstPedidosEntregados().size());
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            linearSeleccionarTodo.setVisibility(View.INVISIBLE);
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            imagensinPedido.setVisibility(View.VISIBLE);
            imagensinPedido.setImageDrawable(getResources().getDrawable(R.drawable.sinactivos));

        }
    }


}
