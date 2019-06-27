package com.example.djgra.inacapdeli.Adaptadores;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.djgra.inacapdeli.Clases.Pedido;
import com.example.djgra.inacapdeli.R;
import java.util.ArrayList;
public class AdaptadorPedidosPorPreparar extends BaseAdapter {
    ArrayList<Pedido> lstPedido;
    Activity context;

    public AdaptadorPedidosPorPreparar(ArrayList<Pedido> lstPedido, Activity context) {
        this.lstPedido = lstPedido;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstPedido.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstPedido.get(position).getCodigo();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.tablaporprepararpedidos,null);
        //declro items vista
        TextView tvNPedido = item.findViewById(R.id.tvNPedidoPreparar);
        TextView tvFecha = item.findViewById(R.id.tvFechaPreparar);
        TextView tvHora = item.findViewById(R.id.tvHoraPreparar);
        if(!lstPedido.isEmpty()){
                tvNPedido.setText(String.valueOf(lstPedido.get(position).getCodigo()));
                String fecha="", hora="";
                for (int c=0; c < lstPedido.get(position).getFechaPedido().length(); c++){
                    if(c < 11){
                        fecha= fecha + lstPedido.get(position).getFechaPedido().substring(c,c+1);
                    }else{
                        hora = hora + lstPedido.get(position).getFechaPedido().substring(c,c+1);
                    }

                }
                tvFecha.setText(fecha);
                tvHora.setText(hora);
        }

        return item;
    }
}
