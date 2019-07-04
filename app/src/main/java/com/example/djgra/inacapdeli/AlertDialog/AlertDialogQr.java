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
import com.example.djgra.inacapdeli.Login;
import com.example.djgra.inacapdeli.PrincipalCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AlertDialogQr extends AlertDialog {
    public AlertDialogQr(final Activity context, final Persona cliente, final String accion) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.codigoqr, null);
        setView(view);
        TextView tvNombre = view.findViewById(R.id.tvNombreQr);
        Button btnListo = view.findViewById(R.id.btnListoQr);
        ImageView imgQr = view.findViewById(R.id.imgQr);
        tvNombre.setText(tvNombre.getText()+ " "+cliente.getNombre()+ " " + cliente.getApellido());
        imgQr.setImageBitmap(Functions.StringToBitMap(cliente.getCodigoQr()));
        if(accion.equals("RECARGAR")){
            btnListo.setText("LISTO");
        }
        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accion.equals("RETIRAR")){
                    Intent intent = new Intent(context, PrincipalCliente.class);
                    intent.putExtra("usr",cliente);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }
            }
        });
    }
}
