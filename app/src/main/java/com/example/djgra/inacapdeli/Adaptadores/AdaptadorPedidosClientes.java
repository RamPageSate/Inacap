package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.R;


import java.util.ArrayList;

public class AdaptadorPedidosClientes extends RecyclerView.Adapter<AdaptadorPedidosClientes.ViewHolderPedidosClientes> {
    ArrayList<Pedido> lstPedidos = new ArrayList<>();
    Activity context = new Activity();
    //cuando meto el pedido en el adaptador hay que meter el pedidoComprado
    public AdaptadorPedidosClientes(ArrayList<Pedido> lstPedidos, Activity context) {
        this.lstPedidos = lstPedidos;
        this.context = context;
    }

    @Override
    public ViewHolderPedidosClientes onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidosanteriorescliente, null, false);
        return new ViewHolderPedidosClientes(view);
    }

    @Override
    public void onBindViewHolder( ViewHolderPedidosClientes holder, int position) {
        holder.tvNumeroPedido.setText(String.valueOf(lstPedidos.get(position).getCodigo()));
        holder.tvMontoCompra.setText(String.valueOf(lstPedidos.get(position).totalPagarPedido()));
        holder.tvCandidadArticulosPedidos.setText(String.valueOf(lstPedidos.get(position).PedidoComprado().size()));
        ArrayList<Producto> lstProducto = lstPedidos.get(position).PedidoComprado();
        AdaptadorProductoCompradoCliente adp = new AdaptadorProductoCompradoCliente(lstProducto,context);
        holder.rcProductos.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        holder.rcProductos.setAdapter(adp);
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Eliminara Pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPedidos.size();
    }

    public class ViewHolderPedidosClientes extends RecyclerView.ViewHolder {
        TextView tvNumeroPedido, tvCandidadArticulosPedidos, tvMontoCompra;
        ImageButton btnEliminar;
        RecyclerView rcProductos;
        Button btnVolverComprar;


        public ViewHolderPedidosClientes(View itemView) {
            super(itemView);

            tvNumeroPedido = itemView.findViewById(R.id.tvNumeroPedidoAnteriorCliente);
             btnEliminar = itemView.findViewById(R.id.btnEliminarPedidoAnteriorCliente);
            tvCandidadArticulosPedidos = itemView.findViewById(R.id.tvCantidadArticuloPedidoAnteriorCliente);
            tvMontoCompra = itemView.findViewById(R.id.tvMontoPedidoAnteriorCliente);
            rcProductos = itemView.findViewById(R.id.rcProductosPedidoAnteriorCliente);
            btnVolverComprar = itemView.findViewById(R.id.btnVolverComprarPedidoAnteriorCliente);

        }
    }
}
