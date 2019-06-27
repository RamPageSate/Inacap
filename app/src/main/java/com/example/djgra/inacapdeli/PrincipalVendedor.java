package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PrincipalVendedor extends AppCompatActivity {
    ImageButton btnPorPreparar, btnEntregarPedido, btnHistorialPedidos, btnCerrarSesion;
    Persona vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_vendedor);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendedor = (Persona) bundle.getSerializable("usr");
        }
        btnPorPreparar = findViewById(R.id.btnPrepararPedido);
        btnEntregarPedido = findViewById(R.id.btnEntregarPedido);


        btnEntregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalVendedor.this, EntregarPedido.class);
                startActivity(i);
            }
        });


        btnPorPreparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Pedidos..!", PrincipalVendedor.this);
                final ArrayList<Pedido> lstPedido = new ArrayList<>();
                BddPedido.getPedidoFaltante(PrincipalVendedor.this, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.toString().equals("[]")) {
                            for (int x = 0; x < response.length(); x++) {
                                final Pedido pedido = new Pedido();
                                try {
                                    pedido.setCodigo(response.getJSONObject(x).getInt("pedido_id"));
                                    pedido.setFechaPedido(response.getJSONObject(x).getString("pedido_fecha_hora"));
                                    pedido.setPedido_estado(response.getJSONObject(x).getInt("pedido_estado"));
                                    pedido.setId_cliente(response.getJSONObject(x).getInt("id_cliente"));
                                    pedido.setId_vendedor(response.getJSONObject(x).getInt("id_vendedor"));
                                    pedido.setId_condicion_pedido(response.getJSONObject(x).getInt("id_condicion_pedido"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                lstPedido.add(pedido);
                            }
                        }
                        progressDialog.dismiss();
                        Intent i = new Intent(PrincipalVendedor.this, PrepararPedido.class);
                        i.putExtra("usr", vendedor);
                        i.putExtra("lstPedidosPorPreparar", lstPedido);
                        //enviar los pedidos con id_condicion_2
                        startActivity(i);
                    }
                }, null);
            }
        });

    }
}
