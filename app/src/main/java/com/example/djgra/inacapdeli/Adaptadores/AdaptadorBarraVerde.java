package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djgra.inacapdeli.R;

import org.w3c.dom.Text;

public class AdaptadorBarraVerde extends RecyclerView.Adapter<AdaptadorBarraVerde.ViewHolderBarraVerder> {


    Activity context;

    public AdaptadorBarraVerde(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderBarraVerder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.viewbarrapagar, null, false);
        return new ViewHolderBarraVerder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBarraVerder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolderBarraVerder extends RecyclerView.ViewHolder {
        TextView tvCantidad, tvTotal;

        public ViewHolderBarraVerder(@NonNull View itemView) {
            super(itemView);

            tvCantidad = itemView.findViewById(R.id.tvCantidadVerdePagar);
            tvTotal = itemView.findViewById(R.id.tvTotalClienteVerde);
        }
    }
}
