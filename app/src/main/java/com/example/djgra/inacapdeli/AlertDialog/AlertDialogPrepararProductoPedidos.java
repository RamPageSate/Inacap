package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorProductosPorPreparar;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AlertDialogPrepararProductoPedidos extends AlertDialog {


    public AlertDialogPrepararProductoPedidos(final Activity context, ArrayList<Producto> lstProducto, final int codigoPedido) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.preparar_pedido_producto, null);
        setView(view);
        ListView lstvProductos = view.findViewById(R.id.lstProductoPreparando);
        Button btnTerminar = view.findViewById(R.id.btnTerminarPrepararPedido);
        Button btnCancelar = view.findViewById(R.id.btnCancelarPrepararPedido);
        btnTerminar.setEnabled(false);
        AdaptadorProductosPorPreparar adpatador = new AdaptadorProductosPorPreparar(lstProducto,btnTerminar,context);
        lstvProductos.setAdapter(adpatador);
        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BddPedido.updateCondicionPedido(codigoPedido, 3, context, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Cambio ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },null);
            }
        });
    }
}
