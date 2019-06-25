package com.example.djgra.inacapdeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class EntregarPedido extends AppCompatActivity {
    ImageButton btnQr, btnLista, btnVolver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar_pedido);
        btnLista = findViewById(R.id.btnListaEntregarPedido);
        btnQr = findViewById(R.id.btnQrEntregarPedido);
        btnVolver = findViewById(R.id.btnVolverEntregarPedido);









    }
}
