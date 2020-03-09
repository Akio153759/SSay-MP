package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.Perfil;

public class AlertDialogIngreso {

    public interface AlertDialogIngresoCallback{
        void ResultCallback (int result);
    }
    private  AlertDialogIngresoCallback interfaz;
    public AlertDialogIngreso(final Context context, String titulo, String btnPositiveText, String msgHint, final int minChar, final int maxChar, AlertDialogIngresoCallback iface){
        interfaz = iface;

        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_ingreso);

        final TextView tvTitulo = dialog.findViewById(R.id.tvTituloIngreso);

        final Button btnPositive = dialog.findViewById(R.id.btnAceptarIngreso);
        final EditText etIngreso = dialog.findViewById(R.id.etIngresoNick);

        tvTitulo.setText(titulo);
        etIngreso.setHint(msgHint);
        btnPositive.setText(btnPositiveText);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = etIngreso.getText().toString();
                if(input.length() < minChar){
                    etIngreso.setError("El nick debe tener al menos seis caracteres");
                    return;
                }
                if(input.length() > maxChar){
                    etIngreso.setError("El nick no debe tener más de 16 caracteres");
                    return;
                }
                DatabaseReference DBRefUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
               DBRefUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                           try{
                               String _sConsultaUserExistente = dataSnapshot.child(input).child("Perfil").child("usuario").getValue().toString();
                               if(_sConsultaUserExistente.equals(input)){
                                   etIngreso.setError("El nick se encuentra en uso");
                                   return;
                               }
                           }catch (Exception ex){
                               Perfil.USER_ID = input;
                               dialog.dismiss();
                               interfaz.ResultCallback(1);
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });


            }
        });

        dialog.show();


    }
}
