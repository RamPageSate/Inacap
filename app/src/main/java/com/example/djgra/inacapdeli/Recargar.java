package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.Functions;

import org.json.JSONException;
import org.json.JSONObject;

public class Recargar extends AppCompatActivity {
    Persona vendedodr,  cliente;
    ImageButton btnSalir, btnQr;
    EditText etRecargar;
    public static TextView tvCliente;
    Button btnRecargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recargar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            vendedodr = (Persona) bundle.getSerializable("usr");
        }
        btnRecargar = findViewById(R.id.btnRecargar);
        btnSalir = findViewById(R.id.btnSalirRecargar);
        btnQr = findViewById(R.id.btnLectorQrRecargar);
        tvCliente = findViewById(R.id.tvClienteRecargar);
        etRecargar =  findViewById(R.id.etRecargar);


        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recargar.this, LectoQr.class);
                startActivityForResult(intent,99);
            }
        });

        btnRecargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cliente != null){
                    if(etRecargar.getText().toString().isEmpty() || etRecargar.getText().toString().equals("0")){
                        etRecargar.setError("Ingrese Monto");
                    }else{
                        int saldo = Integer.parseInt(etRecargar.getText().toString()) + cliente.getSaldo();
                        cliente.setSaldo(saldo);
                        final ProgressDialog progressDialog = Functions.CargarDatos("Recargando Saldo..",Recargar.this);
                        BddPersonas.updatePersona(cliente, Recargar.this, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Toast.makeText(Recargar.this, "Recarga Exitosa", Toast.LENGTH_SHORT).show();
                                cliente = null;
                                etRecargar.setText("");
                                tvCliente.setText("Cliente ->");
                                progressDialog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Recargar.this, "No se Pudo Recargar", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }else{
                    Toast.makeText(Recargar.this, "Escanear Cliente Para Recargar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {
            final ProgressDialog progressDialog = Functions.CargarDatos("Cargando Cliente.. !", Recargar.this);
            BddPersonas.getPersona(tvCliente.getText().toString(), Recargar.this, new Response.Listener<String>() {
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
                            cliente = persona;
                            tvCliente.setText("Cliente -> " + cliente.getNombre() + " " + cliente.getApellido());
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
