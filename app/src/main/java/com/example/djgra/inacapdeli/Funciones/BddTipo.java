package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Tipo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BddTipo {
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";
    public static ArrayList<Tipo> lstTipo = new ArrayList<>();
    private static Map<String, String> map = new HashMap<>();

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo que trae todos los tipos del web service.
     */
    public static void getTipo(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getTipo", null, listener, errorListener);
        requestQueue.add(request);
    }

    public static void setTipo(final Tipo tipo, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setTipo", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("tipo", tipo.getNombre().toUpperCase());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void updateTipo(final Tipo tipo, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "updateTipo", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("tipo_id", String.valueOf(tipo.getId()));
                map.put("tipo", tipo.getNombre());
                if (tipo.getEstado() == 1) {
                    map.put("tipo_estado", String.valueOf(1));
                } else {
                    map.put("tipo_estado", String.valueOf(0));

                }
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


}
