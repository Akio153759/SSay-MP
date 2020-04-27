package com.tamadev.ssay_mp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

import java.util.ArrayList;

public class RVAdapterFriendsRanking extends RecyclerView.Adapter<RVAdapterFriendsRanking.ViewHolder> {
    private ArrayList<UserFriendProfile> _dataListUsers = new ArrayList<>();
    private Context context;

    public RVAdapterFriendsRanking(ArrayList<UserFriendProfile> _dataListUsers, Context context) {
        this._dataListUsers = _dataListUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_ranking_amigos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(context).load(_dataListUsers.get(position).getUrlImageProfile()).error(R.mipmap.ic_launcher).fit().centerInside().into(holder.ivProfilePhoto);
        holder.tvNick.setText(_dataListUsers.get(position).getUserID());
        holder.tvPosition.setText(position + 1 +"");
        if(_dataListUsers.get(position).getUserID().equals(Perfil.USER_ID)){
            holder.bgItem.setBackgroundResource(R.drawable.bg_search_friend_pcolor);
            holder.ivProfilePhoto.setForeground(context.getDrawable(R.drawable.circle_icon_primary_color_verde));
            holder.tvNick.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvPosition.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvNick.setText("Tú");
        }
    }

    @Override
    public int getItemCount() {
        return _dataListUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNick, tvPosition;
        ImageView ivProfilePhoto;
        LinearLayout bgItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bgItem = itemView.findViewById(R.id.bgItem);
            tvNick = itemView.findViewById(R.id.tvNick);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
        }
    }
}
