package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tamadev.ssay_mp.R;

public class AlertDialogNotification {

    private ResultCallback resultCallback;
    public AlertDialogNotification(final Context context, String sTitulo, final String sMsg, final String sTxtBtnPtv, ResultCallback iface) {
        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_notification);

        resultCallback = iface;

        final TextView tvTitulo = (TextView)dialog.findViewById(R.id.tvTitulo);
        final TextView tvMsg = (TextView)dialog.findViewById(R.id.tvMsg);
        final Button btnPositive = (Button)dialog.findViewById(R.id.btnPositiv);

        tvMsg.setText(sMsg);
        btnPositive.setText(sTxtBtnPtv);
        tvTitulo.setText(sTitulo);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                resultCallback.ResultCallbackDialog(1);
            }
        });
    dialog.show();
    }
}
