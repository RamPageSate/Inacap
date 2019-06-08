package com.example.djgra.inacapdeli;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.djgra.inacapdeli.Funciones.BddSede;
import com.example.djgra.inacapdeli.Funciones.Functions;

public class Inicio extends AppCompatActivity {
    Button btnCrearCuenta, btnIniciaSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

           //referencias

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        btnIniciaSesion = (Button) findViewById(R.id.btnIniciaSesion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnIniciaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,  Registrar_Persona.class);
                startActivity(intent);
            }
        });



    }
}
