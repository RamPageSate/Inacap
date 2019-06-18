package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.djgra.inacapdeli.PrincipalCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;


public class AdaptadorPagarCliente extends RecyclerView.Adapter<AdaptadorPagarCliente.ViewHolderPagarCliente> {
    private final Activity context;
    public static  int cantidad = 0, totalPagar = 0;

    public AdaptadorPagarCliente(Activity context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderPagarCliente onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewbarrapagar, null, false);
        Log.d("TAG_" , "contex " +context.toString());
        return new ViewHolderPagarCliente(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPagarCliente holder, final int i) {
        holder.tvTotal.setText("$" + totalPagar);
        holder.tvCantidad.setText(""+ cantidad);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrincipalCliente.class);
                Log.d("TAG_" , "contex " +context.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolderPagarCliente extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView tvCantidad, tvTotal;

        public ViewHolderPagarCliente(@NonNull View itemView) {
            super(itemView);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadVerdePagar);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalClienteVerde);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearVerdePagar);
        }
    }

    public static int getCantidad() {
        return cantidad;
    }

    public static void setCantidad(int cantidad) {
        AdaptadorPagarCliente.cantidad = cantidad;
    }

    public static int getTotalPagar() {
        return totalPagar;
    }

    public static void setTotalPagar(int totalPagar) {
        AdaptadorPagarCliente.totalPagar = totalPagar;
    }

    public static void  aumentarPago(int precio){
        cantidad++;
        totalPagar = totalPagar + precio;
    }
}
