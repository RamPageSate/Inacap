package com.example.djgra.inacapdeli.AlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.djgra.inacapdeli.R;

public class AlertDialogRecuperarPass extends AlertDialog {

    public AlertDialogRecuperarPass(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.recuperarcontrasena, null);
        setView(view);
        EditText etEmail = view.findViewById(R.id.etEmailRecuperarPass);
        Button btnSalir = view.findViewById(R.id.btnSalirPass);
        Button btnEnviar = view.findViewById(R.id.btnEnviarPass);


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
