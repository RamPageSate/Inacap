package com.example.djgra.inacapdeli.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.PrincipalCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorRecyclerViewProductoCliente extends RecyclerView.Adapter<AdaptadorRecyclerViewProductoCliente.ViewHolderProducto> {
    ArrayList<Producto> lstProductos = new ArrayList<>();

    public AdaptadorRecyclerViewProductoCliente(ArrayList<Producto> lstProductos) {
        this.lstProductos = lstProductos;
    }

    //muestro la vista
    @NonNull
    @Override
    public ViewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewproductocliente, null, false);
        return new ViewHolderProducto(view);
    }

    //leno los datos
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderProducto holder, final int position) {
        holder.nombreProducto.setText(lstProductos.get(position).getNombre());
        holder.descripcionProducto.setText(lstProductos.get(position).getDescripcion());
        holder.precioProducto.setText("$ " + lstProductos.get(position).getPrecio());
        holder.imgProducto.setImageBitmap(Functions.StringToBitMap(lstProductos.get(position).getFoto()));
        holder.cantidadProducto.setText("0");

        holder.btnDescontar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = PrincipalCliente.descontarTotalCompra(lstProductos.get(position).getPrecio());
                int cant = Integer.parseInt(holder.cantidadProducto.getText().toString()) - 1;
                holder.cantidadProducto.setText("" + cant);
                PrincipalCliente.descontarCantidadArticulos();
                if (total == 0 || Integer.parseInt(holder.cantidadProducto.getText().toString()) == 0) {
                    holder.btnDescontar.setVisibility(View.INVISIBLE);
                    holder.cantidadProducto.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrincipalCliente.pagoTotal(lstProductos.get(position).getPrecio());
                PrincipalCliente.agregarCantidadArticulos();
                holder.btnDescontar.setVisibility(View.VISIBLE);
                holder.cantidadProducto.setVisibility(View.VISIBLE);
                int cant = Integer.parseInt(holder.cantidadProducto.getText().toString()) + 1;
                holder.cantidadProducto.setText("" + cant);
            }
        });

    }

    //le digo la cantidad de items
    @Override
    public int getItemCount() {
        return lstProductos.size();
    }

    //creo los view
    public class ViewHolderProducto extends RecyclerView.ViewHolder {
        TextView nombreProducto, descripcionProducto, precioProducto, cantidadProducto;
        ImageView imgProducto;
        ImageButton btnAgregar, btnDescontar;


        //referencio los view
        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);

            nombreProducto = itemView.findViewById(R.id.tvNombreProductoCliente);
            descripcionProducto = itemView.findViewById(R.id.tvCategoriaProductoCliente);
            precioProducto = itemView.findViewById(R.id.tvPrecioProductoCliente);
            btnAgregar = itemView.findViewById(R.id.btnAgregarProductoCliente);
            imgProducto = itemView.findViewById(R.id.imgProductoClente);
            cantidadProducto = itemView.findViewById(R.id.tvCantidadSeleccionadaProductoCliente);
            btnDescontar = itemView.findViewById(R.id.btnMenosProductoSeleccionadoCliente);


        }
    }
}
