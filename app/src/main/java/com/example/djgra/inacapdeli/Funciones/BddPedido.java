package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BddPedido {
    private static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param pedido   pedido para ser ingresado al web service
     * @param context  Contexto de la actividad
     * @param listener Accion que se desea al capturar la respuesta.
     *                 Metodo que ingresa un pedido al web service.
     */
    public static void setPedido(final Pedido pedido, final Context context, Response.Listener listener, Response.ErrorListener error) {
        final RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlBase + "setPedido", listener, error) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("pedido_fecha_hora", pedido.getFechaPedido());
                map.put("pedido_estado", String.valueOf(pedido.getPedido_estado()));
                map.put("id_cliente", String.valueOf(pedido.getId_cliente()));
                map.put("id_vendedor", String.valueOf(pedido.getId_vendedor()));
                map.put("id_condicion_pedido", String.valueOf(pedido.getId_condicion_pedido()));
                map.put("producto_id", new JSONObject(arrayToMap(pedido.getLstProductoPedido())).toString());
                return map;
            }
        };
        queue.add(jsonRequest);
    }

    /**
     * @param id_cliente id del cliente para traer su pedido.
     * @param context    Contexto de la actividad.
     * @param listener   Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo para traer los pedidos segund su cliente en el web service.
     */
    public static void getPedidoByCliente(final int id_cliente, Context context, Response.Listener<JSONArray> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, urlBase + "getPedidoByCliente?id_cliente=" + id_cliente, null, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("id_cliente", String.valueOf(id_cliente));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public static void updateCondicionPedido(final int pedido_id, final int id_condidcion, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "updateCondicionPedido", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("pedido_id", String.valueOf(pedido_id));
                map.put("id_condicion_pedido", String.valueOf(id_condidcion));

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion que se desea al capturar la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     *                      Metodod para traer los pedidos faltantes
     */
    public static void getPedidoFaltante(Context context, Response.Listener listener, Response.ErrorListener errorListener) {

    }

    /**
     * @param arrayList lista de productos para ser convertida en formato JSON
     * @return Map<String, String>
     * Metodo para transformar un ArrayList en un Map<String, String>
     */
    private static Map<String, Integer> arrayToMap(ArrayList<Producto> arrayList) {
        Map<String, Integer> retorno = new HashMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            retorno.put("id" + i, arrayList.get(i).getCodigo());

        }
        return retorno;
    }


}
