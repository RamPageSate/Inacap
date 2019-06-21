package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class AlertDialogVendedores extends AlertDialog {
    private int posicionUdpdateDelete;

    public AlertDialogVendedores(final Activity context, final ArrayList<Persona> lstPersona, final ArrayList<String> lstEmail) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.lstvendedores, null);
        setView(view);
        final AutoCompleteTextView acVendedor = view.findViewById(R.id.acVendedores);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lstEmail);
        acVendedor.setAdapter(adapter);
        acVendedor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(acVendedor.getWindowToken(), 0);
            }
        });
        ImageButton imgPlus = view.findViewById(R.id.btnGuardarVendedor);
        ListView lsvVendedores = view.findViewById(R.id.lstVendedores);
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acVendedor.getText().toString().isEmpty()) {
                    acVendedor.setError("Debe agregar un email");
                } else {
                    final ProgressDialog progressDialog = Functions.CargarDatos(context.getString(R.string.alert_update), context);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    BddPersonas.setVendedor(acVendedor.getText().toString(), 1, 1, context, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            BddPersonas.getVendedores(context, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                }
                            }, Functions.FalloInternet(context, progressDialog, "No se pudo conectar"));
                        }
                    });
                }

            }
        });
        ImageButton imgBack = view.findViewById(R.id.btnSalirVendedor);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstPersona.remove(lstPersona);
                lstEmail.remove(lstEmail);
                dismiss();
            }
        });

    }
}
