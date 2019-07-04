package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosQr;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class EntregarPedidoPorCodigoQr extends AppCompatActivity {
    ImageButton btnAtras, lectorQr;
    CheckBox cbRetirarTodo;
    Button btnEntregar;
    ListView lstvPedidos;
    public static TextView tvCliente;
    Persona cliente, vendedor;
    AdaptadorPedidosQr adaptadorPedidosQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar_pedido_por_codigo_qr);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnAtras = findViewById(R.id.btnSalirQr);
        lstvPedidos = findViewById(R.id.lstvEntregarPedidoQr);
        tvCliente = findViewById(R.id.tvClienteEntregarPedidoQr);
        lectorQr = findViewById(R.id.lectorQr);
        cbRetirarTodo = findViewById(R.id.cbRetirarTodo);
        btnEntregar = findViewById(R.id.btnEntregarPedidoFinal);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendedor = (Persona) bundle.getSerializable("usr");
        }
        lectorQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntregarPedidoPorCodigoQr.this, LectoQr.class);
                startActivityForResult(intent, 99);
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCliente.setText("Cliente -> ");
                onBackPressed();
                finish();
            }
        });

        btnEntregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {
            final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Cliente.. !", EntregarPedidoPorCodigoQr.this);
            BddPersonas.getPersona(tvCliente.getText().toString(), EntregarPedidoPorCodigoQr.this, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("[]")) {
                        try {
                            JSONObject objeto = new JSONObject(response);
                            final Persona persona = new Persona();
                            persona.setNombre(objeto.getString("persona_nombre"));
                            persona.setApellido(objeto.getString("persona_apellido"));
                            persona.setCodigo(objeto.getInt("persona_id"));
                            cliente = persona;
                            tvCliente.setText("Cliente -> " + cliente.getNombre() + " " + cliente.getApellido());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    BddPedido.getPedidoListo(vendedor, EntregarPedidoPorCodigoQr.this, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (!response.toString().equals("[]")) {
                                for (int x = 0; x < response.length(); x++) {
                                    final Pedido pedido = new Pedido();
                                    try {
                                        pedido.setId_cliente(response.getJSONObject(x).getInt("id_cliente"));
                                        if (pedido.getId_cliente() == cliente.getCodigo()) {
                                            pedido.setCodigo(response.getJSONObject(x).getInt("pedido_id"));
                                            pedido.setFechaPedido(response.getJSONObject(x).getString("pedido_fecha_hora"));
                                            pedido.setPedido_estado(response.getJSONObject(x).getInt("pedido_estado"));
                                            pedido.setId_vendedor(vendedor.getCodigo());
                                            BddProductos.getProductoByPedido(pedido.getCodigo(), EntregarPedidoPorCodigoQr.this, new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    if (!response.equals("[]")) {
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
                                                                pedido.agregarProductoListaPedido(producto);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        cliente.agregarPedido(pedido);
                                                        adaptadorPedidosQr = new AdaptadorPedidosQr(cliente.getLstPedidos(), EntregarPedidoPorCodigoQr.this, cbRetirarTodo);
                                                        lstvPedidos.setAdapter(adaptadorPedidosQr);
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            }, null);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }, null);
                }
            });
        }

    }

}
