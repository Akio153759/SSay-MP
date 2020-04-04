package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.AmigosActivity;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

import java.util.ArrayList;

public class RVAdapterUserList extends RecyclerView.Adapter<RVAdapterUserList.ViewHolder> {
    private ArrayList<UserFriendProfile> _dataListUser = new ArrayList<>();
    private Context context;

    public RVAdapterUserList(ArrayList<UserFriendProfile> _dataListUser, Context context) {
        this._dataListUser = _dataListUser;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.with(context).load(_dataListUser.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivUrlProfile);
        holder.lblNick.setText(_dataListUser.get(position).getUserID());

        boolean _bFlagAmigoEnLista = false;
        boolean _bFlagSolicitudEnviada = false;

        for(UserFriendProfile _objUser: AmigosActivity._dataListAmigos){
            if(_objUser.getUserID().equals(_dataListUser.get(position).getUserID())){
                _bFlagAmigoEnLista = true;
            }
        }
        for(String _flagRequest: AmigosActivity._dataListSolicitudesEnviadas){
            if(_flagRequest.equals(_dataListUser.get(position).getUserID())){
                _bFlagSolicitudEnviada = true;
            }
        }



        if(!_bFlagAmigoEnLista){
            if (!_bFlagSolicitudEnviada){
                holder.ibAddFriend.setVisibility(View.VISIBLE);
                holder.ibAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String _sRequestKey = new CrearPartida().GenerarIDPartida(_dataListUser.get(position).getUserID() + Perfil.USER_ID);
                        AmigosActivity.DBrefUsuarioFriends.child(_dataListUser.get(position).getUserID()).child("SolicitudesRecibidas").child(_sRequestKey).setValue(Perfil.USER_ID);
                        AmigosActivity.DBrefUsuarioFriends.child(Perfil.USER_ID).child("SolicitudesEnviadas").child(_sRequestKey).setValue(_dataListUser.get(position).getUserID());
                    }
                });
            }
            else {
                holder.ibAddFriend.setVisibility(View.VISIBLE);
                holder.ibAddFriend.setBackgroundResource(R.drawable.ic_hourglass_empty_black_24dp);
                holder.ibAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.lblNick.setError("Solicitud pendiente");
                        Snackbar.make(holder.itemView, "Ya se envió una solicitud a este usuario", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
        else {
            holder.ibAddFriend.setVisibility(View.INVISIBLE);
            /*
            holder.ibAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(holder.itemView,"Este usuario ya es tu amigo",Snackbar.LENGTH_LONG).show();
                }
            });
            */
        }
    }

    @Override
    public int getItemCount() {
        return _dataListUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivUrlProfile;
        ImageButton ibAddFriend;
        TextView lblNick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUrlProfile = itemView.findViewById(R.id.ivProfileImage);
            ibAddFriend = itemView.findViewById(R.id.ibAddFriend);
            lblNick = itemView.findViewById(R.id.lblNick);
        }
    }
}
