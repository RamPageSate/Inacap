package com.example.djgra.inacapdeli.Funciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Tipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class BddTipo {
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";
    public static ArrayList<Tipo> lstTipo = new ArrayList<>();
    private static Map<String,String> map = new HashMap<>();

    public static void getTipo(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,urlBase+"getTipo",null,listener,errorListener);
        requestQueue.add(request);
    }

    public static void setTipo(final Tipo tipo, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"setTipo",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                map.clear();
                map= new HashMap<String,String>();
                map.put("tipo",tipo.getNombre().toUpperCase());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }





}