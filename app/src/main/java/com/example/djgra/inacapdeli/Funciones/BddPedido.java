package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Pedido;

import java.util.HashMap;
import java.util.Map;

public class BddPedido {
    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";
    //pedido_fecha_hora pedido_estado id_cliente id_vendedor id_condicion_pedido
    //trans transaccion_id trans_fecha_hora transaccion_monto transaccion_estado id_pedido
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
                map.put("id_vendedor",String.valueOf(pedido.getId_vendedor()));
                map.put("id_condicion_pedido", String.valueOf(pedido.getId_condicion_pedido()));
                return map;
            }
        };
        queue.add(jsonRequest);
    }




}
