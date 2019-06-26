package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Categoria;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class BddCategoria {
    private static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param categoria     Categoria que se quiere insertar.
     * @param context       Contexto de la actividad.
     * @param errorListener Accion que se desea al capturar la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * <p>
     * Metodo para setear las categorias por el webService
     */
    public static void setCategoria(final Categoria categoria, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setCategoria", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("categoria_nombre", categoria.getNombre());
                map.put("categoria_estado", String.valueOf(categoria.getEstado()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param categoria     categoria que se quiere modificar.
     * @param context       Contexto de la actividad.
     * @param errorListener Accion que se desea al capturar la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * <p>
     * Metodo para actualizar una categoria.
     */
    public static void updateCategoria(final Categoria categoria, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "updateCategoria", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("categoria_id", String.valueOf(categoria.getCodigo()));
                map.put("categoria_nombre", categoria.getNombre().toUpperCase());
                map.put("categoria_estado", String.valueOf(categoria.getEstado()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param categoria Categoria para eliminar
     * @param context   Contexto de la actividad
     * @param listener  Accion que se desea al capturar la respuesta.
     * @return void
     * metodo para eliminar un categoria en el web service
     */
    public static void deleteCategoria(final Categoria categoria, Context context, Response.Listener<String> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "deleteCategoria", listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("categoria_id", String.valueOf(categoria.getCodigo()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion que se desea al capturar la respuesta.
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo que trae todas las categorias del web service
     */
    public static void getCategoria(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST, //GET or POST
                urlBase + "getCategoria", //URL
                null, listener, errorListener//Parameters
        );
        requestQueue.add(request);

    }

    /**
     * @param codigoProducto Codigo para traer una categoria en especifico
     * @param context        Contexto de la actividad
     * @param errorListener  Accion que se desea al capturar la respuesta
     * @param listener       Accion que se desea al capturar la respuesta.
     */
    public static void getCategoriaByProducto(final int codigoProducto, Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getCategoriaByProducto?producto_id=" + codigoProducto, null, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("producto_id", String.valueOf(codigoProducto));
                return map;
            }
        };
        requestQueue.add(request);
    }
}

