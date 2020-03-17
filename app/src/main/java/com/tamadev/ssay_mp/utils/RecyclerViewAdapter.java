package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.Perfil;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<CrearPartida> _dataListPartidas = new ArrayList<>();
    private Context mContext ;

    public RecyclerViewAdapter(Context mContext, ArrayList<CrearPartida> _dataListPartidas) {
        this.mContext = mContext;
        this._dataListPartidas = _dataListPartidas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_partidas_actuales,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivPlayer1.setImageResource(0);
        holder.ivPlayer2.setImageResource(0);
        holder.ivPlayer3.setImageResource(0);
        holder.ivPlayer4.setImageResource(0);

        int _iPlayerIndex = 0;
        String _sEstado = "";
        boolean _bPartidaFinalizada = false;

        for(Jugador _objJugador : _dataListPartidas.get(position).getJugadores()){
            switch (_iPlayerIndex){
                case 0:
                    Picasso.with(mContext).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivPlayer1);
                    break;
                case 1:
                    Picasso.with(mContext).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivPlayer2);
                    break;
                case 2:
                    Picasso.with(mContext).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivPlayer3);
                    break;
                case 3:
                    Picasso.with(mContext).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivPlayer4);
                    break;
            }

            if(_objJugador.getUser().equals(Perfil.USER_ID)){
                if (_objJugador.getEstado()==1){
                    _sEstado = "Espera...";
                    holder.btnTurno.setEnabled(false);

                }

            }
            _iPlayerIndex++;
        }

        if(_dataListPartidas.get(position).getProximoJugador().equals(Perfil.USER_ID) && !_bPartidaFinalizada){
            _sEstado = "Jugar";
            holder.btnTurno.setEnabled(true);
        }

        holder.btnTurno.setText(_sEstado);
        holder.tvAnfitrion.setText("Partida de " + _dataListPartidas.get(position).getAnfitrion());

        holder.btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Jugar Presionado",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _dataListPartidas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivPlayer1, ivPlayer2, ivPlayer3, ivPlayer4;
        TextView tvAnfitrion, tvExpiracion;
        Button btnTurno;
        public ViewHolder(View itemView){
            super(itemView);
            ivPlayer1 = itemView.findViewById(R.id.ivPlayer1);
            ivPlayer2 = itemView.findViewById(R.id.ivPlayer2);
            ivPlayer3 = itemView.findViewById(R.id.ivPlayer3);
            ivPlayer4 = itemView.findViewById(R.id.ivPlayer4);
            tvAnfitrion = itemView.findViewById(R.id.tvAnfitrion);
            btnTurno = itemView.findViewById(R.id.btnTurno);
        }
    }
}
