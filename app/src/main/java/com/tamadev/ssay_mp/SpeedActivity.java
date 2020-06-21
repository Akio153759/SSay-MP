package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.IndexPuntaje;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.utils.AlertDialogNotification;
import com.tamadev.ssay_mp.utils.AlertDialogTwoButtons;
import com.tamadev.ssay_mp.utils.ResultCallback;
import com.tamadev.ssay_mp.utils.SendNotificationFCM;

import java.util.ArrayList;
import java.util.Map;

public class SpeedActivity extends AppCompatActivity {
    private ImageView ivBirra;
    private ObjectAnimator animatorX;
    private long animationDuration = 1500;
    private Button btnStop;
    private TextView myScore;
    private AnimatorSet animatorSet;


    private TextView scoreP1, scoreP2, scoreP3, scoreP4,
            lblPlayer1,lblPlayer2,lblPlayer3,lblPlayer4;

    private ImageView ivPlayer1, ivPlayer2,ivPlayer3, ivPlayer4,
            ivStarP1, ivStarP2, ivStarP3,ivStarP4,
            ivVida1, ivVida2, ivVida3;

    private ArrayList<String> Secuencia = new ArrayList<String>();


    private DatabaseReference DBrefPartida = FirebaseDatabase.getInstance().getReference().child("Partidas");

    private int _iIndexPartida;
    private CrearPartida _objPartida;
    private int _iPositionInListaJugadores;


    private int _iScore = 0;
    private int _iVidas = 3;
    private int _iNumRondaActual ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        _objPartida = extras.getParcelable("Partida");
        _iNumRondaActual = extras.getInt("RondaNro");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_speed);
        getSupportActionBar().hide();

        Perfil.ACTIVITY_NAVIGATION = false;
        ivBirra = findViewById(R.id.image);
        ivVida1 = findViewById(R.id.vida1);
        ivVida2 = findViewById(R.id.vida2);
        ivVida3 = findViewById(R.id.vida3);
        btnStop = findViewById(R.id.btnStop);
        myScore = findViewById(R.id.myScore);

        ivPlayer1 = findViewById(R.id.ivPlayer1);
        lblPlayer1 = findViewById(R.id.lblPlayer1);
        scoreP1 = findViewById(R.id.scoreP1);
        ivStarP1 = findViewById(R.id.ivStarP1);

        ivPlayer2 = findViewById(R.id.ivPlayer2);
        lblPlayer2 = findViewById(R.id.lblPlayer2);
        scoreP2 = findViewById(R.id.scoreP2);
        ivStarP2 = findViewById(R.id.ivStarP2);

        ivPlayer3 = findViewById(R.id.ivPlayer3);
        lblPlayer3 = findViewById(R.id.lblPlayer3);
        scoreP3 = findViewById(R.id.scoreP3);
        ivStarP3 = findViewById(R.id.ivStarP3);

        ivPlayer4 = findViewById(R.id.ivPlayer4);
        lblPlayer4 = findViewById(R.id.lblPlayer4);
        scoreP4 = findViewById(R.id.scoreP4);
        ivStarP4 = findViewById(R.id.ivStarP4);

        ivPlayer1.setVisibility(View.INVISIBLE);
        ivPlayer2.setVisibility(View.INVISIBLE);
        ivPlayer3.setVisibility(View.INVISIBLE);
        ivPlayer4.setVisibility(View.INVISIBLE);

        lblPlayer1.setVisibility(View.INVISIBLE);
        lblPlayer2.setVisibility(View.INVISIBLE);
        lblPlayer3.setVisibility(View.INVISIBLE);
        lblPlayer4.setVisibility(View.INVISIBLE);

        scoreP1.setVisibility(View.INVISIBLE);
        scoreP2.setVisibility(View.INVISIBLE);
        scoreP3.setVisibility(View.INVISIBLE);
        scoreP4.setVisibility(View.INVISIBLE);

        ivStarP1.setVisibility(View.INVISIBLE);
        ivStarP2.setVisibility(View.INVISIBLE);
        ivStarP3.setVisibility(View.INVISIBLE);
        ivStarP4.setVisibility(View.INVISIBLE);

        int _iPlayerIndex = 0;
        for(Jugador _objJugador: _objPartida.getJugadores()){
            switch (_iPlayerIndex){
                case 0:
                    Picasso.with(SpeedActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer1);
                    ivPlayer1.setVisibility(View.VISIBLE);
                    lblPlayer1.setText(_objJugador.getUser());
                    lblPlayer1.setVisibility(View.VISIBLE);
                    ivStarP1.setVisibility(View.VISIBLE);
                    scoreP1.setVisibility(View.VISIBLE);

                    scoreP1.setText(String.valueOf(_objJugador.getPuntos()));



                    break;
                case 1:
                    Picasso.with(SpeedActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer2);
                    ivPlayer2.setVisibility(View.VISIBLE);
                    lblPlayer2.setText(_objJugador.getUser());
                    lblPlayer2.setVisibility(View.VISIBLE);
                    ivStarP2.setVisibility(View.VISIBLE);
                    scoreP2.setVisibility(View.VISIBLE);

                    scoreP2.setText(String.valueOf(_objJugador.getPuntos()));




                    break;
                case 2:
                    Picasso.with(SpeedActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer3);
                    ivPlayer3.setVisibility(View.VISIBLE);
                    lblPlayer3.setText(_objJugador.getUser());
                    lblPlayer3.setVisibility(View.VISIBLE);

                    ivStarP3.setVisibility(View.VISIBLE);
                    scoreP3.setVisibility(View.VISIBLE);

                    scoreP3.setText(String.valueOf(_objJugador.getPuntos()));


                    break;
                case 3:
                    Picasso.with(SpeedActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer4);
                    ivPlayer4.setVisibility(View.VISIBLE);
                    lblPlayer4.setText(_objJugador.getUser());
                    lblPlayer4.setVisibility(View.VISIBLE);
                    ivStarP4.setVisibility(View.VISIBLE);
                    scoreP4.setVisibility(View.VISIBLE);

                    scoreP4.setText(String.valueOf(_objJugador.getPuntos()));

                    break;
            }
            _iPlayerIndex++;
        }

        for(int i = 0; i<_objPartida.getJugadores().size();i++)
        {
            if (_objPartida.getJugadores().get(i).getUser().equals(Perfil.USER_ID)){
                _iPositionInListaJugadores = i;
            }
        }
        new AlertDialogTwoButtons(SpeedActivity.this, "Información", "Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)", "Aceptar", "Cancelar", new AlertDialogTwoButtons.AlertDialogTwoButtonsCallback() {
            @Override
            public void ResultCallback(int Result) {
                switch (Result){
                    case 0:
                        Perfil.ACTIVITY_NAVIGATION = true;
                        Intent i = new Intent(SpeedActivity.this,InicioActivity.class);
                        startActivity(i);
                        finish();

                        break;
                    case 1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animate();
                            }
                        },1000);

                        break;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!animatorSet.isPaused()){
                    animatorX.pause();
                    btnStop.setEnabled(false);
                    if((float)animatorX.getAnimatedValue()>200 && (float)animatorX.getAnimatedValue()<800){
                        _iScore += 10;
                        myScore.setText(String.valueOf(_iScore));
                        animationDuration -= 10;
                        animatorX.setDuration(animationDuration);

                    }
                    else{
                        PierdeVida();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnStop.setEnabled(true);
                            animatorX.end();
                        }
                    },1000);
                }
                else {

                }
            }
        });
    }

    private void animate(){
        animatorX = ObjectAnimator.ofFloat(ivBirra,"x",1500);
        animatorX.setDuration(animationDuration);
        animatorX.setFloatValues(-400,1200);
        animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animatorSet.start();
    }

    private void PierdeVida(){
        _iVidas -= 1;
        if(_iVidas > 0){
            if(_iVidas < 3){
                ivVida3.setVisibility(View.INVISIBLE);
            }
            if(_iVidas < 2){
                ivVida2.setVisibility(View.INVISIBLE);
            }

            new AlertDialogNotification(SpeedActivity.this, "Has fallado", "Te quedan " + _iVidas + " vida/as", "Jugar", new ResultCallback() {
                @Override
                public void ResultCallbackDialog(int Result) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate();
                        }
                    },1000);
                }
            });

        }
        else{
            ivVida1.setVisibility(View.INVISIBLE);
            FinalizarTurno(_iScore);
        }
    }

    private void FinalizarTurno(int RESULT){


        // Verifico si mi puntaje maximo se superó

        if(RESULT > Perfil.MAX_SCORE){
            Perfil.MAX_SCORE = RESULT;
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("maxScore").setValue(Perfil.MAX_SCORE);
        }

        _objPartida.getRondas().get(_iNumRondaActual).getScore().put(Perfil.USER_ID,RESULT);

        // Con esto verifico si queda alguien por jugar o si la ronda tiene que dar la vuelta
        // ----------------
        int _iPosicionProximoJugador;
        if(_iPositionInListaJugadores < _objPartida.getJugadores().size() - 1) {
            _iPosicionProximoJugador = _iPositionInListaJugadores +1;
            do{
                if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){

                    _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                    DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

                    //Aca le envio la notificacion al proximo jugador que le toca
                    DatabaseReference DBrefProxJugador = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser()).child("Perfil");
                    DBrefProxJugador.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String _sTokenJugador = dataSnapshot.child("userFCM").getValue().toString();
                            SendNotificationFCM sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion(),"Es tu turno para jugar. Demuestra quien manda!!",_sTokenJugador);
                            sendNotificationFCM.execute();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    new AlertDialogNotification(SpeedActivity.this, "Información", "Tu turno ha finalizado!\nDebes esperar a tu rival", "Continuar", new ResultCallback() {
                        @Override
                        public void ResultCallbackDialog(int Result) {
                            Perfil.ACTIVITY_NAVIGATION = true;
                            Intent i = new Intent(SpeedActivity.this, InicioActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                    return;
                }
                else{
                    _iPosicionProximoJugador++;
                }
            }while (_iPosicionProximoJugador < _objPartida.getJugadores().size() - 1);

        }
        // -------------
        // finalizo ronda
        _objPartida.getRondas().get(_iNumRondaActual).setFinalizada(true);
        // Sumo el puntaje obtenido de todos en esta ronda al puntaje global de la partida
        for(Map.Entry<String,Integer> index: _objPartida.getRondas().get(_iNumRondaActual).getScore().entrySet()){
            int _iIndex = 0;
            for(int i = 0; i < _objPartida.getJugadores().size(); i++){
                if(_objPartida.getJugadores().get(i).getUser().equals(index.getKey())){
                    _iIndex = i;
                    break;
                }
            }
            _objPartida.getJugadores().get(_iIndex).setPuntos(_objPartida.getJugadores().get(_iIndex).getPuntos() + index.getValue());
        }
        // Checkeo si ya termino la partida.. y colocar los estados correspondientes
        // segun la cantidad de jugadores que sean
        if(_iNumRondaActual == _objPartida.getRondas().size()-1){
            _objPartida.setEstado(0);
            ArrayList<IndexPuntaje> _lIndexPuntaje = new ArrayList<>();
            for(int i = 0; i < _objPartida.getJugadores().size();i++){
                _lIndexPuntaje.add(new IndexPuntaje(i,_objPartida.getJugadores().get(i).getPuntos()));
            }

            int _iIndexA = 0;
            int _iIndexB = 1;
            int _iCounter = 1;

            while (_iCounter <= _lIndexPuntaje.size()) {
                while (_iIndexB < _lIndexPuntaje.size()) {
                    if(_lIndexPuntaje.get(_iIndexB).getPuntaje()>_lIndexPuntaje.get(_iIndexA).getPuntaje()){
                        IndexPuntaje _objAux = _lIndexPuntaje.get(_iIndexA);
                        _lIndexPuntaje.set(_iIndexA,_lIndexPuntaje.get(_iIndexB));
                        _lIndexPuntaje.set(_iIndexB,_objAux);
                    }
                    _iIndexA++;
                    _iIndexB++;
                }

                _iCounter++;
                _iIndexA = 0;
                _iIndexB = 1;
            }
            String _sMiPuesto = "";
            for(int i = 0; i < _lIndexPuntaje.size(); i++){
                int _iPosicion =i+1;
                final String _sStatus = "3" + _objPartida.getCantidadJugadores() + _iPosicion;
                _objPartida.getJugadores().get(_lIndexPuntaje.get(i).getIndex()).setEstado(Integer.parseInt(_sStatus));
                if(_objPartida.getJugadores().get(_lIndexPuntaje.get(i).getIndex()).getUser().equals(Perfil.USER_ID)){
                    _sMiPuesto = String.valueOf(_iPosicion);
                }

                final DatabaseReference _objAux = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_objPartida.getJugadores().get(_lIndexPuntaje.get(i).getIndex()).getUser()).child("Perfil");
                _objAux.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Me traigo la cantidad de partidas jugadas para sumarle una
                        int _iTotalPartidasPrev = Integer.parseInt(dataSnapshot.child("partidasJugadas").getValue().toString());
                        _objAux.child("partidasJugadas").setValue(_iTotalPartidasPrev + 1);

                        //Me traigo la cantidad de partidas que salió en esa posición para sumarle una
                        int _iCantPosicionPrev;
                        SendNotificationFCM sendNotificationFCM =  null;

                        // Consulto con que termina la variable que declaré arriba como final, ya que el último número me indica la posición en la que terminó
                        if(_sStatus.endsWith("4")){
                            _iCantPosicionPrev = Integer.parseInt(dataSnapshot.child("partidasCuartoPuesto").getValue().toString());
                            _objAux.child("partidasCuartoPuesto").setValue(_iCantPosicionPrev +1);
                            sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion() + " finalizada", "Has quedado en 4to lugar :(. Ingresa al historial en tu perfil para ver los detalles!", dataSnapshot.child("userFCM").getValue().toString());
                        }
                        else if(_sStatus.endsWith("3")){
                            _iCantPosicionPrev = Integer.parseInt(dataSnapshot.child("partidasTercerPuesto").getValue().toString());
                            _objAux.child("partidasTercerPuesto").setValue(_iCantPosicionPrev +1);
                            sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion() + " finalizada", "Has quedado en 3er lugar. Ingresa al historial en tu perfil para ver los detalles!", dataSnapshot.child("userFCM").getValue().toString());
                        }
                        else if(_sStatus.endsWith("2")){
                            _iCantPosicionPrev = Integer.parseInt(dataSnapshot.child("partidasSegundoPuesto").getValue().toString());
                            _objAux.child("partidasSegundoPuesto").setValue(_iCantPosicionPrev +1);
                            sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion() + " finalizada", "Casi! Has quedado en 2do lugar, bien hecho! Ingresa al historial en tu perfil para ver los detalles!", dataSnapshot.child("userFCM").getValue().toString());
                        }
                        else{
                            _iCantPosicionPrev = Integer.parseInt(dataSnapshot.child("partidasPrimerPuesto").getValue().toString());
                            _objAux.child("partidasPrimerPuesto").setValue(_iCantPosicionPrev +1);
                            sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion() + " finalizada", "Felicitaciones :D !! Has obtenido el 1er puesto!! Ingresa al historial en tu perfil para ver los detalles!", dataSnapshot.child("userFCM").getValue().toString());
                        }

                        sendNotificationFCM.execute();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);


            new AlertDialogNotification(SpeedActivity.this, "Información", "Partida Finalizada!\nHas finalizado en " + _sMiPuesto + "° puesto", "Continuar", new ResultCallback() {
                @Override
                public void ResultCallbackDialog(int Result) {
                    Perfil.ACTIVITY_NAVIGATION = true;
                    Intent i = new Intent(SpeedActivity.this, InicioActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            return;
        }

        // Aca doy vuelta la ronda de turnos y busco el proximo participante a jugar (Al proximo que le toque, con las validaciones anteriormente
        // hechas, se va a encargar de crear la ronda como tal)
        _iPosicionProximoJugador = 0;
        while (_iPosicionProximoJugador != _iPositionInListaJugadores){
            if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){
                _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

                //Aca le envio la notificacion al proximo jugador que le toca
                DatabaseReference DBrefProxJugador = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser()).child("Perfil");
                DBrefProxJugador.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String _sTokenJugador = dataSnapshot.child("userFCM").getValue().toString();
                        SendNotificationFCM sendNotificationFCM = new SendNotificationFCM("Partida de " + _objPartida.getAnfitrion(),"Es tu turno para jugar. Demuestra quien manda!!",_sTokenJugador);
                        sendNotificationFCM.execute();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                new AlertDialogNotification(SpeedActivity.this, "Información", "Tu turno ha finalizado!\nDebes esperar a tu rival", "Continuar", new ResultCallback() {
                    @Override
                    public void ResultCallbackDialog(int Result) {
                        Perfil.ACTIVITY_NAVIGATION = true;
                        Intent i = new Intent(SpeedActivity.this, InicioActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

                return;
            }
            else {
                _iPosicionProximoJugador++;
            }
        }


    }
}
