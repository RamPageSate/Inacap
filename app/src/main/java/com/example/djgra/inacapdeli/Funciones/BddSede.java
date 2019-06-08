package com.example.djgra.inacapdeli.Funciones;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Sede;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BddSede {
    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";
    public static void setSede(final Sede sede, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"setSede",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                map.clear();
                map= new HashMap<String,String>();
                map.put("sede_direccion",sede.getDireccion().toUpperCase());
                map.put("sede_estado",String.valueOf(sede.getEstado()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public static void getSede(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,urlBase+"getSede",null,listener,errorListener);
        requestQueue.add(request);
    }
    public static void updateSede(final Sede sede, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase+"updateSede",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                map.clear();
                map=new HashMap<String, String>();
                map.put("sede_id",String.valueOf(sede.getCodigo()));
                map.put("sede_direccion",sede.getDireccion().toUpperCase());
                map.put("sede_estado",String.valueOf(sede.getEstado()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public static void deleteSede(final Sede sede, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"deleteSede",listener,errorListener){
            @Override
            protected  Map<String, String> getParams() throws  AuthFailureError{
                map.clear();
                map = new HashMap<String,String>();
                map.put("sede_id",String.valueOf(sede.getCodigo()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}

