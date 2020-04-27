package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.utils.AlertDialogGamesHistory;
import com.tamadev.ssay_mp.utils.RVAdapterFriendsRanking;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNick, tvName, tvDesempMsg, tvMaxScore, tvMatchesPlayed,
                     tvFirstPlace, tvSecondPlace, tvThirdPlace, tvQuarterPlace;
    private ImageView ivProfilePhoto;
    private ProgressBar pbDesemp;
    private RecyclerView rvRanking;
    private DatabaseReference DBRefUsers;
    private int _iProgressDes;
    private LinearLayoutManager layoutManager;
    private RVAdapterFriendsRanking adapterFriendsRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Perfil.ACTIVITY_NAVIGATION = false;
        setSupportActionBar(toolbar);

        Perfil.ACTIVITY_NAVIGATION = false;

        tvNick = findViewById(R.id.tvProfileNick);
        tvName = findViewById(R.id.tvNombre);
        tvDesempMsg = findViewById(R.id.lblDesempMsg);
        tvMaxScore = findViewById(R.id.lblMaxScore);
        tvMatchesPlayed = findViewById(R.id.lblPartidasJugadas);
        tvFirstPlace = findViewById(R.id.lblFirstPlace);
        tvSecondPlace = findViewById(R.id.lblSecondPlace);
        tvThirdPlace = findViewById(R.id.lblThirdPlace);
        tvQuarterPlace = findViewById(R.id.lblQuarterPlace);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        pbDesemp = findViewById(R.id.pbDesemp);
        rvRanking = findViewById(R.id.rvRanking);

        tvNick.setText(Perfil.USER_ID);
        tvName.setText(Perfil.NAME);
        tvMaxScore.setText(Perfil.MAX_SCORE+"");
        tvMatchesPlayed.setText(Perfil.MATCHES_PLAYED+"");
        tvFirstPlace.setText(Perfil.FIRST_PLACE_GAMES+"");
        tvSecondPlace.setText(Perfil.SECOND_PLACE_GAMES+"");
        tvThirdPlace.setText(Perfil.THIRD_PLACE_GAMES+"");
        tvQuarterPlace.setText(Perfil.QUARTER_PLACE_GAMES+"");

        DBRefUsers = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        Picasso.with(PerfilActivity.this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhoto);

        _iProgressDes = getDesempenio(Perfil.MATCHES_PLAYED,
                Perfil.FIRST_PLACE_GAMES,
                Perfil.SECOND_PLACE_GAMES,
                Perfil.THIRD_PLACE_GAMES,
                Perfil.QUARTER_PLACE_GAMES);

        pbDesemp.setProgress(_iProgressDes);

        if(_iProgressDes == 0){
            tvDesempMsg.setText("Todavía no jugaste ninguna partida");
        }
        else if(_iProgressDes>=95){
            tvDesempMsg.setText("Disfrutalo porque este nivel no dura mucho!!");
        }
        else if(_iProgressDes>=90){
            tvDesempMsg.setText("Awantiaaa!!");
        }
        else if(_iProgressDes>=80){
            tvDesempMsg.setText("Juju, no te para nadie!!");
        }
        else if(_iProgressDes>=75){
            tvDesempMsg.setText("Bien ahiii!!");
        }
        else if(_iProgressDes>=60){
            tvDesempMsg.setText("Bien! pero puede ser mejor!");
        }
        else if(_iProgressDes>=50){
            tvDesempMsg.setText("Dale que se puede!");
        }
        else if(_iProgressDes>=45){
            tvDesempMsg.setText("Definitivamente podría ser mejor!!");
        }
        else if(_iProgressDes>=30){
            tvDesempMsg.setText("Falta memoria por ahí!!");
        }
        else {
            tvDesempMsg.setText("Daa!,ponete las pilas!!");
        }

        layoutManager = new LinearLayoutManager(PerfilActivity.this,LinearLayoutManager.HORIZONTAL,false);
        adapterFriendsRanking = new RVAdapterFriendsRanking(getRanking(),PerfilActivity.this);
        rvRanking.setLayoutManager(layoutManager);
        rvRanking.setAdapter(adapterFriendsRanking);
    }

    private int getDesempenio(int _iMatchesPlayed, int _iFirstPlaceGames, int _iSecondPlaceGames, int _iThirdPlaceGames, int _iQuarterPlaceGames){

        if (_iMatchesPlayed == 0)
            return 0;

        int _iPuntosRequeridos = _iMatchesPlayed * 4;

        int _iPuntosObtenidos = _iFirstPlaceGames * 4 +
                                _iSecondPlaceGames * 3 +
                                _iThirdPlaceGames * 2 +
                                _iQuarterPlaceGames;



        return(_iPuntosObtenidos*100)/_iPuntosRequeridos;
    }

    private ArrayList<UserFriendProfile> getRanking(){
        final ArrayList<UserFriendProfile> _dataListUsers = new ArrayList<>();

        DBRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Nodos) {
                for(DataSnapshot nodo: Nodos.child(Perfil.USER_ID).getChildren()){
                    switch (nodo.getKey()){
                        case "Amigos":
                            for(DataSnapshot amigo: nodo.getChildren()){
                                UserFriendProfile _objFriend = new UserFriendProfile(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("usuario").getValue().toString(),
                                        Nodos.child(amigo.getValue().toString()).
                                                child("Perfil").
                                                child("urlProfileImage").getValue().toString(),
                                        Boolean.parseBoolean(
                                                Nodos.child(amigo.getValue().toString()).
                                                        child("Perfil").
                                                        child("enLinea").getValue().toString()));
                                _objFriend.setDesempenio(getDesempenio(
                                        Integer.parseInt(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("partidasJugadas").getValue().toString()),
                                        Integer.parseInt(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("partidasPrimerPuesto").getValue().toString()),
                                        Integer.parseInt(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("partidasSegundoPuesto").getValue().toString()),
                                        Integer.parseInt(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("partidasTercerPuesto").getValue().toString()),
                                        Integer.parseInt(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("partidasCuartoPuesto").getValue().toString())));
                                _dataListUsers.add(_objFriend);
                            }
                            UserFriendProfile _objUserMe = new UserFriendProfile(Perfil.USER_ID,Perfil.URL_IMAGE_PROFILE.toString(),true);
                            _objUserMe.setDesempenio(_iProgressDes);
                            _dataListUsers.add(_objUserMe);

                            int _iIndexA = 0;
                            int _iIndexB = 1;
                            int _iCounter = 1;

                            while (_iCounter <= _dataListUsers.size()) {
                                for (int i = 0; i < _dataListUsers.size()-1; i++) {

                                    if(_dataListUsers.get(_iIndexB).getDesempenio()>_dataListUsers.get(_iIndexA).getDesempenio()){
                                        UserFriendProfile _objAuxiliar = _dataListUsers.get(_iIndexA);
                                        _dataListUsers.set(_iIndexA,_dataListUsers.get(_iIndexB));
                                        _dataListUsers.set(_iIndexB,_objAuxiliar);
                                    }
                                    _iIndexA++;
                                    _iIndexB++;
                                }
                                _iCounter+=1;
                                _iIndexA = 0;
                                _iIndexB = 1;
                            }
                            break;
                    }
                }
                adapterFriendsRanking.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return _dataListUsers;
    }

    public void ShowGamesHistory(View view){
        new AlertDialogGamesHistory(PerfilActivity.this,InicioActivity._dataListPartidasInactivas);
    }

    public void Regresar(View view){
        Perfil.ACTIVITY_NAVIGATION = true;
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        Perfil.ACTIVITY_NAVIGATION = true;
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Perfil.ONLINE && !Perfil.ACTIVITY_NAVIGATION){
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(false);
            Perfil.ONLINE = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Perfil.ONLINE){
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(true);
            Perfil.ONLINE = true;
        }
    }
}
