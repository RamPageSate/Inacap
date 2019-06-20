package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorSede;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.BddSede;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class AlertDialogSede extends AlertDialog {
    private int posicionUdpdateDelete;

    public AlertDialogSede(final Activity context, final ArrayList<Sede> lstSede) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.addcategoria, null);
        setView(view);

        final ListView lstvSede = view.findViewById(R.id.lstView);

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        tvTitulo.setText("Sede");
        final EditText etNombre = view.findViewById(R.id.etNombreView);
        etNombre.setHint("Agregar nueva sede");
        final ImageButton btnGuardar = view.findViewById(R.id.btnGuardarView);
        final AdaptadorSede adapter = new AdaptadorSede(lstSede, context, etNombre, btnGuardar);
        //SI ESTA VACIA  NO CARGA
        if (!lstSede.isEmpty()) {
            lstvSede.setAdapter(adapter);
        }


        ImageButton btnSalir = view.findViewById(R.id.btnSalirView);
        lstvSede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                etNombre.setText(lstSede.get(position).getDireccion());

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnGuardar.getDrawable().getConstantState().equals(view.getResources().getDrawable(R.drawable.plusblue).getConstantState())) {
                    if (!etNombre.getText().toString().isEmpty()) {
                        final ProgressDialog progressDialog = Functions.CargarDatos("Agregando Fabricante", context);
                        final Categoria nuevaCategoria = new Categoria();
                        nuevaCategoria.setNombre(etNombre.getText().toString().toUpperCase());
                        BddCategoria.setCategoria(nuevaCategoria, context, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                BddCategoria.getCategoria(context, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        lstSede.removeAll(lstSede);
                                        for (int x = 0; x < response.length(); ++x) {
                                            try {
                                                Sede sede = new Sede();
                                                sede.setCodigo(response.getJSONObject(x).getInt("categoria_id"));
                                                sede.setDireccion(response.getJSONObject(x).getString("categoria_nombre"));
                                                sede.setEstado(response.getJSONObject(x).getInt("categoria_estado"));
                                                lstSede.add(sede);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Collections.reverse(lstSede);
                                        lstvSede.setAdapter(adapter);
                                        lstvSede.deferNotifyDataSetChanged();
                                        progressDialog.hide();
                                        etNombre.setText("");
                                        Toast.makeText(context, "Agregado", Toast.LENGTH_SHORT).show();
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
                                    }
                                }, Functions.FalloInternet(context, progressDialog, "no Pudo Cargar"));
                            }
                        }, Functions.FalloInternet(context, progressDialog, "Fallo al agregar una categoria"));
                    } else {
                        etNombre.setError("Ingrese Categoria");
                    }
                } else {
                    //Actualizo ArrayList y el Listview

                    int codigo = lstSede.get(posicionUdpdateDelete).getCodigo();
                    String nuevoNombre = etNombre.getText().toString().toUpperCase();
                    final Sede[] sede = {new Sede()};
                    sede[0].setCodigo(codigo);
                    sede[0].setDireccion(nuevoNombre);
                    sede[0].setEstado(lstSede.get(posicionUdpdateDelete).getEstado());
                    lstSede.get(posicionUdpdateDelete).setDireccion(nuevoNombre);
                    lstvSede.setAdapter(adapter);
                    lstvSede.deferNotifyDataSetChanged();
                    etNombre.setText("");
                    final ProgressDialog progressDialog = Functions.CargarDatos("Actualizando", context);

                    //Actualizo BD
                    BddSede.updateSede(sede[0], context, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            Log.d("TAG_", "actualizo fabricante");
                            btnGuardar.setImageResource(R.drawable.plusblue);
                            sede[0] = new Sede();
                            etNombre.setText("");
                        }
                    }, Functions.FalloInternet(context, progressDialog, ""));

                }
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lstSede.removeAll(lstSede);


                dismiss();

            }
        });

    }


}
