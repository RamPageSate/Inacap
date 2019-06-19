package com.example.djgra.inacapdeli;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorBarraVerde;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorDetalleProductoPagar;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorPagarCliente;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;

public class DetallePagarCliente extends AppCompatActivity {
    private static Pedido pedido;
    private RecyclerView rcProductos, rcPagar;
    public static TextView tvSubtotalDetallePagar, tvTotalDetalle;
    TextView tvClickAqui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pagar_cliente);
        tvClickAqui = (TextView) findViewById(R.id.tvClickAqui);
        tvTotalDetalle = (TextView)  findViewById(R.id.tvTotalPagarDetalleCliente);
        rcProductos = (RecyclerView) findViewById(R.id.rcProductoDPC);
        rcProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        tvSubtotalDetallePagar = (TextView) findViewById(R.id.tvSubTotalPagarCliente);
        rcPagar = (RecyclerView) findViewById(R.id.linearresumen);
        rcPagar.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        AdaptadorBarraVerde adaptador = new AdaptadorBarraVerde(this);
        rcPagar.setHasFixedSize(true);
        rcPagar.setAdapter(adaptador);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
        }
        AdaptadorDetalleProductoPagar adapter = new AdaptadorDetalleProductoPagar(pedido.getLstProductoPedido());
        rcProductos.setHasFixedSize(true);
        rcProductos.setAdapter(adapter);

        tvClickAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog horaDialog = new TimePickerDialog(DetallePagarCliente.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvClickAqui.setText("" + hourOfDay+":"+minute);
                    }
                }, 19, 25, true);
                horaDialog.show();
            }
        });
    }

    public static void addProductoPedido(Producto producto){
        pedido.agregarProductoListaPedido(producto);
    }
    public static void missProductoPedido(Producto producto){
        pedido.quitarProductoListaPedido(producto);
    }
    public static void sumarSubTotal(int precio){
        int total = Integer.parseInt(tvSubtotalDetallePagar.getText().toString());
        tvSubtotalDetallePagar.setText(String.valueOf(total + precio));
        tvTotalDetalle.setText("$ " + tvSubtotalDetallePagar.getText());
    }
    public static void restarSubTotal(int precio){
        int total = Integer.parseInt(tvSubtotalDetallePagar.getText().toString());
        tvSubtotalDetallePagar.setText(String.valueOf(total - precio));
        tvTotalDetalle.setText("$ " + tvSubtotalDetallePagar.getText());
    }
}
