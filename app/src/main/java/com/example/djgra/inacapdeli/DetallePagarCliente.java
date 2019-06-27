package com.example.djgra.inacapdeli;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.djgra.inacapdeli.Adaptadores.AdaptadorDetalleProductoPagar;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.Clases.Producto;
import com.example.djgra.inacapdeli.Funciones.BddPedido;
import com.example.djgra.inacapdeli.Funciones.Functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetallePagarCliente extends AppCompatActivity {
    public static Pedido pedido;
    private RecyclerView rcProductos;

    int codigoActividad= 0;
    LinearLayout linearPagar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //yyyy-MM-dd HH:mm:ss
    ImageButton btnSalir;
    Long horaSeleccionada= System.currentTimeMillis();
    public static TextView tvSubtotalDetallePagar, tvTotalDetalle;
    TextView tvClickAqui, cantidadBarar, totalBarra, tvSaldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pagar_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tvClickAqui = (TextView) findViewById(R.id.tvClickAqui);
        tvSubtotalDetallePagar = (TextView)  findViewById(R.id.tvSubTotalPagarCliente);
        rcProductos = (RecyclerView) findViewById(R.id.rcProductoDPC);
        rcProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        tvTotalDetalle = (TextView) findViewById(R.id.tvTotalPagarDetalleCliente);
        cantidadBarar = findViewById(R.id.cantdetalle);
        linearPagar = findViewById(R.id.linearPagarDetalle);
        tvSaldo = findViewById(R.id.tvSaldoDetallePagarCliente);
        btnSalir = findViewById(R.id.btnSalirPDC);
        totalBarra = findViewById(R.id.totaldetalle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
            codigoActividad = bundle.getInt("code");
            tvSaldo.setText(String.valueOf(PrincipalCliente.clientePrincipal.getSaldo()));
        }
        AdaptadorDetalleProductoPagar adapter = new AdaptadorDetalleProductoPagar(pedido.getLstProductoPedido(),cantidadBarar,totalBarra,pedido,DetallePagarCliente.this);
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
                final TimePickerDialog horaDialog = new TimePickerDialog(DetallePagarCliente.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String fecha = "";
                        if(minute == 00){
                            fecha = hourOfDay + ":00:00";
                        }else{
                            if(minute < 10){
                                fecha = hourOfDay + ":0" + minute+":00";
                            }else{
                                fecha = hourOfDay + ":" + minute+":00";
                            }
                        }
                        if(HoraRetiro(fecha) <= getTimeStamp()){
                            tvClickAqui.setText("Seleccione otra Hora");
                            tvClickAqui.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            horaSeleccionada = HoraRetiro(fecha);
                            tvClickAqui.setTextColor(Color.parseColor("#3C3F41"));
                            tvClickAqui.setText(fecha);
                        }

                    }
                }, 00, 00, true);
                horaDialog.show();
            }
        });

        linearPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validad = 0;
                String mensaje = "";
                if (PrincipalCliente.clientePrincipal.getSaldo() > pedido.totalPagarPedido()) {
                    if (pedido.totalPagarPedido() == 0) {
                        mensaje = "No tiene Productos ";
                        validad++;
                    }
                    if (tvClickAqui.getText().equals("Click Aqui") || tvClickAqui.getText().equals("Seleccione otra Hora")) {
                        if(validad == 1){
                            mensaje = mensaje + " y Escoja Hora";
                        }else {
                            mensaje = mensaje + "Escoja Hora";
                        }
                        validad++;

                    }

                    if (validad == 0) {
                        pedido.setId_cliente(PrincipalCliente.clientePrincipal.getCodigo());
                        pedido.setLstProductoPedido(pedido.listaProductosFinal(pedido.getLstProductoPedido()));
                        pedido.setPedido_estado(1);
                        pedido.setId_condicion_pedido(2);
                        pedido.setId_vendedor(pedido.getId_cliente());
                        pedido.setFechaPedido(sdf.format(getDate(horaSeleccionada)));
                        final ProgressDialog progressDialog = Functions.CargarDatos("Realizando Pedido", DetallePagarCliente.this);
                        BddPedido.setPedido(pedido, DetallePagarCliente.this, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Toast.makeText(DetallePagarCliente.this, "Pedido Realizado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetallePagarCliente.this, PedidosCliente.class);
                                intent.putExtra("cliente", PrincipalCliente.clientePrincipal);
                                intent.putExtra("code", 3);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                                //enviar pedido a vendedor//
                                //eliminartodo el pedido ya comptado
                                //enviar pedido a a historial activos
                                //descontar saldo
                                //cuando paga hay que resetear la barra verde
                            }
                        }, Functions.FalloInternet(DetallePagarCliente.this, progressDialog, "No realizo Compra"));
                    } else {
                        Toast.makeText(DetallePagarCliente.this, mensaje, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DetallePagarCliente.this, "No cuenta con Saldo Suficiente", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void Subtotal(int total){
        tvTotalDetalle.setText(String.valueOf(total));
        tvSubtotalDetallePagar.setText(String.valueOf(total));
    }
    private Long getTimeStamp(){
        Long tiemStamp = System.currentTimeMillis();
        return tiemStamp;
    }

    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.

        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time) );
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Long HoraRetiro(String horaSeleccionada){
        boolean ok = false;
        long millis;
        Date date = new Date();
        SimpleDateFormat fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fecha = fechaActual.format(date);
        String mytime = horaSeleccionada;
        String toParse = fecha + " " + mytime;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date dates = null;
        try {
            dates = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        millis = dates.getTime();

        return millis;
    }

}
