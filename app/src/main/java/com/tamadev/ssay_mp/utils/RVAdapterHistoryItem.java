package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

import java.util.ArrayList;

public class RVAdapterHistoryItem extends RecyclerView.Adapter<RVAdapterHistoryItem.ViewHolder>{
    private Context context;
    private ArrayList<CrearPartida> _dataListPartidas = new ArrayList<>();

    public RVAdapterHistoryItem(Context context, ArrayList<CrearPartida> _dataListPartidas) {
        this.context = context;
        this._dataListPartidas = _dataListPartidas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_games_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(_dataListPartidas.get(position).getCantidadJugadores() == 2){
            holder.lblNickThird.setVisibility(View.INVISIBLE);
            holder.lblThird.setVisibility(View.INVISIBLE);
            holder.lblNickQuarter.setVisibility(View.INVISIBLE);
            holder.lblQuarter.setVisibility(View.INVISIBLE);
        }
        else if(_dataListPartidas.get(position).getCantidadJugadores() == 3){
            holder.lblNickQuarter.setVisibility(View.INVISIBLE);
            holder.lblQuarter.setVisibility(View.INVISIBLE);
        }
        else{

        }

        for(Jugador player: _dataListPartidas.get(position).getJugadores()){

            boolean _bMyProfile = false;

            if(player.getUser().equals(Perfil.USER_ID))
                _bMyProfile = true;


            if(String.valueOf(player.getEstado()).startsWith("3") && String.valueOf(player.getEstado()).endsWith("1")){
                holder.lblNickFirst.setText(player.getUser());


                if(_bMyProfile)
                    holder.ivPosition.setImageResource(R.drawable.medallaoro);
            }
            else if(String.valueOf(player.getEstado()).startsWith("3") && String.valueOf(player.getEstado()).endsWith("2")){
                holder.lblNickSecond.setText(player.getUser());

                if(_bMyProfile)
                    holder.ivPosition.setImageResource(R.drawable.medallaplata);
            }
            else if(String.valueOf(player.getEstado()).startsWith("3") && String.valueOf(player.getEstado()).endsWith("3")){
                holder.lblNickThird.setText(player.getUser());

                if(_bMyProfile)
                    holder.ivPosition.setImageResource(R.drawable.medallabronce);
            }
            else if(String.valueOf(player.getEstado()).startsWith("3") && String.valueOf(player.getEstado()).endsWith("4")){
                holder.lblNickQuarter.setText(player.getUser());

                if(_bMyProfile)
                    holder.ivPosition.setImageResource(R.drawable.medallaloser);
            }
        }

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return _dataListPartidas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPosition,lblFirst,lblSecond,lblThird, lblQuarter;
        TextView lblNickFirst, lblNickSecond, lblNickThird, lblNickQuarter;
        ImageButton btnViewMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosition = itemView.findViewById(R.id.ivPosition);
            lblFirst = itemView.findViewById(R.id.lbl1ro);
            lblNickFirst = itemView.findViewById(R.id.lblNick1ro);
            lblSecond = itemView.findViewById(R.id.lbl2do);
            lblNickSecond = itemView.findViewById(R.id.lblNick2do);
            lblThird = itemView.findViewById(R.id.lbl3ro);
            lblNickThird = itemView.findViewById(R.id.lblNick3ro);
            lblQuarter = itemView.findViewById(R.id.lbl4to);
            lblNickQuarter = itemView.findViewById(R.id.lblNick4to);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }
}
