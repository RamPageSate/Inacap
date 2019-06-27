package com.example.djgra.inacapdeli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosPorPreparar;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorProductosPorPreparar;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogPrepararProductoPedidos;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PrepararPedido extends AppCompatActivity {
    ImageButton btnSalir;
    ListView lstPedidosPorPreparar;
    Persona vendedor = new Persona();
    ArrayList<Pedido> lstPedido = new ArrayList<>();
    AdaptadorPedidosPorPreparar adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparar_pedido);
        Bundle bundle = getIntent().getExtras();
        lstPedidosPorPreparar = (ListView) findViewById(R.id.lstPedidosPorPreparar);
        btnSalir = (ImageButton) findViewById(R.id.btnSalirPorPreparar);
        if(bundle != null){
            lstPedido = (ArrayList<Pedido>) bundle.getSerializable("lstPedidosPorPreparar");
            vendedor = (Persona) bundle.getSerializable("usr");
            llenarTabla();
        }

        lstPedidosPorPreparar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Productos... !", PrepararPedido.this);
                BddProductos.getProductoByPedido(lstPedido.get(position).getCodigo(), PrepararPedido.this, new Response.Listener<JSONArray>() {
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
                                    lstPedido.get(position).agregarProductoListaPedido(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            progressDialog.dismiss();
                            AlertDialogPrepararProductoPedidos alert = new AlertDialogPrepararProductoPedidos(PrepararPedido.this,lstPedido.get(position).getLstProductoPedido(),lstPedido.get(position).getCodigo(),lstPedido,lstPedidosPorPreparar);
                            alert.show();
                        }

                    }
                }, null);
            }
        });


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstPedido.removeAll(lstPedido);
                onBackPressed();
            }
        });

    }
    public void llenarTabla(){
        adaptador = new AdaptadorPedidosPorPreparar(lstPedido,PrepararPedido.this);
        lstPedidosPorPreparar.setAdapter(adaptador);
        lstPedidosPorPreparar.deferNotifyDataSetChanged();
    }
}

