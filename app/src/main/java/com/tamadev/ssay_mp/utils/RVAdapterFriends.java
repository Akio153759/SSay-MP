package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.CreacionPartida;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

import java.util.ArrayList;

public class RVAdapterFriends extends RecyclerView.Adapter<RVAdapterFriends.ViewHolder> {

    private ArrayList<UserFriendProfile> _dataListAmigosAdd = new ArrayList<>();
    private Context mContext;

    public RVAdapterFriends(ArrayList<UserFriendProfile> _dataListAmigosAdd, Context mContext) {
        this._dataListAmigosAdd = _dataListAmigosAdd;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_amigos_add,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.with(mContext).load(_dataListAmigosAdd.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivPhotoFriend);
        holder.lblUserFriend.setText(_dataListAmigosAdd.get(position).getUserID());

        holder.ivPhotoFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < CreacionPartida.LISTA_JUGADORES_SALA.size(); i++){
                    if(CreacionPartida.LISTA_JUGADORES_SALA.get(i).getUserID().equals(_dataListAmigosAdd.get(position).getUserID())){

                        Snackbar.make(holder.itemView,"Ya añadiste este usuario",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if(CreacionPartida.LISTA_JUGADORES_SALA.size() < 4) {

                    CreacionPartida.LISTA_JUGADORES_SALA.add(_dataListAmigosAdd.get(position));

                    if(CreacionPartida.LBL_P2_SALA.getText().equals("Vacio")){
                        Picasso.with(mContext).load(_dataListAmigosAdd.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(CreacionPartida.IV_P2_SALA);
                        CreacionPartida.LBL_P2_SALA.setText(_dataListAmigosAdd.get(position).getUserID());
                        CreacionPartida.IV_P2_SALA.setEnabled(true);
                        CreacionPartida.LBL_P2_SALA.setEnabled(true);
                    }
                    else if(CreacionPartida.LBL_P3_SALA.getText().equals("Vacio")){
                        Picasso.with(mContext).load(_dataListAmigosAdd.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(CreacionPartida.IV_P3_SALA);
                        CreacionPartida.LBL_P3_SALA.setText(_dataListAmigosAdd.get(position).getUserID());
                        CreacionPartida.IV_P3_SALA.setEnabled(true);
                        CreacionPartida.LBL_P3_SALA.setEnabled(true);
                    }
                    else{
                        Picasso.with(mContext).load(_dataListAmigosAdd.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(CreacionPartida.IV_P4_SALA);
                        CreacionPartida.LBL_P4_SALA.setText(_dataListAmigosAdd.get(position).getUserID());
                        CreacionPartida.IV_P4_SALA.setEnabled(true);
                        CreacionPartida.LBL_P4_SALA.setEnabled(true);
                    }
                }
                else {
                    Snackbar.make(holder.itemView,"Capacidad máxima en la sala",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return _dataListAmigosAdd.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivPhotoFriend;
        TextView lblUserFriend;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhotoFriend = itemView.findViewById(R.id.ivPhotoFriend);
            lblUserFriend = itemView.findViewById(R.id.lblUserFriend);
        }
    }
}
