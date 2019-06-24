package com.example.djgra.inacapdeli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;

import java.util.ArrayList;

public class PrincipalVendedor extends AppCompatActivity {
    ImageButton btnPorPreparar, btnEntregarPedido, btnHistorialPedidos, btnCerrarSesion;
    Persona vendedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_vendedor);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
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
                Intent i = new Intent(PrincipalVendedor.this, PrepararPedido.class);
                ArrayList<Pedido> lstPedido  = new ArrayList<>();
                Pedido pedido = new Pedido();
                pedido.setCodigo(23);
                pedido.setFechaPedido("2019/06/24 13:14:24");
                lstPedido.add(pedido);
                pedido = new Pedido();
                pedido.setCodigo(123324);
                pedido.setFechaPedido("2019/06/24 13:15:34");
                lstPedido.add(pedido);
                pedido = new Pedido();
                pedido.setCodigo(123325);
                pedido.setFechaPedido("2019/06/24 13:16:44");
                lstPedido.add(pedido);
                pedido.setCodigo(123326);
                pedido.setFechaPedido("2019/06/24 13:14:24");
                lstPedido.add(pedido);
                pedido = new Pedido();
                pedido.setCodigo(123327);
                pedido.setFechaPedido("2019/06/24 13:15:34");
                lstPedido.add(pedido);
                pedido = new Pedido();
                pedido.setCodigo(123328);
                pedido.setFechaPedido("2019/06/24 13:16:44");
                lstPedido.add(pedido);
                i.putExtra("usr", vendedor);
                i.putExtra("lstPedidosPorPreparar",lstPedido);
                //enviar los pedidos con id_condicion_2
                startActivity(i);
            }
        });

    }
}
