package com.example.djgra.inacapdeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorRecyclerViewProductoCliente;
import com.example.djgra.inacapdeli.Clases.Categoria;
import com.example.djgra.inacapdeli.Clases.Producto;

import java.util.ArrayList;

public class ClienteProductosPorCategoria extends AppCompatActivity {
    TextView tvNombreCategoria;
    public static TextView tvCantidadProductos, tvTotal;
    RecyclerView rcProductos;
    ImageButton btnSalir;
    ArrayList<Producto> lstProducto = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_productos_por_categoria);
        tvNombreCategoria = (TextView) findViewById(R.id.tvTituloCategoriasFiltrada);
        rcProductos = (RecyclerView) findViewById(R.id.rcProductoCPC);
        tvCantidadProductos = (TextView) findViewById(R.id.tvCantidadArticulosCPC);
        tvTotal = (TextView) findViewById(R.id.tvTotalCPC);
        btnSalir = (ImageButton) findViewById(R.id.btnSalirCPC);
        rcProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            Categoria categoria = (Categoria) bundle.getSerializable("categoria");
            lstProducto = (ArrayList<Producto>) bundle.getSerializable("listaProducto") ;
            tvNombreCategoria.setText(""+ categoria.getNombre());
        }
        AdaptadorRecyclerViewProductoCliente adpatador = new AdaptadorRecyclerViewProductoCliente(lstProducto);
        rcProductos.setHasFixedSize(true);

        rcProductos.setAdapter(adpatador);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}
