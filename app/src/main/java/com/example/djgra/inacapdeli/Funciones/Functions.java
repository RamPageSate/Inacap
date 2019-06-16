package com.example.djgra.inacapdeli.Funciones;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.djgra.inacapdeli.Clases.Sede;
import com.example.djgra.inacapdeli.R;


import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class Functions {

    static Map<String,String> map = new HashMap<>();
    private static  String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    //SACAR
    public static ProgressDialog CargarDatos(String cargar,  Context context){
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("" + cargar);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        return  progressDialog;
    }

    public static Response.ErrorListener FalloInternet(final Context context, final ProgressDialog progressDialog, final String Mensaje){
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                progressDialog.dismiss();
                alert.setTitle("FALLÓ LA CONEXIÓN");
                alert.setIcon(R.drawable.nowifi);
                alert.setMessage(Mensaje);
                Log.d("TAG_",""+ error.toString());
                alert.setNegativeButton("Cancelar",null);
                alert.show();
            }
        };
        return errorListener;
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;


    }
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
