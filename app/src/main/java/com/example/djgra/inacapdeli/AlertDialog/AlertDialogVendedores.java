package com.example.djgra.inacapdeli.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AlertDialogVendedores extends AlertDialog {
    private int posicionUdpdateDelete;

    public AlertDialogVendedores(final Activity context, final ArrayList<Persona> lstPersona) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.lstvendedores, null);
        setView(view);


    }
}
