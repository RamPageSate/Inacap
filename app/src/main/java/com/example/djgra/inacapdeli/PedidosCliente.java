package com.example.djgra.inacapdeli;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosClientes;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorProductoCompradoCliente;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Pedido;

import java.util.ArrayList;

public class PedidosCliente extends AppCompatActivity {

    Pedido pedido = new Pedido();
    RecyclerView rcPedidos;
    Button btnAnteriores, btnActivos;
    ArrayList<Pedido> lstPedidos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);
        rcPedidos = (RecyclerView) findViewById(R.id.rcPedidosCliente);
        btnActivos = findViewById(R.id.btnPedidosActivosCliente);
        btnAnteriores = findViewById(R.id.btnPedidosAnterioresCliente);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
            lstPedidos.add(pedido);
        }

        //quitar caches
        rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        AdaptadorPedidosClientes adaptadorPedidosClientes = new AdaptadorPedidosClientes(lstPedidos,PedidosCliente.this);
        rcPedidos.setAdapter(adaptadorPedidosClientes);


        btnActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivos.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btnAnteriores.setBackgroundColor(Color.parseColor("#808080"));
            }
        });

    }
}
