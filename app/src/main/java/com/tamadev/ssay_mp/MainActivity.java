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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.utils.AlertDialogTwoButtons;
import com.tamadev.ssay_mp.utils.AnimatorController;

import java.util.ArrayList;

import database.SQLiteDB;

public class MainActivity extends AppCompatActivity implements AlertDialogTwoButtons.AlertDialogTwoButtonsCallback {

    private Button btnTop, btnLeft, btnBottom, btnRight;

    private ArrayList<String> Secuencia = new ArrayList<String>();

    private int _iContadorSecuencia = 0;

    private boolean _bPlayMode = false;
    private boolean _bSecuenciaFinalizada = false;
    private DatabaseReference DBrefPartida = FirebaseDatabase.getInstance().getReference().child("Partidas");

    private String _sIDPartida;
    private CrearPartida _objPartida;
    private int _iPositionInListaJugadores;

    private SQLiteDB helper = new SQLiteDB(MainActivity.this,"db",null,1);
    private long intervaloMuestraSecuencia = 800;

    private MediaPlayer btnVerdeSound, btnAmarilloSound, btnAzulSound, btnRojoSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        _sIDPartida = extras.getString("IDPartida");
        setContentView(R.layout.activity_main);

        btnVerdeSound = MediaPlayer.create(MainActivity.this,R.raw.btnverde);
        btnAmarilloSound = MediaPlayer.create(MainActivity.this,R.raw.btnamarillo);
        btnAzulSound = MediaPlayer.create(MainActivity.this,R.raw.btnazul);
        btnRojoSound = MediaPlayer.create(MainActivity.this,R.raw.btnrojo);

        btnTop = (Button)findViewById(R.id.btnTop);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnBottom = (Button)findViewById(R.id.btnBottom);
        btnRight = (Button)findViewById(R.id.btnRight);

        DBrefPartida.child(_sIDPartida).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodoPartida) {
                _objPartida = NodoPartida.getValue(CrearPartida.class);
                for(int i = 0; i<_objPartida.getJugadores().size();i++)
                {
                    if (_objPartida.getJugadores().get(i).getUser().equals(helper.GetUser())){
                        _iPositionInListaJugadores = i;
                        break;
                    }
                }
                for (DataSnapshot x: NodoPartida.child("secuencia").getChildren()){
                    Secuencia.add(x.getValue().toString());
                }
                new AlertDialogTwoButtons(MainActivity.this,"Información","Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)","Aceptar","Cancelar",MainActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
    public void Play(){
        btnTop.setEnabled(false);
        btnLeft.setEnabled(false);
        btnBottom.setEnabled(false);
        btnRight.setEnabled(false);
        MostraSecuencia();
        btnTop.setEnabled(true);
        btnLeft.setEnabled(true);
        btnBottom.setEnabled(true);
        btnRight.setEnabled(true);
        _bPlayMode = true;


    }
    public void AgregarSecuencia(){
        _bPlayMode = false;
        Toast.makeText(this,"Añade una secuencia",Toast.LENGTH_SHORT).show();
        _bSecuenciaFinalizada = false;
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
                                    btnTop.setBackgroundColor(Color.parseColor("#27D404"));
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
                                    btnTop.setBackgroundResource(R.drawable.efecto_boton_verde);
                                }
                            },intervaloMuestraSecuencia*i + 400);
                            break;

                        case "l":
                            //Presionando
                            btnLeft.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnLeft.setBackgroundColor(Color.parseColor("#C1A600"));
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
                                    btnLeft.setBackgroundResource(R.drawable.efecto_boton_amarillo);
                                }
                            },intervaloMuestraSecuencia*i + 400);

                            break;

                        case "b":
                            //Presionando
                            btnBottom.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnBottom.setBackgroundColor(Color.parseColor("#00A1BD"));
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
                                    btnBottom.setBackgroundResource(R.drawable.efecto_boton_azul);
                                }
                            },intervaloMuestraSecuencia*i + 400);


                            break;

                        case "r":
                            //Presionando
                            btnRight.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnRight.setBackgroundColor(Color.parseColor("#DC0000"));
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
                                    btnRight.setBackgroundResource(R.drawable.efecto_boton_rojo);
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
                FinalizarTurno(0);
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
                FinalizarTurno(0);
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
                FinalizarTurno(0);
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
                FinalizarTurno(0);
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
        DBrefPartida.child(_sIDPartida).child("jugadores").child(String.valueOf(_iPositionInListaJugadores)).child("estado").setValue(3);



    }

    public void FinalizarTurno(int RESULT){
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

                    Intent i = new Intent(MainActivity.this,LobbyPartidasActivity.class);
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

                Intent i = new Intent(MainActivity.this,LobbyPartidasActivity.class);
                startActivity(i);
                finish();

                return;
            }
            else {
                _iPosicionProximoJugador++;
            }
        }
    }


    @Override
    public void ResultCallback(int Result) {
        switch (Result){
            case 0:
                Intent i = new Intent(MainActivity.this,LobbyPartidasActivity.class);
                startActivity(i);
                finish();
                break;
            case 1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Play();
                    }
                },2000);

                break;
        }
    }
}
