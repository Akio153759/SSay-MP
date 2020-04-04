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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.AmigosActivity;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.classes.UsuarioEnFirebase;

import java.util.ArrayList;

public class RVAdapterFriendList extends RecyclerView.Adapter<RVAdapterFriendList.ViewHolder> implements AlertDialogTwoButtons.AlertDialogTwoButtonsCallbackPosition {
    private ArrayList<UserFriendProfile> _dataListAmigos = new ArrayList<>();
    private ArrayList<UsuarioEnFirebase> _dataListIDAmigos = new ArrayList<>();
    private Context mContext;

    public RVAdapterFriendList(ArrayList<UserFriendProfile> _dataListAmigos,ArrayList<UsuarioEnFirebase> _dataListIDAmigos,Context mContext) {
        this._dataListAmigos = _dataListAmigos;
        this.mContext = mContext;
        this._dataListIDAmigos = _dataListIDAmigos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_friends,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(mContext).load(_dataListAmigos.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivProfileImage);
        holder.lblNick.setText(_dataListAmigos.get(position).getUserID());

        holder.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogTwoButtons(mContext,"Información","¿Remover a " + _dataListAmigos.get(position).getUserID() + " de mis amigos","Aceptar","Cancelar",RVAdapterFriendList.this,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _dataListAmigos.size();
    }

    @Override
    public void ResultCallback(int Result, final int position) {
        if(Result==1) {
            final DatabaseReference dbRefAmigo = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_dataListIDAmigos.get(position).getUsuario()).child("Amigos");
            dbRefAmigo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot SusAmigos) {
                    for(DataSnapshot usuario: SusAmigos.getChildren()){
                        if(usuario.getValue().toString().equals(Perfil.USER_ID)){
                            dbRefAmigo.child(usuario.getKey()).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            AmigosActivity.DBrefUsuarioFriends.child(Perfil.USER_ID).child("Amigos").child(_dataListIDAmigos.get(position).getId()).removeValue();
            AmigosActivity.etSearchFriend.setText("");
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        ImageButton ibRemove;
        TextView lblNick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ibRemove = itemView.findViewById(R.id.ibRemove);
            lblNick = itemView.findViewById(R.id.lblNick);
        }
    }
}
