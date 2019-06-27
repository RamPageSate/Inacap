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
    private static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void.
     * Metodo para traer todos los vendedores del web service
     */
    public static void getVendedores(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, urlBase + "getVendedor", null, listener, errorListener);
        requestQueue.add(request);
    }

    /**
     * @param persona  Persona para ser insertada
     * @param context  Contexto de la actividad
     * @param listener Accion que se desea al capturar la respuesta.
     * @return int
     * Metodo para insertar una persona en el web service
     */
    public static int setPersona(final Persona persona, final Context context, Response.Listener listener, Response.ErrorListener error) {
        final int[] retorno = {0};
        final RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlBase + "setPersona", listener, error) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("persona_nombre", persona.getNombre());
                map.put("persona_apellido", persona.getApellido());
                map.put("persona_pass", persona.getContrasena());
                map.put("persona_email", persona.getCorreo());
                map.put("persona_foto", String.valueOf(persona.getFoto()));
                map.put("persona_codigo_qr", String.valueOf(persona.getCodigoQr()));
                map.put("id_sede", String.valueOf(persona.getSede()));

                return map;
            }
        };
        queue.add(jsonRequest);

        return retorno[0];
    }

    /**
     * @param email    correo de la persona para traer sus datos
     * @param context  Contexto de la actividad
     * @param listener Accion que se desea al capturar la respuesta.
     * @return void
     */
    public static void getPersona(final String email, Context context, Response.Listener<String> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "getPersona", listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("persona_email", email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param email    email de la persona que se desea cambiar su rol
     * @param rol      rol a ser cambiado de la persona
     * @param estado   estado de la persona que sera cambiada.
     * @param context  Contexto de la actividad
     * @param listener Accion que se desea al capturar la respuesta.
     * @return void
     */
    public static void setVendedor(final String email, final int rol, final int estado, Context context, Response.Listener<String> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, urlBase + "updateVendedor", listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("persona_email", email);
                map.put("id_rol", String.valueOf(rol));
                map.put("persona_estado", String.valueOf(estado));
                return map;
            }
        };
        requestQueue.add(request);
    }

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo que trae las personas segun su email.
     */
    public static void getPersonaEmail(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getPersonaEmail", null, listener, errorListener);
        requestQueue.add(request);
    }

    /**
     * @param persona       persona que sera cambiada.
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo para cambiar los datos de una persona
     */
    public static void updatePersona(final Persona persona, final Context context, Response.Listener listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "updatePersona", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("persona_id", String.valueOf(persona.getCodigo()));
                map.put("persona_nombre", persona.getNombre());
                map.put("persona_apellido", persona.getApellido());
                map.put("persona_pass", persona.getContrasena());
                map.put("persona_saldo", String.valueOf(persona.getSaldo()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
