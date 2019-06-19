package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.DetallePagarCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class AdaptadorDetalleProductoPagar extends RecyclerView.Adapter<AdaptadorDetalleProductoPagar.ViewHolderDetalleProductoPagar> {
    ArrayList<Producto> lstProductos = new ArrayList<>();
    Pedido pedido = new Pedido();
    TextView cantidad;
    TextView total;
    Activity context = new Activity();

    public AdaptadorDetalleProductoPagar(ArrayList<Producto> lstProductos, TextView cantidad, TextView total,Pedido pedido, Activity context) {
        this.lstProductos = lstProductos;
        this.cantidad = cantidad;
        this.total = total;
        this.pedido = pedido;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderDetalleProductoPagar onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagardetalleclienteproducto, null, false);
        return new ViewHolderDetalleProductoPagar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDetalleProductoPagar holder, final int position) {
        holder.tvNombreProducto.setText(lstProductos.get(position).getNombre());
       holder.tvCantidadProducto.setText(""+ lstProductos.get(position).getCantidad());
       holder.tvTotalProducto.setText("$ " +(lstProductos.get(position).getCantidad() * lstProductos.get(position).getPrecio()));
       cantidad.setText(String.valueOf(pedido.cantidadArticulos()));
       total.setText(String.valueOf(pedido.totalPagarPedido()));
       DetallePagarCliente.Subtotal(pedido.totalPagarPedido());
       holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(pedido.cantidadArticulos() == 10){
                   Toast.makeText(context, "Solo 10 productos por Pedido", Toast.LENGTH_SHORT).show();
               }else {
                   pedido.agregarProductoListaPedido(lstProductos.get(position));
                   holder.btnQuitar.setVisibility(View.VISIBLE);
                   cantidad.setText(String.valueOf(pedido.cantidadArticulos()));
                   total.setText(String.valueOf(pedido.totalPagarPedido()));
                   holder.tvCantidadProducto.setText(String.valueOf(Integer.parseInt(holder.tvCantidadProducto.getText().toString()) + 1));
                   int TotalporProducto = Integer.parseInt(holder.tvCantidadProducto.getText().toString()) * lstProductos.get(position).getPrecio();
                   holder.tvTotalProducto.setText("$ " + TotalporProducto);
                   DetallePagarCliente.Subtotal(pedido.totalPagarPedido());
               }

           }
       });
       holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(pedido.quitarProductoListaPedido(lstProductos.get(position)) == true){
                   cantidad.setText(String.valueOf(pedido.cantidadArticulos()));
                   total.setText(String.valueOf(pedido.totalPagarPedido()));
                   DetallePagarCliente.Subtotal(pedido.totalPagarPedido());
                   holder.btnQuitar.setVisibility(View.INVISIBLE);
                   holder.tvCantidadProducto.setText("0");
                   holder.tvTotalProducto.setText("$ 0");
               }else{
                   cantidad.setText(String.valueOf(pedido.cantidadArticulos()));
                   total.setText(String.valueOf(pedido.totalPagarPedido()));
                   holder.tvCantidadProducto.setText(String.valueOf(Integer.parseInt(holder.tvCantidadProducto.getText().toString()) - 1));
                   int TotalporProducto = Integer.parseInt(holder.tvCantidadProducto.getText().toString()) * lstProductos.get(position).getPrecio();
                   holder.tvTotalProducto.setText("$ " + TotalporProducto);
                   DetallePagarCliente.Subtotal(pedido.totalPagarPedido());
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return lstProductos.size();
    }

    public class ViewHolderDetalleProductoPagar extends RecyclerView.ViewHolder {

        TextView tvNombreProducto, tvCantidadProducto, tvTotalProducto;
        ImageButton btnAgregar, btnQuitar;

        public ViewHolderDetalleProductoPagar(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProductoPDC);
            tvCantidadProducto = (TextView) itemView.findViewById(R.id.tvCantidadProductoPDC);
            tvTotalProducto = (TextView) itemView.findViewById(R.id.tvTotalPDC);
            btnAgregar =(ImageButton) itemView.findViewById(R.id.btnRcAgregarDPC);
            btnQuitar = (ImageButton) itemView.findViewById(R.id.btnRcQuitarPDC);


        }
    }
}
