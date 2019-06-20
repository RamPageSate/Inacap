package com.example.djgra.inacapdeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPedidosClientes;
import com.example.djgra.inacapdeli.Clases.Pedido;

import java.util.ArrayList;

public class PedidosCliente extends AppCompatActivity {

    Pedido pedido = new Pedido();
    RecyclerView rcPedidos;
    ArrayList<Pedido> lstPedido = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);

        rcPedidos = (RecyclerView) findViewById(R.id.rcPedidosCliente);
        rcPedidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
            lstPedido.add(pedido);
        }
        AdaptadorPedidosClientes adaptadorPedidosClientes = new AdaptadorPedidosClientes(lstPedido,PedidosCliente.this);
        rcPedidos.setAdapter(adaptadorPedidosClientes);


    }
}
