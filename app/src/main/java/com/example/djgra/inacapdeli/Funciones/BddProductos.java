package com.example.djgra.inacapdeli.Funciones;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.djgra.inacapdeli.Clases.Producto;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class BddProductos {

    private static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";




    public static void getProducto(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST, //GET or POST
                urlBase+"getProducto", //URL
                null, listener, errorListener//Parameters
        );
        requestQueue.add(request);

    }

    public static void updateProducto(final Producto producto, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase+"updateCategoria",listener,errorListener){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                map.clear();
                map=new HashMap<String, String>();
                map.put("producto_nombre",producto.getNombre());
                map.put("producto_descripcion",producto.getDescripcion());
                map.put("producto_foto",producto.getFoto());
                //map.put("producto_stock", String.valueOf(producto.getStock()));
                map.put("producto_precio",String.valueOf(producto.getPrecio()));
                map.put("producto_sku",producto.getSku());
                //map.put("producto_estado",String.valueOf(producto.getEstado()));
                map.put("id_fabricante",String.valueOf(producto.getId_fabricante()));
                map.put("id_tipo",String.valueOf(producto.getId_tipo()));
                map.put("categoria_id",String.valueOf(producto.getId_categoria()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void setProducto(final Producto producto, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase+"setProducto",listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("producto_nombre",producto.getNombre());
                map.put("producto_descripcion",producto.getDescripcion());
                map.put("producto_foto",producto.getFoto());
                //map.put("producto_stock", String.valueOf(producto.getStock()));
                map.put("producto_precio",String.valueOf(producto.getPrecio()));
                map.put("producto_sku",producto.getSku());
                //map.put("producto_estado",String.valueOf(producto.getEstado()));
                map.put("id_fabricante",String.valueOf(producto.getId_fabricante()));
                map.put("id_tipo",String.valueOf(producto.getId_tipo()));
                map.put("categoria_id",String.valueOf(producto.getId_categoria()));

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


}