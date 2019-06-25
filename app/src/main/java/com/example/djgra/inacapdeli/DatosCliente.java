package com.example.djgra.inacapdeli;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogActualizarPerfil;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.Functions;

import java.io.IOException;

public class DatosCliente extends AppCompatActivity {
    Persona cliente = new Persona();
    EditText etNombre, etEmail, etPass, etApellido;
    Persona NuevosDatos = new Persona();
    boolean Actualizo = false;
    ImageView imagen;
    ImageButton btnInicio;
    Button btnActualizarPerfil, btnCerrarSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etNombre = findViewById(R.id.etNombrePerfil);
        etApellido = findViewById(R.id.etApellidoPerfil);
        etEmail = findViewById(R.id.etEmailPerfil);
        btnCerrarSession = findViewById(R.id.btnCerrarSesion);
        etPass = findViewById(R.id.etPassPerfil);
        btnActualizarPerfil = findViewById(R.id.btnActualizarPerfil);
        imagen = findViewById(R.id.imgActualizarPerfil);
        btnInicio = findViewById(R.id.btnInicioPerfil);
        btnActualizarPerfil.setEnabled(false);
        btnActualizarPerfil.setBackgroundColor(Color.parseColor("#808080"));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (Persona) bundle.getSerializable("cliente");
            etNombre.setText(cliente.getNombre());
            etEmail.setText(cliente.getCorreo());
            etApellido.setText(cliente.getApellido());
            etPass.setText(cliente.getContrasena());
            imagen.setImageBitmap(Functions.StringToBitMap(cliente.getFoto()));
            NuevosDatos.setCodigo(cliente.getCodigo());
            NuevosDatos.setNombre(cliente.getNombre());
            NuevosDatos.setApellido(cliente.getApellido());
            NuevosDatos.setFoto(cliente.getFoto());
            NuevosDatos.setContrasena(cliente.getContrasena());
            NuevosDatos.setSaldo(cliente.getSaldo());
        }

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        btnCerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("usuarioConectado", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorPreferencias = preferences.edit();
                editorPreferencias.clear();
                editorPreferencias.commit();
                Intent intent =  new Intent(DatosCliente.this, Inicio.class);
                startActivity(intent);
            }
        });

        etNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this, "NOMBRE", NuevosDatos, etNombre, btnActualizarPerfil);
                alertDialogActualizarPerfil.show();
            }
        });
        etApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this, "APELLIDO", NuevosDatos, etApellido, btnActualizarPerfil);
                alertDialogActualizarPerfil.show();
            }
        });
        etPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogActualizarPerfil alertDialogActualizarPerfil = new AlertDialogActualizarPerfil(DatosCliente.this, "PASS", NuevosDatos, etPass, btnActualizarPerfil);
                alertDialogActualizarPerfil.show();
            }
        });
        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = Functions.CargarDatos("Actualizando Perfil", DatosCliente.this);
                BddPersonas.updatePersona(NuevosDatos, DatosCliente.this, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(DatosCliente.this);
                        builder.setTitle("Notificacion");
                        builder.setMessage("Actualizo Los Datos");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cliente.setNombre(NuevosDatos.getNombre());
                                cliente.setContrasena(NuevosDatos.getContrasena());
                                cliente.setApellido(NuevosDatos.getApellido());
                                cliente.setFoto(NuevosDatos.getFoto());
                                Actualizo=true;
                                SharedPreferences preferences = getSharedPreferences("usuarioConectado", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorPreferencias = preferences.edit();
                                editorPreferencias.clear();
                                editorPreferencias.putString("email", cliente.getCorreo());
                                editorPreferencias.putString("pass", cliente.getContrasena());
                                editorPreferencias.commit();
                                Intent intent = new Intent(DatosCliente.this,Login.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog mostrar = builder.create();
                        mostrar.setCancelable(false);
                        mostrar.setCanceledOnTouchOutside(false);
                        mostrar.show();
                    }
                }, Functions.FalloInternet(DatosCliente.this,progressDialog,"No pudo Actualizar Perfil"));
            }
        });
    }

    private void cargarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la Aplicacion"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if (resultCode == RESULT_OK) {
                Uri path = data.getData();
                try {
                    //Cómo obtener el mapa de bits de la Galería
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    //Configuración del mapa de bits en ImageView
                    NuevosDatos.setFoto(Functions.getStringImage(bitmap));
                    btnActualizarPerfil.setEnabled(true);
                    btnActualizarPerfil.setBackgroundColor(Color.parseColor("#FF0000"));
                    imagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }


}
