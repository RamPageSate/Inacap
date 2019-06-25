package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.content.Intent;
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
import com.example.djgra.inacapdeli.DetallePagarCliente;
import com.example.djgra.inacapdeli.R;


import java.util.ArrayList;

public class AdaptadorPedidosClientes extends RecyclerView.Adapter<AdaptadorPedidosClientes.ViewHolderPedidosClientes> {
    ArrayList<Pedido> lstPedidos = new ArrayList<>();
    Activity context = new Activity();
    String tipoHistorial = "";
    //cuando meto el pedido en el adaptador hay que meter el pedidoComprado
    public AdaptadorPedidosClientes(ArrayList<Pedido> lstPedidos, Activity context, String tipoHistorial) {
        this.lstPedidos = lstPedidos;
        this.context = context;
        this.tipoHistorial = tipoHistorial;
    }

    @Override
    public ViewHolderPedidosClientes onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidosanteriorescliente, null, false);
        return new ViewHolderPedidosClientes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPedidosClientes holder, final int position) {
        if(tipoHistorial.equals("PENDIENTES")){
            holder.btnVolverComprar.setEnabled(false);
            holder.btnVolverComprar.setVisibility(View.INVISIBLE);
            holder.btnEliminar.setVisibility(View.INVISIBLE);
            holder.btnEliminar.setEnabled(false);
        }
        holder.tvNumeroPedido.setText(String.valueOf(lstPedidos.get(position).getCodigo()));
        int total = 0;
        for(int x = 0;  x < lstPedidos.get(position).getLstProductoPedido().size(); x++){
            total = total + lstPedidos.get(position).getLstProductoPedido().get(x).getPrecio();
        }
        holder.tvMontoCompra.setText(String.valueOf(total));
        holder.tvCandidadArticulosPedidos.setText(String.valueOf(lstPedidos.get(position).PedidoComprado().size()));
        holder.tvFecha.setText(lstPedidos.get(position).getFechaPedido());
        ArrayList<Producto> lstProducto = Pedido.filtrarPedidos(lstPedidos.get(position).getLstProductoPedido());
        AdaptadorProductoCompradoCliente adp = new AdaptadorProductoCompradoCliente(lstProducto,context);
        holder.rcProductos.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        holder.rcProductos.setAdapter(adp);
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Eliminara Pedido", Toast.LENGTH_SHORT).show();


            }
        });

        holder.btnVolverComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Producto> listaComprar = new ArrayList<>();
                listaComprar= Pedido.filtrarPedidos(lstPedidos.get(position).getLstProductoPedido());
                Pedido pedido = new Pedido();
                pedido.setLstProductoPedido(listaComprar);
                Intent intent = new Intent(context, DetallePagarCliente.class);
                intent.putExtra("pedido", pedido);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstPedidos.size();
    }

    public class ViewHolderPedidosClientes extends RecyclerView.ViewHolder {
        final TextView tvNumeroPedido, tvCandidadArticulosPedidos, tvMontoCompra, tvFecha;
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
            tvFecha = itemView.findViewById(R.id.tvFechaPedidoAnteriorCliente);

        }
    }
}
