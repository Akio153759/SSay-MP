package com.tamadev.ssay_mp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.R;
import com.tamadev.ssay_mp.classes.Friends;

import database.SQLiteDB;

public class AlertDialogSearchUser{

    private String _sUser = "";
    private String _sName = "";

    public AlertDialogSearchUser(final Context context)
    {
        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.alert_dialog_add_friend);

        final EditText etUserSearch = (EditText)dialogo.findViewById(R.id.etUserSearch);
        final TextView tvResult = (TextView)dialogo.findViewById(R.id.tv_result);
        ImageButton ibtnSearch = (ImageButton) dialogo.findViewById(R.id.ibtnSearch);
        Button btnOk = (Button)dialogo.findViewById(R.id.btnOk);
        final ImageButton ibtnAdd = (ImageButton) dialogo.findViewById(R.id.ibtnAdd);






        ibtnAdd.setVisibility(View.INVISIBLE);

        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                final String _sUserSearch = etUserSearch.getText().toString();
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            try{
                                _sUser = dataSnapshot.child(_sUserSearch).child("Perfil").child("usuario").getValue().toString();
                                _sName = dataSnapshot.child(_sUserSearch).child("Perfil").child("nombre").getValue().toString();
                                tvResult.setText(_sUser);
                                ibtnAdd.setVisibility(View.VISIBLE);
                                return;
                            }
                            catch (Exception e){
                                Toast.makeText(context,"No se encontraron resultados",Toast.LENGTH_SHORT).show();
                                tvResult.setText("");
                                ibtnAdd.setVisibility(View.INVISIBLE);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDB helper = new SQLiteDB(context,"db",null,1);
                DatabaseReference DBrefUserAdd = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_sUser);
                DBrefUserAdd.child("Solicitudes").push().setValue(helper.GetUser());
                Toast.makeText(context,"Solicitud de amistad enviada a " + _sUser,Toast.LENGTH_SHORT).show();
                ibtnAdd.setVisibility(View.INVISIBLE);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
