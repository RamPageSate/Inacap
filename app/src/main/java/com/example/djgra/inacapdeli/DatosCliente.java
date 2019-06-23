package com.example.djgra.inacapdeli;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djgra.inacapdeli.AlertDialog.AlertDialogActualizarPerfil;
import com.example.djgra.inacapdeli.Clases.Persona;

public class DatosCliente extends AppCompatActivity {
    Persona cliente = new Persona();
    EditText etNombre, etEmail, etPass, etApellido;
    Button btnActualizarPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etNombre = findViewById(R.id.etNombrePerfil);
        etApellido = findViewById(R.id.etApellidoPerfil);
        etEmail = findViewById(R.id.etEmailPerfil);
        etPass = findViewById(R.id.etPassPerfil);
        btnActualizarPerfil= findViewById(R.id.btnActualizarPerfil);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            cliente = (Persona) bundle.getSerializable("cliente");
            etNombre.setText(cliente.getNombre());
            etEmail.setText(cliente.getCorreo());
            etApellido.setText(cliente.getApellido());
            etPass.setText(cliente.getContrasena());

        }
        etNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this,"NOMBRE",cliente,etNombre);
                alertDialogActualizarPerfil.show();
            }
        });
        etApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this,"APELLIDO",cliente,etApellido);
                alertDialogActualizarPerfil.show();
            }
        });
        etPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this,"PASS",cliente,etPass);
                alertDialogActualizarPerfil.show();
            }
        });
        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void mostrarVista(String dato){


    }


}
