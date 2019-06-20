package com.example.djgra.inacapdeli;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.djgra.inacapdeli.Adaptadores.AdaptadorDetalleProductoPagar;
import com.example.djgra.inacapdeli.Clases.Pedido;

public class DetallePagarCliente extends AppCompatActivity {
    public static Pedido pedido;
    private RecyclerView rcProductos;
    int codigoActividad= 0;
    ImageButton btnSalir;
    public static TextView tvSubtotalDetallePagar, tvTotalDetalle;
    TextView tvClickAqui, cantidadBarar, totalBarra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pagar_cliente);
        tvClickAqui = (TextView) findViewById(R.id.tvClickAqui);
        tvSubtotalDetallePagar = (TextView)  findViewById(R.id.tvSubTotalPagarCliente);
        rcProductos = (RecyclerView) findViewById(R.id.rcProductoDPC);
        rcProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        tvTotalDetalle = (TextView) findViewById(R.id.tvTotalPagarDetalleCliente);
        cantidadBarar = findViewById(R.id.cantdetalle);
        btnSalir = findViewById(R.id.btnSalirPDC);
        totalBarra = findViewById(R.id.totaldetalle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
            codigoActividad = bundle.getInt("code");
        }
        AdaptadorDetalleProductoPagar adapter = new AdaptadorDetalleProductoPagar(pedido.getLstProductoPedido(),cantidadBarar,totalBarra,pedido,DetallePagarCliente.this);
        rcProductos.setHasFixedSize(true);
        rcProductos.setAdapter(adapter);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codigoActividad == 1){
                    //regresara a principal cliente
                    Intent inta = getIntent();
                    setResult(7, inta);
                }else{
                    Intent inta = getIntent();
                    setResult(7, inta);
                    // regresara a categorias
                }
                finish();
            }
        });
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

    public static void Subtotal(int total){
        tvTotalDetalle.setText(String.valueOf(total));
        tvSubtotalDetallePagar.setText(String.valueOf(total));
    }
}
