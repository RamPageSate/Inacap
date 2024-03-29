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
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Producto_Favorito;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BddProductos {

    private static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void.
     * Metodo para traer todos los productos
     */

    public static void getProducto(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST, //GET or POST
                urlBase + "getProducto", //URL
                null, listener, errorListener//Parameters
        );
        requestQueue.add(request);
    }

    /**
     * @param pedido_id     id del pedido para traer sus productos
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     *                      Metodo que trae los productos segun su pedido
     */
    public static void getProductoByPedido(final int pedido_id, Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getProductoByPedido?pedido_id=" + pedido_id, null, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("pedido_id", String.valueOf(pedido_id));
                return map;
            }
        };
        requestQueue.add(request);
    }

    /**
     * @param producto      Producto que se desea cambiar
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     */
    public static void updateProducto(final Producto producto, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setProducto", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("producto_id", String.valueOf(producto.getCodigo()));
                map.put("producto_nombre", producto.getNombre());
                map.put("producto_precio", String.valueOf(producto.getPrecio()));
                map.put("producto_foto", producto.getFoto());
                map.put("producto_descripcion", producto.getDescripcion());
                map.put("producto_sku", producto.getSku());
                map.put("id_fabricante", String.valueOf(producto.getId_fabricante()));
                map.put("id_tipo", String.valueOf(producto.getId_tipo()));
                map.put("categoria_id", new JSONObject(arrayToMap(producto.getLstCategoriasProducto())).toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    /**
     * @param producto      Producto para ser insertado
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo que inserta un producto en el web service
     */
    public static void setProducto(final Producto producto, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setProducto", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("producto_nombre", producto.getNombre());
                map.put("producto_precio", String.valueOf(producto.getPrecio()));
                map.put("producto_foto", producto.getFoto());
                map.put("producto_descripcion", producto.getDescripcion());
                map.put("producto_sku", producto.getSku());
                map.put("id_fabricante", String.valueOf(producto.getId_fabricante()));
                map.put("id_tipo", String.valueOf(producto.getId_tipo()));
                map.put("categoria_id", new JSONObject(arrayToMap(producto.getLstCategoriasProducto())).toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param arrayList Lista de categorias para ser convertida
     * @return Map
     * Metodo para convertir un ArrayList en una Map
     */

    private static Map<String, Integer> arrayToMap(ArrayList<Categoria> arrayList) {
        Map<String, Integer> retorno = new HashMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            retorno.put("id" + i, arrayList.get(i).getCodigo());
        }
        return retorno;
    }

    /**
     * @param producto      producto que quieres que sea el favorito.
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo para colocar un producto como favorito
     */
    public static void setProductoFavorito(final Producto_Favorito producto, Context context, Response.Listener listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "setProductoFavorito", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("id_cliente", String.valueOf(producto.getId_cliente()));
                map.put("id_producto", String.valueOf(producto.getId_producto()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void deleteProductoFavorito(final Producto_Favorito producto, Context context, Response.Listener listener, Response.ErrorListener errorListener){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase + "deleteProductoFavorito", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("id_cliente", String.valueOf(producto.getId_cliente()));
                map.put("id_producto", String.valueOf(producto.getId_producto()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param id_cliente    id del cliente para traer sus productos favoritos
     * @param context       Contexto de la actividad
     * @param errorListener Accion en caso de error que se desea al capturar  la respuesta
     * @param listener      Accion que se desea al capturar la respuesta.
     * @return void
     * Metodo para traer todos los productos favoritos segun el cliente.
     */
    public static void getProductoFavorito(final int id_cliente, Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBase + "getProductoFavorito?id_cliente=" + id_cliente, null, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map = new HashMap<String, String>();
                map.put("id_cliente", String.valueOf(id_cliente));
                return map;
            }
        };
        requestQueue.add(request);
    }

}
