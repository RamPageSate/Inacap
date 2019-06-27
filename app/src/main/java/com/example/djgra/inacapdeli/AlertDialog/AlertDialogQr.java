package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.PrincipalCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AlertDialogQr extends AlertDialog {
    public AlertDialogQr(final Activity context, final Persona cliente) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.codigoqr, null);
        setView(view);
        TextView tvNombre = view.findViewById(R.id.tvNombreQr);
        Button btnListo = view.findViewById(R.id.btnListoQr);
        TextView tvCantidad = view.findViewById(R.id.tvCantidadPedidoQr);
        ImageView imgQr = view.findViewById(R.id.imgQr);
        tvNombre.setText(tvNombre.getText()+ " "+cliente.getNombre()+ " " + cliente.getApellido());
        tvCantidad.setText("");
        imgQr.setImageBitmap(Functions.StringToBitMap(cliente.getCodigoQr()));

        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrincipalCliente.class);
                intent.putExtra("usr",cliente);
                context.startActivity(intent);
            }
        });
    }
}
