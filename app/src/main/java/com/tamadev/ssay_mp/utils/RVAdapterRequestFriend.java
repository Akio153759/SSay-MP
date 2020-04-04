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

import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.InicioActivity;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.RequestFriend;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

import java.util.ArrayList;

public class RVAdapterRequestFriend extends RecyclerView.Adapter<RVAdapterRequestFriend.ViewHolder>{
    private Context context;
    private ArrayList<RequestFriend> _dataListRequest = new ArrayList<>();

    public RVAdapterRequestFriend(Context context, ArrayList<RequestFriend> _dataListRequest) {
        this.context = context;
        this._dataListRequest = _dataListRequest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_friend_request,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(context).load(_dataListRequest.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivProfileImage);
        holder.lblNick.setText(_dataListRequest.get(position).getUserID()+" quiere ser tu amigo/a");

        holder.ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Borro tanto la solicitud enviada por parte de el, como la recibida por parte mia
                InicioActivity.DBrefFriendRequest.child(Perfil.USER_ID).child("SolicitudesRecibidas").child(_dataListRequest.get(position).getFriendKey()).removeValue();
                InicioActivity.DBrefFriendRequest.child(_dataListRequest.get(position).getUserID()).child("SolicitudesEnviadas").child(_dataListRequest.get(position).getFriendKey()).removeValue();

                //Aca nos agrego como amigo tanto en mi lista, como en su lista
                InicioActivity.DBrefFriendRequest.child(Perfil.USER_ID).child("Amigos").child(_dataListRequest.get(position).getFriendKey()).setValue(_dataListRequest.get(position).getUserID());
                InicioActivity.DBrefFriendRequest.child(_dataListRequest.get(position).getUserID()).child("Amigos").child(_dataListRequest.get(position).getFriendKey()).setValue(Perfil.USER_ID);
            }
        });

        holder.ibDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Borro tanto la solicitud enviada por parte de el, como la recibida por parte mia
                InicioActivity.DBrefFriendRequest.child(Perfil.USER_ID).child("SolicitudesRecibidas").child(_dataListRequest.get(position).getFriendKey()).removeValue();
                InicioActivity.DBrefFriendRequest.child(_dataListRequest.get(position).getUserID()).child("SolicitudesEnviadas").child(_dataListRequest.get(position).getFriendKey()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _dataListRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView lblNick;
        ImageButton ibConfirm, ibDeny;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            lblNick = itemView.findViewById(R.id.lblNick);
            ibConfirm = itemView.findViewById(R.id.ibConfirm);
            ibDeny = itemView.findViewById(R.id.ibDeny);
        }
    }
}
