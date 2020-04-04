package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamadev.ssay_mp.AmigosActivity;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.UserFriendProfile;

public class AlertDialogNewFriend {

    public static RVAdapterUserList adapterUserList;

    public AlertDialogNewFriend(final Context context) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog_new_friend);

        final EditText etSearchUser = dialog.findViewById(R.id.etSearchUser);
        ImageButton ibClose = dialog.findViewById(R.id.ibClose);
        RecyclerView rvUsers = dialog.findViewById(R.id.rv_users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(adapterUserList);

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchUser.setText("");
                dialog.dismiss();
            }
        });

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etSearchUser.getText().toString().equalsIgnoreCase("")){
                    AmigosActivity._dataListAllUsersFilter.clear();
                    adapterUserList.notifyDataSetChanged();
                    return;
                }
                AmigosActivity._dataListAllUsersFilter.clear();
                for (UserFriendProfile friendFilter: AmigosActivity._dataListAllUsers){
                    if (friendFilter.getUserID().toLowerCase().contains(etSearchUser.getText().toString().toLowerCase())){
                        AmigosActivity._dataListAllUsersFilter.add(friendFilter);
                    }
                }
                adapterUserList.notifyDataSetChanged();
            }
        });

        dialog.show();
    }
}
