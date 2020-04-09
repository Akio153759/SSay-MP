package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.content.Intent;
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
import com.tamadev.ssay_mp.CreacionPartida;
import com.tamadev.ssay_mp.LobbyPartidasActivity;
import com.tamadev.ssay_mp.MainActivity;
import com.tamadev.ssay_mp.MenuPrincipalActivity;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
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

                if (_objJugador.getEstado()==1 && !_dataListPartidas.get(position).getProximoJugador().equals(Perfil.USER_ID)){
                    _sEstado = "Esperando";
                    holder.lblTurno.setTextSize(16);
                    holder.lblTurno.setBackgroundResource(R.drawable.label_turno);
                    holder.lblTurno.setEnabled(false);

                }
                else if (_objJugador.getEstado()==1 && _dataListPartidas.get(position).getProximoJugador().equals(Perfil.USER_ID)){
                    _sEstado = "Jugar";
                    holder.lblTurno.setTextSize(18);
                    holder.lblTurno.setBackgroundResource(R.drawable.label_turno);
                    holder.lblTurno.setEnabled(true);
                }
                else if(_objJugador.getEstado()==0){
                    _sEstado = "¿Aceptar partida?";
                    holder.lblTurno.setEnabled(true);
                    holder.lblTurno.setBackgroundResource(R.drawable.label_nueva_partida);
                    holder.lblTurno.setTextSize(14);
                }

            }
            _iPlayerIndex++;
        }



        holder.lblTurno.setText(_sEstado);
        holder.tvAnfitrion.setText(_dataListPartidas.get(position).getAnfitrion());

        holder.lblTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.lblTurno.getText().toString().equals("Jugar")){
                    Intent i = new Intent(mContext, MainActivity.class);
                    i.putExtra("IndexPartida",position);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);

                }
                else{
                    for(int i = 0; i<_dataListPartidas.get(position).getJugadores().size();i++)
                    {
                        if (_dataListPartidas.get(position).getJugadores().get(i).getUser().equals(Perfil.USER_ID)){
                            MenuPrincipalActivity.DBrefUsuario.child("Partidas").child(_dataListPartidas.get(position).getID()).child("jugadores").child(String.valueOf(i)).child("estado").setValue(1);

                            break;
                        }
                    }


                }
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
        TextView lblTurno;
        public ViewHolder(View itemView){
            super(itemView);
            ivPlayer1 = itemView.findViewById(R.id.ivPlayer1);
            ivPlayer2 = itemView.findViewById(R.id.ivPlayer2);
            ivPlayer3 = itemView.findViewById(R.id.ivPlayer3);
            ivPlayer4 = itemView.findViewById(R.id.ivPlayer4);
            tvAnfitrion = itemView.findViewById(R.id.tvAnfitrion);
            lblTurno = itemView.findViewById(R.id.btnTurno);
        }
    }
}
