package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorCategoria;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;
import com.example.djgra.inacapdeli.Registrar_Persona;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class AlertDialogActualizarPerfil extends AlertDialog {
    private int posicionUdpdateDelete;

    public AlertDialogActualizarPerfil(final Activity context, final String dato, final Persona persona, final EditText editText, final Button btnActualizar) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.viewactualizarperfil, null);
        setView(view);
        TextView tvTitulo = view.findViewById(R.id.tvTituloActualizarPerfil);
        final EditText etPrincipal = view.findViewById(R.id.etPrincipalActualizarPerfil);
        final Button btnGuardar = view.findViewById(R.id.btnGuardarActualizarPerfil);
        final ImageView checkVerde = view.findViewById(R.id.checkVerde);
        ImageButton btnSalir = view.findViewById(R.id.btnsalirActualizarPerfil);
        switch (dato) {
            case "NOMBRE":
                etPrincipal.setHint(persona.getNombre());
                tvTitulo.setText("Cambiar mi Nombre");
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etPrincipal.getText().toString().trim().isEmpty()) {
                            etPrincipal.setError("Ingrese  Nombre");
                        } else {
                            if (Functions.validarLetras(etPrincipal.getText().toString()) == true) {
                                persona.setNombre(etPrincipal.getText().toString().trim());
                                editText.setText(persona.getNombre());
                                checkVerde.setVisibility(View.VISIBLE);
                                btnActualizar.setEnabled(true);
                                btnActualizar.setBackgroundColor(Color.parseColor("#FF0000"));
                                Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show();
                            } else {
                                etPrincipal.setText("");
                                etPrincipal.setError("Solo Letras");
                                checkVerde.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
                break;
            case "APELLIDO":
                etPrincipal.setHint(persona.getApellido());
                tvTitulo.setText("Cambiar mi Apellido");
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etPrincipal.getText().toString().trim().isEmpty()) {
                            etPrincipal.setError("Ingrese  Apellido");
                        } else {
                            if (Functions.validarLetras(etPrincipal.getText().toString()) == true) {
                                persona.setApellido(etPrincipal.getText().toString().trim());
                                editText.setText(persona.getApellido());
                                checkVerde.setVisibility(View.VISIBLE);
                                btnActualizar.setEnabled(true);
                                btnActualizar.setBackgroundColor(Color.parseColor("#FF0000"));
                                Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show();
                            } else {
                                etPrincipal.setText("");
                                etPrincipal.setError("Solo Letras");
                                checkVerde.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
                break;
            case "PASS":
                etPrincipal.setTransformationMethod(PasswordTransformationMethod.getInstance());
                final EditText etNuevaContraseña = view.findViewById(R.id.etNuevaPassActualizarPerfil);
                final TextView tvRequerimiento = view.findViewById(R.id.tvRequerimientoPass);
                etPrincipal.setHint("Ingrese Contraseña");
                btnGuardar.setEnabled(false);
                tvTitulo.setText("Cambiar mi Contraseña");
                btnGuardar.setBackgroundColor(Color.parseColor("#808080"));
                etPrincipal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (etPrincipal.getText().toString().equals(persona.getContrasena())) {
                            etNuevaContraseña.setVisibility(View.VISIBLE);
                            checkVerde.setVisibility(View.VISIBLE);
                            btnGuardar.setBackgroundColor(Color.parseColor("#FF0000"));
                            btnGuardar.setEnabled(true);
                            tvRequerimiento.setVisibility(View.VISIBLE);
                            etPrincipal.setEnabled(false);
                        }else{
                            etPrincipal.setError("Contraseña Incorrecta");
                        }
                        return false;
                    }
                });
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Functions.contraseñaSegura(etNuevaContraseña.getText().toString()) == true) {
                            persona.setContrasena(etNuevaContraseña.getText().toString().trim());
                            editText.setText(persona.getContrasena());
                            etPrincipal.setText("");
                            etNuevaContraseña.setText("");
                            checkVerde.setVisibility(View.INVISIBLE);
                            tvRequerimiento.setVisibility(View.INVISIBLE);
                            btnActualizar.setEnabled(true);
                            btnActualizar.setBackgroundColor(Color.parseColor("#FF0000"));
                            etNuevaContraseña.setVisibility(View.INVISIBLE);
                            btnGuardar.setBackgroundColor(Color.parseColor("#808080"));
                            btnGuardar.setEnabled(false);
                            Toast.makeText(context, "Contraseña Cambiada", Toast.LENGTH_SHORT).show();
                        } else {
                            etNuevaContraseña.setText("");
                            etNuevaContraseña.setError("Contraseña Insegura");
                            checkVerde.setVisibility(View.INVISIBLE);
                        }

                    }
                });
                break;
            default:

        }
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
}
