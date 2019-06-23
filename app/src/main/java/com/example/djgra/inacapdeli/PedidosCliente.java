package com.example.djgra.inacapdeli;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosClientes;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;

import java.util.ArrayList;

public class PedidosCliente extends AppCompatActivity {

    Pedido pedido = new Pedido();
    RecyclerView rcPedidos;
    ScrollView scroll;
    Button btnAnteriores, btnActivos;
    ImageButton btnInicio;
    AdaptadorPedidosClientes adaptadorPedidosClientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scroll = (ScrollView) findViewById(R.id.scPedidosCliente);
        rcPedidos = (RecyclerView) findViewById(R.id.rcPedidosCliente);
        btnActivos = findViewById(R.id.btnPedidosActivosCliente);
        btnAnteriores = findViewById(R.id.btnPedidosAnterioresCliente);
        btnInicio = findViewById(R.id.btnInicioPedidos);
        btnAnteriores.setEnabled(false);
        cargarVistaPedidoAnterior();

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    //cargar vistas anterior primero
    private void cargarVistaPedidoAnterior(){
        if(!PrincipalCliente.clientePrincipal.lstPedidosEntregados().isEmpty()){
            scroll.setBackgroundResource(R.drawable.limpiarhistorial);
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(PrincipalCliente.clientePrincipal.lstPedidosEntregados(),PedidosCliente.this,"ENTREGADOS");
            rcPedidos.setHasFixedSize(true);
            rcPedidos.setItemViewCacheSize(PrincipalCliente.clientePrincipal.lstPedidosEntregados().size());
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            //cargar la imagen de vacio
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            scroll.setBackgroundResource(R.drawable.anteriores);
        }

    }

    private void cargarVistaPedidosActivos(){
        if(!PrincipalCliente.clientePrincipal.lstPedidosPendientes().isEmpty()){
            scroll.setBackgroundResource(R.drawable.limpiarhistorial);
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(PrincipalCliente.clientePrincipal.lstPedidosPendientes(),PedidosCliente.this,"PENDIENTES");
            rcPedidos.setHasFixedSize(true);
            rcPedidos.setItemViewCacheSize(PrincipalCliente.clientePrincipal.lstPedidosEntregados().size());
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            //cargar la imagen de vacio
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            scroll.setBackgroundResource(R.drawable.sinactivos);

        }
    }


}
