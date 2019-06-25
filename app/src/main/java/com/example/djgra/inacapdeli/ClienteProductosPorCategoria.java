package com.example.djgra.inacapdeli;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;

import java.util.ArrayList;

public class ClienteProductosPorCategoria extends AppCompatActivity {
    TextView tvNombreCategoria;
    public static TextView tvCantidadProductos, tvTotal;
    RecyclerView rcProductos;
    ImageButton btnSalir;
    Categoria categoria;
    ImageView favoritos;
    ArrayList<Producto> lstProducto = new ArrayList<>();
    public static Pedido pedido = new Pedido();
    LinearLayout linearBarra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_productos_por_categoria);
        tvNombreCategoria = (TextView) findViewById(R.id.tvTituloCategoriasFiltrada);
        rcProductos = (RecyclerView) findViewById(R.id.rcProductoCPC);
        linearBarra = (LinearLayout) findViewById(R.id.linearahora);
        favoritos = findViewById(R.id.imgFavoritos);
        tvCantidadProductos = (TextView) findViewById(R.id.tvCantidadArticulosCPC);
        tvTotal = (TextView) findViewById(R.id.tvTotalCPC);
        btnSalir = (ImageButton) findViewById(R.id.btnSalirCPC);
        rcProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            categoria = (Categoria) bundle.getSerializable("categoria");
            lstProducto = (ArrayList<Producto>) bundle.getSerializable("listaProducto") ;
            tvNombreCategoria.setText(""+ categoria.getNombre());
            pedido = (Pedido) bundle.getSerializable("pedido");
            if(pedido.getLstProductoPedido().isEmpty()){
                linearBarra.setVisibility(View.INVISIBLE);
            }
            if(categoria.getNombre().equals("Mis Favoritas") && lstProducto.isEmpty()){
                favoritos.setVisibility(View.VISIBLE);
            }else{
                favoritos.setVisibility(View.INVISIBLE);
            }
        }
        AdaptadorRecyclerViewProductoCliente adpatador = new AdaptadorRecyclerViewProductoCliente(lstProducto, tvCantidadProductos,tvTotal,pedido,ClienteProductosPorCategoria.this,linearBarra,PrincipalCliente.clientePrincipal,categoria);
        rcProductos.setHasFixedSize(false);
        rcProductos.setItemViewCacheSize(lstProducto.size());
        rcProductos.setAdapter(adpatador);
        linearBarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteProductosPorCategoria.this, DetallePagarCliente.class);
                intent.putExtra("pedido", pedido);
                intent.putExtra("code", 2);
                startActivityForResult(intent,23);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inta = getIntent();
                setResult(23, inta);
                finish();
            }
        });



    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 23){
                pedido = PrincipalCliente.pedidoCliente;
            }
            if( requestCode == 7){
                pedido = DetallePagarCliente.pedido;
            }
            AdaptadorRecyclerViewProductoCliente adp = new AdaptadorRecyclerViewProductoCliente(lstProducto, tvCantidadProductos,tvTotal,pedido,ClienteProductosPorCategoria.this,linearBarra, PrincipalCliente.clientePrincipal,null);
            rcProductos.setHasFixedSize(true);
            rcProductos.setItemViewCacheSize(lstProducto.size());
            rcProductos.setAdapter(adp);
            if(pedido.totalPagarPedido() == 0){
                linearBarra.setVisibility(View.INVISIBLE);
            }



    }
}
