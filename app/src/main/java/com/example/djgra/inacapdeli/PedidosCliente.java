package com.example.djgra.inacapdeli;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosClientes;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;

import java.util.ArrayList;

public class PedidosCliente extends AppCompatActivity {

    Pedido pedido = new Pedido();
    RecyclerView rcPedidos;
    ScrollView scroll;
    Persona cliente = new Persona();
    Button btnAnteriores, btnActivos;
    AdaptadorPedidosClientes adaptadorPedidosClientes;
    ArrayList<Pedido> lstPedidos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scroll = (ScrollView) findViewById(R.id.scPedidosCliente);
        rcPedidos = (RecyclerView) findViewById(R.id.rcPedidosCliente);
        btnActivos = findViewById(R.id.btnPedidosActivosCliente);
        btnAnteriores = findViewById(R.id.btnPedidosAnterioresCliente);
        //inicia con las compras ya entregadas anteriormente     condicion 2 pendiente
        btnAnteriores.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (Persona) bundle.getSerializable("cliente");
            cargarVistaPedidoAnterior();
        }

        //condicion 1 entregado 2 pendiente



        btnActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivos.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btnAnteriores.setBackgroundColor(Color.parseColor("#B2BABB"));
                cargarVistaPedidosActivos();
                btnAnteriores.setEnabled(true);
            }
        });

        // hacer adapter si no hay pedido anterior o historial  cuando ingresa el btn anteriores debera estar deshabilitado
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
        if(!cliente.lstPedidosEntregados().isEmpty()){
            scroll.setBackgroundResource(R.drawable.limpiarhistorial);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(cliente.lstPedidosEntregados(),PedidosCliente.this);
            adaptadorPedidosClientes.notifyDataSetChanged();
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            //cargar la imagen de vacio
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            scroll.setBackgroundResource(R.drawable.anteriores);
        }

    }

    private void cargarVistaPedidosActivos(){
        if(!cliente.lstPedidosPendientes().isEmpty()){
            scroll.setBackgroundResource(R.drawable.limpiarhistorial);
            rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adaptadorPedidosClientes = new AdaptadorPedidosClientes(cliente.lstPedidosPendientes(),PedidosCliente.this);
            adaptadorPedidosClientes.notifyDataSetChanged();
            rcPedidos.setAdapter(adaptadorPedidosClientes);
        }else{
            //cargar la imagen de vacio
            adaptadorPedidosClientes = null;
            rcPedidos.setAdapter(adaptadorPedidosClientes);
            scroll.setBackgroundResource(R.drawable.sinactivos);

        }
    }


}
