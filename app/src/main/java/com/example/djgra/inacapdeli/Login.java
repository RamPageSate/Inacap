package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.AlertDialog.AlertDialogRecuperarPass;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Producto_Favorito;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.BddProductos;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnIniciar;
    CheckBox cbRecordarme;
    TextView tvRecuperarPass, tvPoliticas;
    ProgressDialog progressDialog;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnIniciar = findViewById(R.id.btnIngresar);
        cbRecordarme = findViewById(R.id.cbRecordarme);
        tvRecuperarPass = findViewById(R.id.tvRecuperarPass);
        tvPoliticas = findViewById(R.id.tvPolitica);
        preferences = getSharedPreferences("usuarioConectado", Context.MODE_PRIVATE);
        if (!preferences.getString("email", "").isEmpty() && !preferences.getString("pass", "").isEmpty()) {
            etEmail.setText(preferences.getString("email", ""));
            etPass.setText(preferences.getString("pass", ""));
            cbRecordarme.setChecked(true);
            Logeo();

        }
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logeo();
            }

        });

        tvRecuperarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogRecuperarPass alert = new AlertDialogRecuperarPass(Login.this);
                alert.show();
            }
        });
    }

    public void Logeo() {
        final ProgressDialog progress = Functions.CargarDatos("Conectando", Login.this);
        progress.show();
        progress.setCancelable(false);

        Boolean validador = true;
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("CAMPO OBLIGATORIO");
            progress.hide();
            validador = false;
        }
        if (etPass.getText().toString().isEmpty()) {
            etPass.setError("CAMPO OBLIGATORIO");
            validador = false;
            progress.hide();
        }
        if (validador) {

            final SharedPreferences.Editor editor = preferences.edit();

            final String email = etEmail.getText().toString();

            final String contraseña = etPass.getText().toString();


            BddPersonas.getPersona(email, Login.this, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("[]")) {
                        try {
                            JSONObject objeto = new JSONObject(response);
                            final Persona persona = new Persona();
                            persona.setCorreo(objeto.getString("persona_email"));
                            persona.setContrasena(objeto.getString("persona_contrasena"));
                            persona.setNombre(objeto.getString("persona_nombre"));
                            persona.setApellido(objeto.getString("persona_apellido"));
                            persona.setFoto(objeto.getString("persona_foto"));
                            persona.setCodigoQr(objeto.getString("persona_codigo_qr"));
                            persona.setEstado(objeto.getInt("persona_estado"));
                            persona.setRol(objeto.getInt("id_rol"));
                            persona.setCodigo(objeto.getInt("persona_id"));
                            persona.setSaldo(objeto.getInt("persona_saldo"));
                            persona.setSede(objeto.getInt("id_sede"));
                            if (persona.getCorreo().equals(email) && persona.getContrasena().equals(contraseña)) {
                                if (cbRecordarme.isChecked()) {
                                    editor.putString("email", persona.getCorreo());
                                    editor.putString("pass", persona.getContrasena());
                                    editor.commit();
                                }
                                switch (persona.getRol()) {
                                    case (1):
                                        final Intent c = new Intent(Login.this, PrincipalCliente.class);
                                        BddProductos.getProductoFavorito(persona.getCodigo(), Login.this, new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                if (!response.equals("[]")) {
                                                    for (int x = 0; x < response.length(); x++) {
                                                        try {
                                                            Producto_Favorito producto_favorito = new Producto_Favorito();
                                                            producto_favorito.setId_cliente(persona.getCodigo());
                                                            producto_favorito.setId_producto(response.getJSONObject(x).getInt("id_producto"));
                                                            persona.addProductoFavorito(producto_favorito);
                                                        }catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                c.putExtra("usr", persona);
                                                progress.dismiss();
                                                startActivity(c);
                                                finish();

                                            }
                                        }, null);
                                        break;
                                    case (2):
                                        Intent inten = new Intent(Login.this, PrincipalVendedor.class);
                                        inten.putExtra("usr", persona);
                                        startActivity(inten);
                                        break;
                                    case (3):
                                        Intent i = new Intent(Login.this, PrincipalAdministrador.class);
                                        i.putExtra("admin", persona);
                                        startActivity(i);
                                        break;
                                }
                            } else {
                                etEmail.setError("Correo y/o contraseña Incorrectas");
                                etPass.setError("Correo y/o contraseña Incorrectas");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        etEmail.setError("Correo y/o contraseña Incorrectas");
                        etPass.setError("Correo y/o contraseña Incorrectas");
                    }
                }
            });
        }

    }


}
