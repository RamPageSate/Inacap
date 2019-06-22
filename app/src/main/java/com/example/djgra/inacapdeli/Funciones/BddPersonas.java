package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Persona;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class BddPersonas {
    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    public static void getVendedores(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,urlBase+"getVendedor",null, listener, errorListener);
        requestQueue.add(request);
    }
    public static int setPersona(final Persona persona, final Context context, Response.Listener listener, Response.ErrorListener error) {
        final int[] retorno = {0};
        final RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlBase+"setPersona",listener,error)
                {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("persona_nombre", persona.getNombre());
                map.put("persona_apellido", persona.getApellido());
                map.put("persona_pass", persona.getContrasena());
                map.put("persona_email", persona.getCorreo());
                map.put("persona_foto",String.valueOf(persona.getFoto()));
                map.put("persona_codigo_qr",String.valueOf(persona.getCodigoQr()));
                map.put("id_sede", String.valueOf(persona.getSede()));

                return map;
            }
        };
        queue.add(jsonRequest);

        return retorno[0];
    }
    public static void getPersona(final String email,Context context, Response.Listener<String> listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlBase+"getPersona",listener,null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                map.clear();
                map = new HashMap<String,String>();
                map.put("persona_email",email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public static void setVendedor(final String email, final int rol,final int estado, Context context, Response.Listener<String> listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST,urlBase+"updateVendedor", listener, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                map.clear();
                map = new HashMap<String,String>();
                map.put("persona_email",email);
                map.put("id_rol",String.valueOf(rol));
                map.put("persona_estado",String.valueOf(estado));
                return map;
            }
        };
        requestQueue.add(request);
    }

    public static void getPersonaEmail(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getPersonaEmail", null, listener, errorListener);
        requestQueue.add(request);
    }

}
