package com.example.djgra.inacapdeli.Adaptadores;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Persona;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Clases.Producto_Favorito;
import com.example.djgra.inacapdeli.Funciones.Functions;
import com.example.djgra.inacapdeli.R;

import java.util.ArrayList;

public class AdaptadorRecyclerViewProductoCliente extends RecyclerView.Adapter<AdaptadorRecyclerViewProductoCliente.ViewHolderProducto> {
    ArrayList<Producto> lstProductos = new ArrayList<>();
    TextView cantidadProductosPedido, totalPagarPedido;
    Pedido pedido = new Pedido();
    Activity context = new Activity();
    LinearLayout linearLayout;
    Persona cliente;

    public AdaptadorRecyclerViewProductoCliente(ArrayList<Producto> lstProductos, TextView cantidadProductosPedido, TextView totalPagarPedido, Pedido pedido, Activity context, LinearLayout linearLayout, Persona cliente) {
        this.lstProductos = lstProductos;
        this.cantidadProductosPedido = cantidadProductosPedido;
        this.totalPagarPedido = totalPagarPedido;
        this.pedido = pedido;
        this.context = context;
        this.linearLayout = linearLayout;
        this.cliente = cliente;
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
        if(!cliente.getLstProductosFavoritos().isEmpty()){
            for(int x=0; x < cliente.getLstProductosFavoritos().size(); x++){
                if(cliente.getLstProductosFavoritos().get(x).getId_producto() == lstProductos.get(position).getCodigo()){
                    holder.btnLike.setBackgroundResource(R.drawable.like);
                }
            }
        }
        if(!pedido.getLstProductoPedido().isEmpty()){
            for(int x=0 ; x < pedido.getLstProductoPedido().size(); x++){
                if(pedido.getLstProductoPedido().get(x).getCodigo() == lstProductos.get(position).getCodigo()){
                    lstProductos.get(position).setCantidad(pedido.getLstProductoPedido().get(x).getCantidad());
                    holder.cantidadProducto.setVisibility(View.VISIBLE);
                    holder.btnDescontar.setVisibility(View.VISIBLE);
                    holder.cantidadProducto.setText(String.valueOf(pedido.getLstProductoPedido().get(x).getCantidad()));
                }else{
                    holder.cantidadProducto.setText("0");
                }
                cantidadProductosPedido.setText(String.valueOf(pedido.cantidadArticulos()));
                totalPagarPedido.setText(String.valueOf(pedido.totalPagarPedido()));
            }
        }else{
            holder.cantidadProducto.setVisibility(View.INVISIBLE);
            holder.cantidadProducto.setText("0");
        }
        holder.btnDescontar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pedido.quitarProductoListaPedido(lstProductos.get(position)) == true){
                    cantidadProductosPedido.setText(String.valueOf(pedido.cantidadArticulos()));
                    holder.cantidadProducto.setText("0");
                    holder.cantidadProducto.setVisibility(View.INVISIBLE);
                    holder.btnDescontar.setVisibility(View.INVISIBLE);
                    totalPagarPedido.setText(String.valueOf(pedido.totalPagarPedido()));

                }else{
                    cantidadProductosPedido.setText(String.valueOf(pedido.cantidadArticulos()));
                    totalPagarPedido.setText(String.valueOf(pedido.totalPagarPedido()));
                    holder.cantidadProducto.setText(String.valueOf(Integer.parseInt(holder.cantidadProducto.getText().toString()) -1));
                }
                    //solo desconto la cantidad
                if(Integer.parseInt(cantidadProductosPedido.getText().toString()) == 0){
                    linearLayout.setVisibility(View.INVISIBLE);
                }
                Log.d("TAG","quita");
            }
        });
        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pedido.cantidadArticulos() == 10){
                    Toast.makeText(context, "Solo 10 productos por pedido", Toast.LENGTH_SHORT).show();
                }else{
                    pedido.agregarProductoListaPedido(lstProductos.get(position));
                    holder.cantidadProducto.setVisibility(View.VISIBLE);
                    holder.cantidadProducto.setText(String.valueOf(Integer.parseInt(holder.cantidadProducto.getText().toString()) + 1));// aqui no
                    linearLayout.setVisibility(View.VISIBLE);
                    holder.btnDescontar.setVisibility(View.VISIBLE);
                    cantidadProductosPedido.setText(String.valueOf(pedido.cantidadArticulos()));
                    totalPagarPedido.setText(String.valueOf(pedido.totalPagarPedido()));
                }
                Log.d("TAG","suma");
            }
        });
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //falta agregar a la BD tabla favoritos
                Producto_Favorito pro = new Producto_Favorito();
                pro.setId_cliente(cliente.getCodigo());
                pro.setId_producto(lstProductos.get(position).getCodigo());
                if(cliente.ProductoFavorito(pro) == true){
                    holder.btnLike.setBackgroundResource(R.drawable.like);
                }else{
                    holder.btnLike.setBackgroundResource(R.drawable.nolike);
                }
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
        ImageButton btnAgregar, btnDescontar, btnLike;


        //referencio los view
        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);

            nombreProducto = itemView.findViewById(R.id.tvNombreProductoPDC);
            descripcionProducto = itemView.findViewById(R.id.tvCategoriaProductoCliente);
            precioProducto = itemView.findViewById(R.id.tvPrecioProductoCliente);
            btnAgregar = itemView.findViewById(R.id.btnRcAgregarDPC);
            imgProducto = itemView.findViewById(R.id.imgProductoClente);
            cantidadProducto = itemView.findViewById(R.id.tvCantidadProductoPDC);
            btnDescontar = itemView.findViewById(R.id.btnRcQuitarPDC);
            btnLike = itemView.findViewById(R.id.btnLike);

        }
    }
}
