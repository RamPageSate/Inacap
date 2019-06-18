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
    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    public static void getFabricantes(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,urlBase+"getFabricante",null, listener, errorListener);
        requestQueue.add(request);
    }

    public static void setFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"setFabricante",listener,null){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                map.clear();
                map= new HashMap<String,String>();
                map.put("fabricante_nombre",fabricante.getNombre().toUpperCase());
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public static void updateFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"updateFabricante",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                map.clear();
                map= new HashMap<String,String>();
                map.put("fabricante_id",String.valueOf(fabricante.getCodigo()));
                map.put("fabricante_nombre",fabricante.getNombre());
                map.put("fabrincante_estado", String.valueOf(fabricante.getEstado()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public static void deleteFabricante(final Fabricante fabricante, Context context, Response.Listener<String> listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"deleteFabricante",listener,null){
            @Override
            protected  Map<String, String> getParams() throws  AuthFailureError{
                map.clear();
                map = new HashMap<String,String>();
                map.put("sede_id",String.valueOf(fabricante.getCodigo()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
