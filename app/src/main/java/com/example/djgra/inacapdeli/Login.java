package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnIniciar;
    CheckBox cbRecordarme;
    TextView tvRecuperarPass, tvPoliticas;
    ProgressDialog progressDialog;


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

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String contraseña = etPass.getText().toString();
                BddPersonas.getPersona(email, Login.this, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("[]")) {
                            try {
                                JSONObject objeto = new JSONObject(response);
                                String correo = objeto.getString("persona_email");
                                String contrasena = objeto.getString("persona_contrasena");
                                int rol = objeto.getInt("id_rol");
                                if (correo.equals(email) && contrasena.equals(contraseña)) {
                                    switch (rol) {
                                        case (1):
                                            break;
                                        case (2):
                                            break;
                                        case (3):
                                            Intent i = new Intent(Login.this, PrincipalAdministrador.class);
                                            startActivity(i);
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
        });

    }

    public void Copio() {

    }
}
