package com.example.djgra.inacapdeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.Functions;

public class PrincipalCliente extends AppCompatActivity {
    ImageView fotoUsr;
    public static Persona cliente = new Persona();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cliente);

        fotoUsr= (ImageView) findViewById(R.id.imgFotoCliente);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (Persona) bundle.getSerializable("usr");
            fotoUsr.setImageBitmap(Functions.StringToBitMap(cliente.getFoto()));
        }
    }
}
