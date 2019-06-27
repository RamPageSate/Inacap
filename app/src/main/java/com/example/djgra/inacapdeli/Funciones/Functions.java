package com.example.djgra.inacapdeli.Funciones;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.djgra.inacapdeli.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

    static Map<String, String> map = new HashMap<>();
    private static String urlBase = "https://laxjbz6j-site.gtempurl.com/igniter/funcion/";

    /**
     * @param context Contexto de la actividad
     * @param cargar  Texto para mostrar en el dialog
     * @return ProgressDialog
     * Metodo para mostrar un alertdialog de carga
     */
    public static ProgressDialog CargarDatos(String cargar, Context context) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("" + cargar);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        return progressDialog;
    }


    /**
     * @param context        Contexto de la actividad
     * @param Mensaje        Mensaje en caso de fallo del internet
     * @param progressDialog Progres dialog para ser cerradp
     * @return Response
     */
    public static Response.ErrorListener FalloInternet(final Context context, final ProgressDialog progressDialog, final String Mensaje) {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                progressDialog.dismiss();
                alert.setTitle("FALLÓ LA CONEXIÓN");
                alert.setIcon(R.drawable.nowifi);
                alert.setMessage(Mensaje);
                Log.d("TAG_", "" + error.toString());
                alert.setNegativeButton("Cancelar", null);
                alert.show();
            }
        };
        return errorListener;
    }

    /**
     * @param bmp Bitmap para ser cambiado a string
     * @return String
     * Metodo que transforma un bitmap en un string
     */
    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;


    }

    /**
     * @param encodedString String para ser transformado en bitmap
     * @return Bitmap
     * Metodo para transformar un string en Bitmap
     */
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * @param dato String para ser validado
     * @return boolean
     * Metodo para validar un string
     */
    public static boolean validarLetras(String dato) {
        boolean ok = false;

        Pattern regex = Pattern.compile("^[a-zA-Z ]*$");
        Matcher m = regex.matcher(dato);
        boolean as = m.find();
        if (as == true) {
            ok = true;
        }

        return ok;
    }

    /**
     * @param pass Cadena de texto para ser validado
     * @return boolean
     * Metodo para validar la contraseña
     */
    public static boolean contraseñaSegura(String pass) {
        boolean ok = false;
        Pattern regex = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,12}$");
        Matcher m = regex.matcher(pass);
        boolean as = m.find();
        if (as == true) {
            ok = true;
        }

        return ok;
    }


}
