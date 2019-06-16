package com.example.djgra.inacapdeli.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.PrincipalCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorCategoriasCliente extends RecyclerView.Adapter<AdaptadorCategoriasCliente.ViewHolderCategoriasCliente> {
    ArrayList<Categoria> lstCategorias = new ArrayList<>();

    public AdaptadorCategoriasCliente(ArrayList<Categoria> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }
    //muestro la vista
    @NonNull
    @Override
    public ViewHolderCategoriasCliente onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcategoriacliente, null, false);
        return new ViewHolderCategoriasCliente(view);
    }


    //leno los datos
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategoriasCliente holder, final int position) {
        holder.tvnombreCategoria.setText(lstCategorias.get(position).getNombre());
        holder.tvnombreCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrincipalCliente.FiltrarListaPorCategoria(lstCategorias.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCategorias.size();
    }
    //creo los view
    public class ViewHolderCategoriasCliente extends RecyclerView.ViewHolder {
        TextView tvnombreCategoria;

        //referencio los view
        public ViewHolderCategoriasCliente(@NonNull View itemView) {
            super(itemView);
            tvnombreCategoria = itemView.findViewById(R.id.tvCategoriasProductosCliente);
        }
    }
}
