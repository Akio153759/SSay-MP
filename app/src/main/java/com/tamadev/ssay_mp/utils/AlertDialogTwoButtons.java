package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tamadev.ssay_mp.R;

import org.w3c.dom.Text;

public class AlertDialogTwoButtons {
    public interface AlertDialogTwoButtonsCallback{
        void ResultCallback(int Result);
    }
    public interface AlertDialogTwoButtonsCallbackPosition{
        void ResultCallback(int Result,int position);
    }
    private AlertDialogTwoButtonsCallback interfaz;
    private AlertDialogTwoButtonsCallbackPosition interfazPos;

    public AlertDialogTwoButtons(final Context context, String sTitulo,final String sMsg,final String sTxtBtnPtv,final String sTxtBtnNtv,AlertDialogTwoButtonsCallback iface){
        interfaz = iface;
        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_two_buttons);

        final TextView tvTitulo = (TextView)dialog.findViewById(R.id.tvTitulo);
        final TextView tvMsg = (TextView)dialog.findViewById(R.id.tvMsg);
        final Button btnPositive = (Button)dialog.findViewById(R.id.btnPositiv);
        final Button btnNegative = (Button)dialog.findViewById(R.id.btnNegativ);

        tvMsg.setText(sMsg);
        btnPositive.setText(sTxtBtnPtv);
        btnNegative.setText(sTxtBtnNtv);
        tvTitulo.setText(sTitulo);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                interfaz.ResultCallback(1);

            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                interfaz.ResultCallback(0);
            }
        });

        dialog.show();
    }

    public AlertDialogTwoButtons(final Context context, String sTitulo, final String sMsg, final String sTxtBtnPtv, final String sTxtBtnNtv, AlertDialogTwoButtonsCallbackPosition iface, final int position){
        interfazPos = iface;
        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_two_buttons);

        final TextView tvTitulo = (TextView)dialog.findViewById(R.id.tvTitulo);
        final TextView tvMsg = (TextView)dialog.findViewById(R.id.tvMsg);
        final Button btnPositive = (Button)dialog.findViewById(R.id.btnPositiv);
        final Button btnNegative = (Button)dialog.findViewById(R.id.btnNegativ);

        tvMsg.setText(sMsg);
        btnPositive.setText(sTxtBtnPtv);
        btnNegative.setText(sTxtBtnNtv);
        tvTitulo.setText(sTitulo);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                interfazPos.ResultCallback(1,position);

            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                interfazPos.ResultCallback(0,position);
            }
        });

        dialog.show();
    }
}
