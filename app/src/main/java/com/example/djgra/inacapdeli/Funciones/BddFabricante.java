package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Fabricante;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class BddFabricante {
    private static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion que se desea al capturar la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * metodo que trae todos los productos de la base de datos.
     */
    public static void getFabricantes(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, urlBase + "getFabricante", null, listener, errorListener);
        requestQueue.add(request);
    }

    /**
     * @param fabricante Fabricante para ser ingresado a la base de datos
     * @param context    Contexto de la actividad
     * @param listener   Accion que se desea al capturar la respuesta.
     * @return void
     */
    public static void setFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setFabricante", listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("fabricante_nombre", fabricante.getNombre().toUpperCase());
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    /**
     * @param fabricante    fabricante para ser cambiado en el web service
     * @param context       Contexto de la actividad
     * @param errorListener Accion que se desea al capturar la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Medotodo para cambiar un fabricante en una base de datos.
     */
    public static void updateFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "updateFabricante", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("fabricante_id", String.valueOf(fabricante.getCodigo()));
                map.put("fabricante_nombre", fabricante.getNombre());
                if (fabricante.getEstado() == 1) {
                    map.put("fabricante_estado", String.valueOf(1));
                } else {
                    map.put("fabricante_estado", String.valueOf(0));

                }
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param fabricante Fabricante para ser eliminado en la base de datos.
     * @param context    Contexto de la actividad
     * @param listener   Accion que se desea al capturar la respuesta.
     * @return void.
     */
    public static void deleteFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "deleteFabricante", listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("sede_id", String.valueOf(fabricante.getCodigo()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
