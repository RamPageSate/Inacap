package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.Functions;

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
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        btnIniciar = (Button) findViewById(R.id.btnIngresar);
        cbRecordarme = (CheckBox) findViewById(R.id.cbRecordarme);
        tvRecuperarPass = (TextView) findViewById(R.id.tvRecuperarPass);
        tvPoliticas = (TextView) findViewById(R.id.tvPolitica);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Cargando...");
                progressDialog.show();
                Response.Listener<String> respuesta = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.hide();
                            JSONObject jsonRespuesta = new JSONObject(response);
                            String clave = jsonRespuesta.getString("persona_contrasena");
                            if(clave.equalsIgnoreCase(etPass.getText().toString())){
                                Persona persona = new Persona();
                                persona.setNombre(jsonRespuesta.getString("persona_nombre"));
                                persona.setApellido(jsonRespuesta.getString("persona_apellido"));
                                persona.setContrasena(jsonRespuesta.getString("persona_contrasena"));
                                //persona.setFoto(jsonRespuesta.getInt("persona_foto"));
                                persona.setCorreo(jsonRespuesta.getString("persona_email"));
                                //persona.setCodigoQr(jsonRespuesta.getInt("persona_codigo_qr"));
                                persona.setSaldo(jsonRespuesta.getInt("persona_saldo"));
                                persona.setEstado(jsonRespuesta.getInt("persona_estado"));
                                persona.setSede(jsonRespuesta.getInt("id_sede"));
                                persona.setRol(jsonRespuesta.getInt("id_rol"));
                                if(persona.getRol() == 3){
                                    Intent intent = new Intent(Login.this,PrincipalAdministrador.class);
                                    intent.putExtra("persona",persona);
                                    startActivity(intent);
                                }
                                if(persona.getRol() == 1){
                                    Intent intent = new Intent(Login.this,PrincipalCliente.class);
                                    intent.putExtra("persona",persona);
                                    startActivity(intent);
                                }
                                if(persona.getRol() == 2){
                                    Intent intent = new Intent(Login.this,PrincipalCliente.class);
                                    intent.putExtra("persona",persona);
                                    startActivity(intent);
                                }
                            }else{
                                AlertDialog.Builder alerta = new AlertDialog.Builder(Login.this);
                                alerta.setMessage("Datos Incorrectos")
                                        .setNegativeButton("Reintentar",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //parametros de la consulta

                    Intent intent = new Intent(Login.this,PrincipalAdministrador.class);
                    startActivity(intent);
            }
        });

    }
}
