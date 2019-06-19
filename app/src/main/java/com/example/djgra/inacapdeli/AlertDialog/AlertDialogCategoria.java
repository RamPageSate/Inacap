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
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorCategoria;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Funciones.BddCategoria;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class AlertDialogCategoria extends AlertDialog {
    private int posicionUdpdateDelete;

    public AlertDialogCategoria(final Activity context, final ArrayList<Categoria> lstCategorias) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.addcategoria, null);
        setView(view);

        final ListView lstvCategoria = view.findViewById(R.id.lstView);

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        tvTitulo.setText("Categoria");
        final EditText etNombre = view.findViewById(R.id.etNombreView);
        etNombre.setHint("Agregar nueva categoria");
        final ImageButton btnGuardar = view.findViewById(R.id.btnGuardarView);
        final AdaptadorCategoria adapter = new AdaptadorCategoria(lstCategorias, context, etNombre, btnGuardar);
        //SI ESTA VACIA  NO CARGA
        if (!lstCategorias.isEmpty()) {
            lstvCategoria.setAdapter(adapter);
        }


        ImageButton btnSalir = view.findViewById(R.id.btnSalirView);
        lstvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                etNombre.setText(lstCategorias.get(position).getNombre());

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
                                        lstCategorias.removeAll(lstCategorias);
                                        for (int x = 0; x < response.length(); ++x) {
                                            try {
                                                Categoria categoria = new Categoria();
                                                categoria.setCodigo(response.getJSONObject(x).getInt("categoria_id"));
                                                categoria.setNombre(response.getJSONObject(x).getString("categoria_nombre"));
                                                lstCategorias.add(categoria);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Collections.reverse(lstCategorias);
                                        lstvCategoria.setAdapter(adapter);
                                        lstvCategoria.deferNotifyDataSetChanged();
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
                        etNombre.setError("Ingrese Fabricante");
                    }
                } else {
                    //Actualizo ArrayList y el Listview
                    int codigo = lstCategorias.get(posicionUdpdateDelete).getCodigo();
                    String nuevoNombre = etNombre.getText().toString().toUpperCase();
                    final Categoria[] categoria = {new Categoria()};
                    categoria[0].setCodigo(codigo);
                    categoria[0].setNombre(nuevoNombre);
                    lstCategorias.get(posicionUdpdateDelete).setNombre(nuevoNombre);
                    lstvCategoria.setAdapter(adapter);
                    lstvCategoria.deferNotifyDataSetChanged();
                    etNombre.setText("");
                    final ProgressDialog progressDialog = Functions.CargarDatos("Actualizando", context);
                    Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show();
                    //Actualizo BD
                    BddCategoria.updateCategoria(categoria[0], context, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            Log.d("TAG_", "actualizo fabricante");
                            btnGuardar.setImageResource(R.drawable.plusblue);
                            categoria[0] = new Categoria();
                            etNombre.setText("");
                        }
                    }, Functions.FalloInternet(context, progressDialog, ""));
                }
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lstCategorias.removeAll(lstCategorias);


                dismiss();

            }
        });


    }


}
