package com.example.djgra.inacapdeli.AlertDialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Fabricante;
import com.example.djgra.inacapdeli.Funciones.BddFabricante;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AlertDialogFabricantes extends AlertDialog {

    private View content;
    private int posicionUdpdateDelete;
    private int btn = 0;

    public AlertDialogFabricantes(final Context context, final ArrayList<Fabricante> lstFabricantes) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.addcategoria, null);
        setView(view);
        setContentView(R.layout.addcategoria);
        final ListView lstvFabricante = findViewById(R.id.lstView);
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText("Fabricantes");
        final EditText etNombre = findViewById(R.id.etNombreView);
        etNombre.setHint("Fabrincate");
        final ArrayAdapter<Fabricante> adapter = new ArrayAdapter<Fabricante>(context, android.R.layout.simple_expandable_list_item_1, lstFabricantes);
        if (!lstFabricantes.isEmpty()) {
            lstvFabricante.setAdapter(adapter);
        }
        final ImageButton btnGuardar = findViewById(R.id.btnGuardarView);

        ImageButton btnSalir = findViewById(R.id.btnSalirView);
        lstvFabricante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                etNombre.setText(lstFabricantes.get(position).getNombre());

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == 1) {
                    if (!etNombre.getText().toString().isEmpty()) {
                        final ProgressDialog progressDialog = Functions.CargarDatos("Agregando Fabricante", context);
                        final Fabricante fabricante = new Fabricante();
                        fabricante.setNombre(etNombre.getText().toString().toUpperCase());
                        BddFabricante.setFabricante(fabricante, context, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                BddFabricante.getFabricantes(context, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        lstFabricantes.removeAll(lstFabricantes);
                                        for (int x = 0; x < response.length(); ++x) {
                                            try {
                                                Fabricante fabricante = new Fabricante();
                                                fabricante.setCodigo(response.getJSONObject(x).getInt("fabricante_id"));
                                                fabricante.setNombre(response.getJSONObject(x).getString("fabricante_nombre"));
                                                lstFabricantes.add(fabricante);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        lstFabricantes.add(fabricante);
                                        lstvFabricante.setAdapter(adapter);
                                        lstvFabricante.deferNotifyDataSetChanged();
                                        progressDialog.hide();
                                        etNombre.setText("");
                                        Toast.makeText(context, "Agregado", Toast.LENGTH_SHORT).show();
                                    }
                                }, Functions.FalloInternet(context, progressDialog, "no Pudo Cargar"));
                            }
                        });
                    } else {
                        etNombre.setError("Ingrese Fabricante");
                    }
                } else {
                    //Actualizo ArrayList y el Listview
                    int codigo = lstFabricantes.get(posicionUdpdateDelete).getCodigo();
                    String nuevoNombre = etNombre.getText().toString().toUpperCase();
                    Fabricante fabricante = new Fabricante();
                    fabricante.setCodigo(codigo);
                    fabricante.setNombre(nuevoNombre);
                    lstFabricantes.get(posicionUdpdateDelete).setNombre(nuevoNombre);
                    lstvFabricante.setAdapter(adapter);
                    lstvFabricante.deferNotifyDataSetChanged();
                    etNombre.setText("");
                    final ProgressDialog progressDialog = Functions.CargarDatos("Actualizando", context);
                    Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show();
                    //Actualizo BD
                    BddFabricante.updateFabricante(fabricante, context, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            Log.d("TAG_", "actualizo fabricante");
                        }
                    }, Functions.FalloInternet(context, progressDialog, ""));
                    btn = 0;
                }
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstFabricantes.removeAll(lstFabricantes);
                dismiss();
            }
        });


    }


}
