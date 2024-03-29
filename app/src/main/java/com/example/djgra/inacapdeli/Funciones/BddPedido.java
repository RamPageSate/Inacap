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
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BddPedido {
    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    public static void setPedido(final Pedido pedido, final Context context, Response.Listener listener, Response.ErrorListener error) {
        final RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlBase+"setPedido",listener,error)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("pedido_fecha_hora", pedido.getFechaPedido());
                map.put("pedido_estado", String.valueOf(pedido.getPedido_estado()));
                map.put("id_cliente", String.valueOf(pedido.getId_cliente()));
                map.put("producto_id", new JSONObject(arrayToMap(pedido.getLstProductoPedido())).toString());
                return map;
            }
        };
        queue.add(jsonRequest);
    }


    public static void getPedidoByCliente(final int id_cliente,Context context, Response.Listener<JSONArray> listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST,urlBase+"getPedidoByCliente?id_cliente="+id_cliente,null, listener,null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                map.clear();
                map = new HashMap<String,String>();
                map.put("id_cliente",String.valueOf(id_cliente));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public static void updateCondicionPedido(final Pedido pedido, final int id_condidcion, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase+"updateCondicionPedido",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                map.clear();
                map=new HashMap<String, String>();
                map.put("pedido_id", String.valueOf(pedido.getCodigo()));
                map.put("id_vendedor",String.valueOf(pedido.getId_vendedor()));
                map.put("id_condicion_pedido", String.valueOf(id_condidcion));

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public static void getPedidoFaltante(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,urlBase+"getPedidoFaltante", null, listener, errorListener);
            requestQueue.add(request);

    }

    public static void getPedidoListo(final Persona vendedor, Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,urlBase+"getPedidoListo?id_vendedor="+ vendedor.getCodigo(), null, listener, errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                map.clear();
                map=new HashMap<String, String>();
                map.put("id_vendedor", String.valueOf(vendedor.getCodigo()));
                return map;
            }
        };


        requestQueue.add(request);

    }



    private static Map<String, Integer> arrayToMap(ArrayList<Producto> arrayList) {
        Map<String, Integer> retorno = new HashMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            retorno.put("id" + i, arrayList.get(i).getCodigo());
        }
        return retorno;
    }


}
