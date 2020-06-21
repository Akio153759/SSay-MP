package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.tamadev.ssay_mp.R;

public class AlertDialogNewRequestFriend {

    public static RVAdapterRequestFriend adapterRequestFriend;


    public AlertDialogNewRequestFriend(final Context context) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog_new_friend_request);

        ImageButton ibClose = dialog.findViewById(R.id.ibClose);
        RecyclerView rvRequests = dialog.findViewById(R.id.rv_requests);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);

        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(adapterRequestFriend);


        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}
