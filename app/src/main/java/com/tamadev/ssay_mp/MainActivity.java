package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.Round;
import com.tamadev.ssay_mp.utils.AlertDialogTwoButtons;
import com.tamadev.ssay_mp.utils.AnimatorController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.SQLiteDB;

public class MainActivity extends AppCompatActivity implements AlertDialogTwoButtons.AlertDialogTwoButtonsCallback {

    private Button btnTop, btnLeft, btnBottom, btnRight;

    private TextView scoreP1, scoreP2, scoreP3, scoreP4,
            lblPlayer1,lblPlayer2,lblPlayer3,lblPlayer4, myScore;

    private ImageView ivPlayer1, ivPlayer2,ivPlayer3, ivPlayer4,
            P1VidaN1,P1VidaN2,P1VidaN3,
            P2VidaN1,P2VidaN2,P2VidaN3,
            P3VidaN1,P3VidaN2,P3VidaN3,
            P4VidaN1,P4VidaN2,P4VidaN3,
            ivStarP1, ivStarP2, ivStarP3,ivStarP4;

    private ArrayList<String> Secuencia = new ArrayList<String>();

    private int _iContadorSecuencia = 0;

    private boolean _bPlayMode = false;
    private boolean _bSecuenciaFinalizada = false;
    private DatabaseReference DBrefPartida = FirebaseDatabase.getInstance().getReference().child("Partidas");

    private int _iIndexPartida;
    private CrearPartida _objPartida;
    private int _iPositionInListaJugadores;

    private long intervaloMuestraSecuencia = 800;

    private MediaPlayer btnVerdeSound, btnAmarilloSound, btnAzulSound, btnRojoSound;
    private int _iScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        _iIndexPartida = extras.getInt("IndexPartida");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        myScore = findViewById(R.id.myScore);

        ivPlayer1 = findViewById(R.id.ivPlayer1);
        lblPlayer1 = findViewById(R.id.lblPlayer1);
        scoreP1 = findViewById(R.id.scoreP1);
        P1VidaN1 = findViewById(R.id.P1VidaN1);
        P1VidaN2 = findViewById(R.id.P1VidaN2);
        P1VidaN3 = findViewById(R.id.P1VidaN3);
        ivStarP1 = findViewById(R.id.ivStarP1);

        ivPlayer2 = findViewById(R.id.ivPlayer2);
        lblPlayer2 = findViewById(R.id.lblPlayer2);
        scoreP2 = findViewById(R.id.scoreP2);
        P2VidaN1 = findViewById(R.id.P2VidaN1);
        P2VidaN2 = findViewById(R.id.P2VidaN2);
        P2VidaN3 = findViewById(R.id.P2VidaN3);
        ivStarP2 = findViewById(R.id.ivStarP2);

        ivPlayer3 = findViewById(R.id.ivPlayer3);
        lblPlayer3 = findViewById(R.id.lblPlayer3);
        scoreP3 = findViewById(R.id.scoreP3);
        P3VidaN1 = findViewById(R.id.P3VidaN1);
        P3VidaN2 = findViewById(R.id.P3VidaN2);
        P3VidaN3 = findViewById(R.id.P3VidaN3);
        ivStarP3 = findViewById(R.id.ivStarP3);

        ivPlayer4 = findViewById(R.id.ivPlayer4);
        lblPlayer4 = findViewById(R.id.lblPlayer4);
        scoreP4 = findViewById(R.id.scoreP4);
        P4VidaN1 = findViewById(R.id.P4VidaN1);
        P4VidaN2 = findViewById(R.id.P4VidaN2);
        P4VidaN3 = findViewById(R.id.P4VidaN3);
        ivStarP4 = findViewById(R.id.ivStarP4);

        btnVerdeSound = MediaPlayer.create(MainActivity.this,R.raw.btnverde);
        btnAmarilloSound = MediaPlayer.create(MainActivity.this,R.raw.btnamarillo);
        btnAzulSound = MediaPlayer.create(MainActivity.this,R.raw.btnazul);
        btnRojoSound = MediaPlayer.create(MainActivity.this,R.raw.btnrojo);

        btnTop = (Button)findViewById(R.id.btnTop);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnBottom = (Button)findViewById(R.id.btnBottom);
        btnRight = (Button)findViewById(R.id.btnRight);

        _objPartida = InicioActivity._dataListPartidas.get(_iIndexPartida);


        ivPlayer1.setVisibility(View.INVISIBLE);
        ivPlayer2.setVisibility(View.INVISIBLE);
        ivPlayer3.setVisibility(View.INVISIBLE);
        ivPlayer4.setVisibility(View.INVISIBLE);

        lblPlayer1.setVisibility(View.INVISIBLE);
        lblPlayer2.setVisibility(View.INVISIBLE);
        lblPlayer3.setVisibility(View.INVISIBLE);
        lblPlayer4.setVisibility(View.INVISIBLE);

        P1VidaN1.setVisibility(View.INVISIBLE);
        P1VidaN2.setVisibility(View.INVISIBLE);
        P1VidaN3.setVisibility(View.INVISIBLE);

        P2VidaN1.setVisibility(View.INVISIBLE);
        P2VidaN2.setVisibility(View.INVISIBLE);
        P2VidaN3.setVisibility(View.INVISIBLE);

        P3VidaN1.setVisibility(View.INVISIBLE);
        P3VidaN2.setVisibility(View.INVISIBLE);
        P3VidaN3.setVisibility(View.INVISIBLE);

        P4VidaN1.setVisibility(View.INVISIBLE);
        P4VidaN2.setVisibility(View.INVISIBLE);
        P4VidaN3.setVisibility(View.INVISIBLE);

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
                    Picasso.with(MainActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer1);
                    ivPlayer1.setVisibility(View.VISIBLE);
                    lblPlayer1.setText(_objJugador.getUser());
                    lblPlayer1.setVisibility(View.VISIBLE);
                    P1VidaN1.setVisibility(View.VISIBLE);
                    P1VidaN2.setVisibility(View.VISIBLE);
                    P1VidaN3.setVisibility(View.VISIBLE);
                    ivStarP1.setVisibility(View.VISIBLE);
                    scoreP1.setVisibility(View.VISIBLE);

                    if(_objJugador.getVidas()==0){
                        P1VidaN1.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P1VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P1VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==1){
                        P1VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P1VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==2){
                        P1VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else {
                        // Todas las vidas en verde
                    }

                    if(_objPartida.getRondas()==null || _objPartida.getRondas().size()==0){
                        scoreP1.setText("-");
                    }
                    else if(_objPartida.getRondas().get(_objPartida.getRondas().size()-1).isFinalizada()){
                        scoreP1.setText("-");
                    }
                    else {
                        for (Map.Entry<String,Integer> player: _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().entrySet()){
                            if (player.getKey().equals(_objJugador.getUser())){
                                scoreP1.setText(String.valueOf(player.getValue()));
                            }
                            else {
                                scoreP1.setText("-");
                            }
                        }
                    }
                    break;
                case 1:
                    Picasso.with(MainActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer2);
                    ivPlayer2.setVisibility(View.VISIBLE);
                    lblPlayer2.setText(_objJugador.getUser());
                    lblPlayer2.setVisibility(View.VISIBLE);
                    P2VidaN1.setVisibility(View.VISIBLE);
                    P2VidaN2.setVisibility(View.VISIBLE);
                    P2VidaN3.setVisibility(View.VISIBLE);
                    ivStarP2.setVisibility(View.VISIBLE);
                    scoreP2.setVisibility(View.VISIBLE);

                    if(_objJugador.getVidas()==0){
                        P2VidaN1.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P2VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P2VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==1){
                        P2VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P2VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==2){
                        P2VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else {
                        // Todas las vidas en verde
                    }

                    if(_objPartida.getRondas()==null || _objPartida.getRondas().size()==0){
                        scoreP2.setText("-");
                    }
                    else if(_objPartida.getRondas().get(_objPartida.getRondas().size()-1).isFinalizada()){
                        scoreP2.setText("-");
                    }
                    else {
                        for (Map.Entry<String,Integer> player: _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().entrySet()){
                            if (player.getKey().equals(_objJugador.getUser())){
                                scoreP2.setText(String.valueOf(player.getValue()));
                            }
                            else {
                                scoreP2.setText("-");
                            }
                        }
                    }
                    break;
                case 2:
                    Picasso.with(MainActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer3);
                    ivPlayer3.setVisibility(View.VISIBLE);
                    lblPlayer3.setText(_objJugador.getUser());
                    lblPlayer3.setVisibility(View.VISIBLE);
                    P3VidaN1.setVisibility(View.VISIBLE);
                    P3VidaN2.setVisibility(View.VISIBLE);
                    P3VidaN3.setVisibility(View.VISIBLE);
                    ivStarP3.setVisibility(View.VISIBLE);
                    scoreP3.setVisibility(View.VISIBLE);

                    if(_objJugador.getVidas()==0){
                        P3VidaN1.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P3VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P3VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==1){
                        P3VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P3VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==2){
                        P3VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else {
                        // Todas las vidas en verde
                    }

                    if(_objPartida.getRondas()==null || _objPartida.getRondas().size()==0){
                        scoreP3.setText("-");
                    }
                    else if(_objPartida.getRondas().get(_objPartida.getRondas().size()-1).isFinalizada()){
                        scoreP3.setText("-");
                    }
                    else {
                        for (Map.Entry<String,Integer> player: _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().entrySet()){
                            if (player.getKey().equals(_objJugador.getUser())){
                                scoreP3.setText(String.valueOf(player.getValue()));
                            }
                            else {
                                scoreP3.setText("-");
                            }
                        }
                    }
                    break;
                case 3:
                    Picasso.with(MainActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer4);
                    ivPlayer4.setVisibility(View.VISIBLE);
                    lblPlayer4.setText(_objJugador.getUser());
                    lblPlayer4.setVisibility(View.VISIBLE);
                    P4VidaN1.setVisibility(View.VISIBLE);
                    P4VidaN2.setVisibility(View.VISIBLE);
                    P4VidaN3.setVisibility(View.VISIBLE);
                    ivStarP4.setVisibility(View.VISIBLE);
                    scoreP4.setVisibility(View.VISIBLE);

                    if(_objJugador.getVidas()==0){
                        P4VidaN1.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P4VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P4VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==1){
                        P4VidaN2.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                        P4VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else if(_objJugador.getVidas()==2){
                        P4VidaN3.setImageTintList(getResources().getColorStateList(R.color.btn_rojo_press));
                    }
                    else {
                        // Todas las vidas en verde
                    }

                    if(_objPartida.getRondas()==null || _objPartida.getRondas().size()==0){
                        scoreP4.setText("-");
                    }
                    else if(_objPartida.getRondas().get(_objPartida.getRondas().size()-1).isFinalizada()){
                        scoreP4.setText("-");
                    }
                    else {
                        for (Map.Entry<String,Integer> player: _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().entrySet()){
                            if (player.getKey().equals(_objJugador.getUser())){
                                scoreP4.setText(String.valueOf(player.getValue()));
                            }
                            else {
                                scoreP4.setText("-");
                            }
                        }
                    }
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

        AddSecuencia();

        new AlertDialogTwoButtons(MainActivity.this,"Información","Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)","Aceptar","Cancelar",MainActivity.this);

        /*

        DBrefPartida.child(_sIDPartida).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodoPartida) {
                _objPartida = NodoPartida.getValue(CrearPartida.class);
                for(int i = 0; i<_objPartida.getJugadores().size();i++)
                {
                    if (_objPartida.getJugadores().get(i).getUser().equals(Perfil.USER_ID)){
                        _iPositionInListaJugadores = i;
                    }
                }

                AddSecuencia();

                new AlertDialogTwoButtons(MainActivity.this,"Información","Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)","Aceptar","Cancelar",MainActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/





    }
    public void Play(){
        btnTop.setEnabled(false);
        btnLeft.setEnabled(false);
        btnBottom.setEnabled(false);
        btnRight.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MostraSecuencia();

            }
        },2000);

        btnTop.setEnabled(true);
        btnLeft.setEnabled(true);
        btnBottom.setEnabled(true);
        btnRight.setEnabled(true);
        _bPlayMode = true;
    }

    public void AgregarSecuencia(){
        _iScore += 1;
        myScore.setText(String.valueOf(_iScore));
        _bPlayMode = false;
        _bSecuenciaFinalizada = false;
        _iContadorSecuencia = 0;
        AddSecuencia();
        Play();
        //Toast.makeText(this,"Añade una secuencia",Toast.LENGTH_SHORT).show();
        //_bSecuenciaFinalizada = false;
    }

    public void CambiarAspectoBtn(final Button btnPresionado, final String HexColor, final String accion){
        btnPresionado.setBackgroundColor(Color.parseColor(HexColor));

        System.out.println(accion);
    }



    public void MostraSecuencia() {

        for (int i = 0; i < Secuencia.size(); i++) {
            String btn = Secuencia.get(i);
                    switch (btn){
                        case "t":
                            //Presionando
                            btnTop.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnTop.setBackgroundResource(R.drawable.fernetpress);
                                    if (btnVerdeSound.isPlaying()){
                                        btnVerdeSound.stop();
                                        btnVerdeSound.release();
                                        btnVerdeSound = MediaPlayer.create(MainActivity.this,R.raw.btnverde);
                                    }
                                    btnVerdeSound.start();
                                }
                            },intervaloMuestraSecuencia*i);
                            //Soltando
                            btnTop.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnTop.setBackgroundResource(R.drawable.btn_fernet);
                                }
                            },intervaloMuestraSecuencia*i + 400);
                            break;

                        case "l":
                            //Presionando
                            btnLeft.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnLeft.setBackgroundResource(R.drawable.cervezapress);
                                    if(btnAmarilloSound.isPlaying()){
                                        btnAmarilloSound.stop();
                                        btnAmarilloSound.release();
                                        btnAmarilloSound= MediaPlayer.create(MainActivity.this,R.raw.btnamarillo);
                                    }
                                    btnAmarilloSound.start();
                                }
                            },intervaloMuestraSecuencia*i);
                            //Soltando
                            btnLeft.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnLeft.setBackgroundResource(R.drawable.btn_cerveza);
                                }
                            },intervaloMuestraSecuencia*i + 400);

                            break;

                        case "b":
                            //Presionando
                            btnBottom.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnBottom.setBackgroundResource(R.drawable.manaospress);
                                    if(btnAzulSound.isPlaying()){
                                        btnAzulSound.stop();
                                        btnAzulSound.release();
                                        btnAzulSound = MediaPlayer.create(MainActivity.this,R.raw.btnazul);
                                    }
                                    btnAzulSound.start();
                                }
                            },intervaloMuestraSecuencia*i);
                            //Soltando
                            btnBottom.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnBottom.setBackgroundResource(R.drawable.btn_manaos);
                                }
                            },intervaloMuestraSecuencia*i + 400);


                            break;

                        case "r":
                            //Presionando
                            btnRight.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnRight.setBackgroundResource(R.drawable.vinoderechopress);
                                    if(btnRojoSound.isPlaying()){
                                        btnRojoSound.stop();
                                        btnRojoSound.release();
                                        btnRojoSound = MediaPlayer.create(MainActivity.this,R.raw.btnrojo);
                                    }
                                    btnRojoSound.start();
                                }
                            },intervaloMuestraSecuencia*i);
                            //Soltando
                            btnRight.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnRight.setBackgroundResource(R.drawable.btn_vino);
                                }
                            },intervaloMuestraSecuencia*i + 400);

                            break;

                    }




        }
        Toast.makeText(this, "Comienza a jugar", Toast.LENGTH_LONG).show();
    }

    public void pressBtnTop(View view){
        if(btnVerdeSound.isPlaying()){
            btnVerdeSound.stop();
            btnVerdeSound.release();
            btnVerdeSound = MediaPlayer.create(MainActivity.this,R.raw.btnverde);
        }
        btnVerdeSound.start();
        if(_bPlayMode==false) {
            Secuencia.add("t");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("t")){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada){
                    AgregarSecuencia();
                }
            }
            else{
                FinalizarTurno(_iScore);
            }
        }

    }

    public void pressBtnLeft(View view){
        if(btnAmarilloSound.isPlaying()){
            btnAmarilloSound.stop();
            btnAmarilloSound.release();
            btnAmarilloSound = MediaPlayer.create(MainActivity.this,R.raw.btnamarillo);
        }
        btnAmarilloSound.start();
        if(_bPlayMode==false) {
            Secuencia.add("l");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("l")==true){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada==true){
                    AgregarSecuencia();
                }
            }
            else{
                FinalizarTurno(_iScore);
            }
        }
    }

    public void pressBtnBottom(View view){
        if(btnAzulSound.isPlaying()){
            btnAzulSound.stop();
            btnAzulSound.release();
            btnAzulSound = MediaPlayer.create(MainActivity.this,R.raw.btnazul);
        }
        btnAzulSound.start();
        if(_bPlayMode==false) {
            Secuencia.add("b");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("b")==true){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada==true){
                    AgregarSecuencia();
                }
            }
            else{
                FinalizarTurno(_iScore);
            }
        }
    }

    public void pressBtnRight(View view){
        if(btnRojoSound.isPlaying()){
            btnRojoSound.stop();
            btnRojoSound.release();
            btnRojoSound = MediaPlayer.create(MainActivity.this,R.raw.btnrojo);
        }
        btnRojoSound.start();
        if(_bPlayMode==false) {
            Secuencia.add("r");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("r")==true){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada==true){
                    AgregarSecuencia();
                }
            }
            else{
                FinalizarTurno(_iScore);
            }
        }
    }

    // Metodo para verificar secuencia(principal para jugar)
    public boolean ValidacionSecuencia(String btnPress){
        boolean _bCorrecto;
        if(Secuencia.get(_iContadorSecuencia).equals(btnPress)){
            if(Secuencia.size() -1 ==_iContadorSecuencia){
                _bSecuenciaFinalizada = true;
            }
            _bCorrecto = true;
        }
        else{
            _bCorrecto = false;
        }
        return _bCorrecto;
    }
    public void GameOver(){
        Toast.makeText(this,"Has Perdido",Toast.LENGTH_LONG).show();
        DBrefPartida.child(_objPartida.getID()).child("jugadores").child(String.valueOf(_iPositionInListaJugadores)).child("estado").setValue(3);



    }

    private void FinalizarTurno(int RESULT){
        btnBottom.setEnabled(false);
        btnTop.setEnabled(false);
        btnRight.setEnabled(false);
        btnLeft.setEnabled(false);
        // Primero verifico si nunca se ha creado la primera ronda
        if(_objPartida.getRondas()==null || _objPartida.getRondas().size()==0){
            _objPartida.setRondas(new ArrayList<Round>());
            HashMap<String,Integer> _hmMiScore = new HashMap<>();
            _hmMiScore.put(Perfil.USER_ID,RESULT);
            Round _objRound = new Round();
            _objRound.setFinalizada(false);
            _objRound.setScore(_hmMiScore);
            _objPartida.getRondas().add(_objRound);

            int _iPosicionProximoJugador;
            if(_iPositionInListaJugadores < _objPartida.getJugadores().size() - 1) {
                _iPosicionProximoJugador = _iPositionInListaJugadores +1;
                do{
                    if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){

                        _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());
                    }
                    else{
                        _iPosicionProximoJugador++;
                    }
                }while (_iPosicionProximoJugador < _objPartida.getJugadores().size() - 1);

            }

            DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

            Intent i = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(i);
            finish();


        }
        // Si no, verifico si la ultima ronda creada ya ha finalizado
        else if(_objPartida.getRondas().get(_objPartida.getRondas().size()-1).isFinalizada()){
            HashMap<String,Integer> _hmMiScore = new HashMap<>();
            _hmMiScore.put(Perfil.USER_ID,RESULT);
            Round _objRound = new Round();
            _objRound.setFinalizada(false);
            _objRound.setScore(_hmMiScore);
            _objPartida.getRondas().add(_objRound);

            int _iPosicionProximoJugador;
            if(_iPositionInListaJugadores < _objPartida.getJugadores().size() - 1) {
                _iPosicionProximoJugador = _iPositionInListaJugadores +1;
                do{
                    if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){

                        _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());
                    }
                    else{
                        _iPosicionProximoJugador++;
                    }
                }while (_iPosicionProximoJugador < _objPartida.getJugadores().size() - 1);

            }

            DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

            Intent i = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(i);
            finish();
        }
        // si no ha finalizado coloco mi puntaje en ese ronda
        else {
            _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().put(Perfil.USER_ID,RESULT);

            // Con esto verifico si queda alguien por jugar o si la ronda tiene que dar la vuelta
            // ----------------
            int _iPosicionProximoJugador;
            if(_iPositionInListaJugadores < _objPartida.getJugadores().size() - 1) {
                _iPosicionProximoJugador = _iPositionInListaJugadores +1;
                do{
                    if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){

                        _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                        DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

                        Intent i = new Intent(MainActivity.this, InicioActivity.class);
                        startActivity(i);
                        finish();

                        return;
                    }
                    else{
                        _iPosicionProximoJugador++;
                    }
                }while (_iPosicionProximoJugador < _objPartida.getJugadores().size() - 1);

            }
            // -------------

            // La ronda tiene que dar la vuelta, por lo tanto finalizo la misma..
            _objPartida.getRondas().get(_objPartida.getRondas().size()-1).setFinalizada(true);

            ArrayList<Integer> _lJugadoresRestantes = new ArrayList<>();
            String _sUserMin = "";
            int _iScoreMin = 0;
            boolean _bFirst = true;
            //Comienzo el checkeo de puntajes para ver quien fue el perdedor de la RONDA
            for(Map.Entry<String,Integer> index: _objPartida.getRondas().get(_objPartida.getRondas().size()-1).getScore().entrySet()){
                if(_bFirst){
                    _bFirst = false;
                    _sUserMin = index.getKey();
                    _iScoreMin = index.getValue();
                }
                else {
                    if(index.getValue()<_iScoreMin){
                        _sUserMin = index.getKey();
                        _iScoreMin = index.getValue();
                    }
                }

            }
            // Aca recorro los jugadores para encontrar el index del user del menor score
            for(int i = 0; i < _objPartida.getJugadores().size(); i++){
                if(_objPartida.getJugadores().get(i).getUser().equals(_sUserMin)){
                    // Encontrado, le resto una vida y checkeo si no quedo fuera
                    _objPartida.getJugadores().get(i).setVidas(_objPartida.getJugadores().get(i).getVidas()-1);
                    if(_objPartida.getJugadores().get(i).getVidas()==0){
                        _objPartida.getJugadores().get(i).setEstado(3);

                    }
                }
                // mientras recorro los jugadores los voy añadiendo a una lista temporal, para saber los que siguen participando
                if(_objPartida.getJugadores().get(i).getEstado()!=2 && _objPartida.getJugadores().get(i).getEstado()!=3){
                    _lJugadoresRestantes.add(i);
                }
            }
            // Checkeo si hay ganador
            if(_lJugadoresRestantes.size()==1){
                _objPartida.getJugadores().get(_lJugadoresRestantes.get(0)).setEstado(4);
                _objPartida.setEstado(0);

                DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

                Intent i = new Intent(MainActivity.this,InicioActivity.class);
                startActivity(i);
                finish();

                return;
            }

            // Aca doy vuelta la ronda de turnos y busco el proximo participante a jugar (Al proximo que le toque, con las validaciones anteriormente
            // hechas, se va a encargar de crear la ronda como tal)
            _iPosicionProximoJugador = 0;
            while (_iPosicionProximoJugador != _iPositionInListaJugadores){
                if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){
                    _objPartida.setProximoJugador(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                    DBrefPartida.child(_objPartida.getID()).setValue(_objPartida);

                    Intent i = new Intent(MainActivity.this,InicioActivity.class);
                    startActivity(i);
                    finish();

                    return;
                }
                else {
                    _iPosicionProximoJugador++;
                }
            }


        }

        /*
        switch (RESULT){
            case 0:
                DBrefPartida.child(_sIDPartida).child("jugadores").child(String.valueOf(_iPositionInListaJugadores)).child("estado").setValue(3);
                ArrayList<Integer> _lJugadoresRestantes = new ArrayList<>();
                // Comprobación si ya hay un ganador en caso que yo pierda el turno
                for(int i = 0; i<_objPartida.getJugadores().size();i++){
                    if(i==_iPositionInListaJugadores){
                        continue;
                    }
                    if(_objPartida.getJugadores().get(i).getEstado()!=2 && _objPartida.getJugadores().get(i).getEstado()!=3){
                        _lJugadoresRestantes.add(i);
                    }
                }
                if(_lJugadoresRestantes.size()==1){
                    DBrefPartida.child(_sIDPartida).child("jugadores").child(String.valueOf(_lJugadoresRestantes.get(0))).child("estado").setValue(4);
                    DBrefPartida.child(_sIDPartida).child("estado").setValue(0);
                }
                break;
            case 1:
                DBrefPartida.child(_sIDPartida).child("secuencia").setValue(Secuencia);
                break;
        }
        int _iPosicionProximoJugador;
        if(_iPositionInListaJugadores < _objPartida.getJugadores().size() - 1) {
            _iPosicionProximoJugador = _iPositionInListaJugadores +1;
            do{
                if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){
                    DBrefPartida.child(_sIDPartida).child("proximoJugador").setValue(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                    Intent i = new Intent(MainActivity.this, InicioActivity.class);
                    startActivity(i);
                    finish();

                    return;
                }
                else{
                    _iPosicionProximoJugador++;
                }
            }while (_iPosicionProximoJugador < _objPartida.getJugadores().size() - 1);

        }
        _iPosicionProximoJugador = 0;
        while (_iPosicionProximoJugador != _iPositionInListaJugadores){
            if(_objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==0 || _objPartida.getJugadores().get(_iPosicionProximoJugador).getEstado()==1){
                DBrefPartida.child(_sIDPartida).child("proximoJugador").setValue(_objPartida.getJugadores().get(_iPosicionProximoJugador).getUser());

                Intent i = new Intent(MainActivity.this,InicioActivity.class);
                startActivity(i);
                finish();

                return;
            }
            else {
                _iPosicionProximoJugador++;
            }
        }
        */
    }

    private void AddSecuencia(){
        int rand = (int) (Math.random() * 8) + 1;
        if(rand==1 ||rand==8){
            Secuencia.add("t");
        }
        else if(rand==2 ||rand==7){
            Secuencia.add("l");
        }
        else if(rand==3 ||rand==6){
            Secuencia.add("b");
        }
        else {
            Secuencia.add("r");
        }
    }


    @Override
    public void ResultCallback(int Result) {
        switch (Result){
            case 0:
                Intent i = new Intent(MainActivity.this,InicioActivity.class);
                startActivity(i);
                finish();
                break;
            case 1:
                Play();

                break;
        }
    }
}
