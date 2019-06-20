package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorProductoCompradoCliente  extends RecyclerView.Adapter<AdaptadorProductoCompradoCliente. ViewHolderProductoComprado> {
    ArrayList<Producto> lstProducto = new ArrayList<>();
    Activity context = new Activity();

    public AdaptadorProductoCompradoCliente(ArrayList<Producto> lstProducto, Activity context) {
        this.lstProducto = lstProducto;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderProductoComprado onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpedidoscomprados, null,false);
        return new ViewHolderProductoComprado(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProductoComprado holder, int position) {
        holder.imagen.setImageBitmap(Functions.StringToBitMap(lstProducto.get(position).getFoto()));
        holder.tvNombre.setText("Nombre: "+lstProducto.get(position).getNombre());
        holder.tvPrecio.setText("Precio: $" +lstProducto.get(position).getPrecio());
        holder.tvCantidad.setText("Cantidad: " +lstProducto.get(position).getCantidad());
    }

    @Override
    public int getItemCount() {
        return lstProducto.size();
    }

    public class ViewHolderProductoComprado extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad, tvFecha;
        ImageView imagen;

        public ViewHolderProductoComprado(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.imgProductoPedidoComprado);
            tvNombre = itemView.findViewById(R.id.tvNombreProductoPedidoComprado);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProductoPedidoComprado);
            tvCantidad = itemView.findViewById(R.id.tvCantidadArticuloProductoPedidoComprado);
            tvFecha = itemView.findViewById(R.id.tvFechaPedidoAnteriorCliente);

        }
    }
}
