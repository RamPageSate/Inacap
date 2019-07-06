package com.example.djgra.inacapdeli;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.djgra.inacapdeli.Clases.Persona;

public class EntregarPedido extends AppCompatActivity {
    ImageButton btnQr, btnLista, btnVolver;

    Persona vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar_pedido);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnLista = findViewById(R.id.btnListaEntregarPedido);
        btnQr = findViewById(R.id.btnQrEntregarPedido);
        btnVolver = findViewById(R.id.btnVolverEntregarPedido);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            vendedor = (Persona) bundle.getSerializable("usr");
        }
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntregarPedido.this, EntregarPedidoPorCodigoQr.class);
                intent.putExtra("usr", vendedor);
                startActivity(intent);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });




    }
}
