package com.example.djgra.inacapdeli;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.BddSede;
import com.example.djgra.inacapdeli.Funciones.Functions;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar_Persona extends AppCompatActivity {


    ImageView btnImagen;
    Button btnRegistrarse;
    EditText etNombre, etApellido, etCorreo, etContraseña, etConfirmar;
    Spinner spSede;
    Bitmap bitmap;

    Sede sede;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar__persona);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Functions.CargarDatos("Sedes",this);
        //referencias
        etNombre = findViewById(R.id.etNombreRegistro);
        etApellido = findViewById(R.id.etApellidoRegistro);
        etCorreo = findViewById(R.id.etEmailRegistro);
        etContraseña = findViewById(R.id.etContrasenaRegistro);
        etConfirmar = findViewById(R.id.etRepetirContrasenaRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarUsuario);
        btnImagen = findViewById(R.id.btnImagen);
        spSede = findViewById(R.id.spSede);

        final ArrayList<Sede> lstSedes = new ArrayList<>();
        progressDialog = Functions.CargarDatos(getString(R.string.alert_carga), Registrar_Persona.this);
        BddSede.getSede(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i <= response.length(); i++) {
                    try {
                        int codigo = response.getJSONObject(i).getInt("sede_id");
                        int estado = response.getJSONObject(i).getInt("sede_estado");
                        String direccion = response.getJSONObject(i).getString("sede_direccion");
                        Sede sede = new Sede(codigo, estado, direccion);
                        if (sede.getEstado() != 0) {
                            lstSedes.add(sede);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                spSede.setAdapter(new ArrayAdapter<Sede>(Registrar_Persona.this, android.R.layout.simple_list_item_1, lstSedes));
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        spSede.setAdapter(new ArrayAdapter<Sede>(Registrar_Persona.this, android.R.layout.simple_list_item_1, lstSedes));

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        spSede.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sede = (Sede) spSede.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario() == 0) {
                    //nombre, apellido, pass, email, foto, codigo_qr, sede
                    final Persona per = new Persona();
                    per.setApellido(etApellido.getText().toString());
                    per.setNombre(etNombre.getText().toString());
                    per.setCorreo(etCorreo.getText().toString());
                    per.setSede(sede.getCodigo());
                    per.setContrasena(etContraseña.getText().toString());
                    if (btnImagen.getDrawable() != null) {
                        Bitmap bit = ((BitmapDrawable) btnImagen.getDrawable()).getBitmap();
                        per.setFoto(Functions.getStringImage(bit));
                    } else {
                        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.defaultuser);
                        per.setFoto(Functions.getStringImage(bit));

                    }

                    String email = per.getCorreo();
                    Bitmap qr = QRCode.from(email).bitmap();
                    per.setCodigoQr(Functions.getStringImage(qr));

                    progressDialog = Functions.CargarDatos("Agregando Persona", Registrar_Persona.this);
                    progressDialog.show();
                    BddPersonas.setPersona(per, Registrar_Persona.this, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            progressDialog.hide();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Registrar_Persona.this);
                            builder.setTitle("Notificacion");
                            builder.setMessage("Se a agregado correctamente");
                            builder.setPositiveButton("1", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i = new Intent(Registrar_Persona.this, Login.class);
                                    startActivity(i);
                                }
                            });
                            AlertDialog mostrar = builder.create();
                            mostrar.setCancelable(false);
                            mostrar.setCanceledOnTouchOutside(false);
                            mostrar.show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Registrar_Persona.this, "El correo ya existe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    //cipher para las contraseñas
    private void cargarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la Aplicacion"), 10);
    }

    private boolean validarEmail() { //falta hacer la expresion regular antes del inacao
        boolean ok = false;
        String correo = etCorreo.getText().toString().trim();
        Pattern regex = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$");
        Matcher m = regex.matcher(etCorreo.getText().toString());
        boolean as = m.find();
        if (as == true) {
            ok = true;
        }
        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese Correo");
            ok = false;
        } else {
            if (ok == true) {
                if (correo.contains("@inacapmail.cl") || correo.contains("@inacap.cl")) {
                    ok = true;
                } else {
                    etCorreo.setError("Ingrese Correo de Inacap");
                    ok = false;
                }
            } else {
                etCorreo.setError("Correo mal Ingresado");
            }
        }
        return ok;
    }

    private int validarFormulario() {
        int ok = 0;
        //validar solo letras
        if (etNombre.getText().toString().isEmpty()) {
            etNombre.setError("Ingrese Nombre");
            ok++;
        } else {
            if (validarLetras(etNombre.getText().toString()) != true) {
                etNombre.setError("Solo Ingrese Letras");
                ok++;
            }
        }
        if (etApellido.getText().toString().isEmpty()) {
            etApellido.setError("Ingrese Nombre");
        } else {
            if (validarLetras(etApellido.getText().toString()) != true) {
                etApellido.setError("Solo Ingrese Letras");
                ok++;
            }
        }
        if (validarEmail() == false) {
            ok++;
        }
        if (etContraseña.getText().toString().isEmpty()) {
            etContraseña.setError("Contraseña Necesaria");
            ok++;
        } else {
            if (contraseñaSegura(etContraseña.getText().toString()) == false) {
                etContraseña.setText("");
                etConfirmar.setText("");
                etContraseña.setError("Requiere como minimo 1 minuscula, 1 mayuscula, 1 digito y un largo de 6 a 12 caracteres ");
                ok++;
            }
        }
        if (etConfirmar.getText().toString().isEmpty()) {
            etConfirmar.setError("Ingrese para Confirmar");
            ok++;
        }

        if (!etConfirmar.getText().toString().equalsIgnoreCase(etContraseña.getText().toString())) {
            ok++;
            etConfirmar.setError("Contraseñas No Coinciden");
            etConfirmar.setText("");
        }

        return ok;
    }

    private boolean validarLetras(String dato) {
        boolean ok = false;

        Pattern regex = Pattern.compile("^[a-zA-Z ]*$");
        Matcher m = regex.matcher(dato);
        boolean as = m.find();
        if (as == true) {
            ok = true;
        }

        return ok;
    }

    private boolean contraseñaSegura(String pass) {
        boolean ok = false;
        Pattern regex = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,12}$");
        Matcher m = regex.matcher(pass);
        boolean as = m.find();
        if (as == true) {
            ok = true;
        }

        return ok;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                //Configuración del mapa de bits en ImageView
                btnImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }
}
