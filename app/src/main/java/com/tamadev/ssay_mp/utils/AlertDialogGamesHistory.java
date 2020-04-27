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

import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.CrearPartida;

import java.util.ArrayList;

public class AlertDialogGamesHistory {

    public AlertDialogGamesHistory(Context context, ArrayList<CrearPartida> _dataListPartidas) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog_games_history);

        ImageButton ibClose = dialog.findViewById(R.id.ibClose);
        RecyclerView rvHistory= dialog.findViewById(R.id.rv_history);

        RVAdapterHistoryItem adapterHistoryItem = new RVAdapterHistoryItem(context,_dataListPartidas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);

        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(adapterHistoryItem);

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
