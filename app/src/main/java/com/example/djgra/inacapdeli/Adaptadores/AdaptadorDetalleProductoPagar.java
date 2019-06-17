package com.example.djgra.inacapdeli.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.DetallePagarCliente;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorDetalleProductoPagar extends RecyclerView.Adapter<AdaptadorDetalleProductoPagar.ViewHolderDetalleProductoPagar> {
    ArrayList<Producto> lstProductos = new ArrayList<>();

    public AdaptadorDetalleProductoPagar(ArrayList<Producto> lstProductos) {
        this.lstProductos = lstProductos;
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
       int Total = lstProductos.get(position).getCantidad() * lstProductos.get(position).getPrecio();
       holder.tvTotal.setText("$ " +Total);
       DetallePagarCliente.sumarSubTotal(Total);
       holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DetallePagarCliente.addProductoPedido(lstProductos.get(position));
               holder.tvCantidadProducto.setText(""+ lstProductos.get(position).getCantidad());
               int Total = lstProductos.get(position).getCantidad() * lstProductos.get(position).getPrecio();
               DetallePagarCliente.sumarSubTotal(lstProductos.get(position).getPrecio());
               holder.tvTotal.setText("$ " +Total);
               holder.btnQuitar.setVisibility(View.VISIBLE);
           }
       });
       holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DetallePagarCliente.restarSubTotal(lstProductos.get(position).getPrecio());
               //si tiene 1 lo presiono se borra
               if(lstProductos.get(position).getCantidad() == 1){
                   holder.tvCantidadProducto.setText("0");
                   holder.tvTotal.setText("$ 0");
                    holder.btnQuitar.setVisibility(View.INVISIBLE);
                    lstProductos.get(position).setCantidad(lstProductos.get(position).getCantidad() - 1);
               }else{
                   DetallePagarCliente.missProductoPedido(lstProductos.get(position));
               }
               holder.tvCantidadProducto.setText(""+ lstProductos.get(position).getCantidad());
               int Total = lstProductos.get(position).getCantidad() * lstProductos.get(position).getPrecio();
               holder.tvTotal.setText("$ " +Total);
           }
       });
    }

    @Override
    public int getItemCount() {
        return lstProductos.size();
    }

    public class ViewHolderDetalleProductoPagar extends RecyclerView.ViewHolder {

        TextView tvNombreProducto, tvCantidadProducto, tvTotal;
        ImageButton btnAgregar, btnQuitar;

        public ViewHolderDetalleProductoPagar(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProductoPDC);
            tvCantidadProducto = (TextView) itemView.findViewById(R.id.tvCantidadProductoPDC);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalPDC);
            btnAgregar =(ImageButton) itemView.findViewById(R.id.btnRcAgregarDPC);
            btnQuitar = (ImageButton) itemView.findViewById(R.id.btnRcQuitarPDC);


        }
    }
}
