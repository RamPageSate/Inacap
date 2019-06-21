package com.example.djgra.inacapdeli;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.djgra.inacapdeli.Clases.Persona;

public class DatosCliente extends AppCompatActivity {
    Persona cliente = new Persona();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            cliente = (Persona) bundle.getSerializable("cliente");
        }
    }
}
