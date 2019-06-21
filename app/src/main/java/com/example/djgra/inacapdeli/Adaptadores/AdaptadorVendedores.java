package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.BddPersonas;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AdaptadorVendedores extends BaseAdapter {
    private Activity context;
    private ArrayList<Persona> lista;

    public AdaptadorVendedores(Activity context, ArrayList<Persona> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getCodigo();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View item = inflater.inflate(R.layout.listviewvendedores, null);
        ImageView foto = item.findViewById(R.id.imgLstVendedor);
        TextView nombre = item.findViewById(R.id.tvNombreVendedor);
        TextView correo = item.findViewById(R.id.tvEmailVendedor);
        ImageButton btnQuitar = item.findViewById(R.id.btnQuitarVendedor);
        final Switch swEstado = item.findViewById(R.id.swEstadoVendedor);

        foto.setImageBitmap(Functions.StringToBitMap(lista.get(position).getFoto()));
        nombre.setText(lista.get(position).getNombre() + " " + lista.get(position).getApellido());
        correo.setText(lista.get(position).getCorreo());
        if (lista.get(position).getEstado() == 1) {
            swEstado.setChecked(true);
        } else {
            swEstado.setChecked(false);
        }

        btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = Functions.CargarDatos(context.getString(R.string.alert_update), context);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                BddPersonas.setVendedor(lista.get(position).getCorreo(), 1, 1, context, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        BddPersonas.getVendedores(context, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressDialog.dismiss();
                                lista.clear();
                                if (!response.toString().equals("[]")) {

                                    for (int x = 0; x < response.length(); ++x) {

                                        try {
                                            int codigo = response.getJSONObject(x).getInt("persona_id");
                                            String nombre = response.getJSONObject(x).getString("persona_nombre");
                                            String apellido = response.getJSONObject(x).getString("persona_apellido");
                                            String email = response.getJSONObject(x).getString("persona_email");
                                            int estado = response.getJSONObject(x).getInt("persona_estado");
                                            String foto = response.getJSONObject(x).getString("persona_foto");
                                            int rol = response.getJSONObject(x).getInt("id_rol");
                                            int sede = response.getJSONObject(x).getInt("id_sede");
                                            Persona vendedor = new Persona();
                                            vendedor.setCodigo(codigo);
                                            vendedor.setNombre(nombre);
                                            vendedor.setFoto(foto);
                                            vendedor.setApellido(apellido);
                                            vendedor.setCorreo(email);
                                            vendedor.setEstado(estado);
                                            vendedor.setRol(rol);
                                            vendedor.setSede(sede);
                                            lista.add(vendedor);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                                notifyDataSetChanged();
                            }
                        }, Functions.FalloInternet(context, progressDialog, "No se pudo conectar"));
                    }
                });
            }
        });
        swEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int estado = -1;
                if (isChecked) {
                    estado = 1;
                } else {
                    estado = 0;
                }
                final int finalEstado = estado;
                BddPersonas.setVendedor(lista.get(position).getCorreo(), 2, estado, context, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lista.get(position).setEstado(finalEstado);
                        AdaptadorVendedores.super.notifyDataSetChanged();
                    }
                });

            }
        });

        return item;
    }
}
