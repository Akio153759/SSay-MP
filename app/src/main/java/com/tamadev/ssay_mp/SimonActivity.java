package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import com.tamadev.ssay_mp.classes.IndexPuntaje;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.utils.AlertDialogNotification;
import com.tamadev.ssay_mp.utils.AlertDialogTwoButtons;
import com.tamadev.ssay_mp.utils.ResultCallback;
import com.tamadev.ssay_mp.utils.SendNotificationFCM;

import java.util.ArrayList;
import java.util.Map;

public class SimonActivity extends AppCompatActivity implements AlertDialogTwoButtons.AlertDialogTwoButtonsCallback {

    private Button btnTop, btnLeft, btnBottom, btnRight;

    private TextView scoreP1, scoreP2, scoreP3, scoreP4,
            lblPlayer1,lblPlayer2,lblPlayer3,lblPlayer4, myScore;

    private ImageView ivPlayer1, ivPlayer2,ivPlayer3, ivPlayer4,
            ivStarP1, ivStarP2, ivStarP3,ivStarP4,
            ivVida1, ivVida2, ivVida3;

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

        setContentView(R.layout.activity_simon);
        getSupportActionBar().hide();

        Perfil.ACTIVITY_NAVIGATION = false;

        myScore = findViewById(R.id.myScore);
        ivVida1 = findViewById(R.id.vida1);
        ivVida2 = findViewById(R.id.vida2);
        ivVida3 = findViewById(R.id.vida3);

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

        btnVerdeSound = MediaPlayer.create(SimonActivity.this,R.raw.btnverde);
        btnAmarilloSound = MediaPlayer.create(SimonActivity.this,R.raw.btnamarillo);
        btnAzulSound = MediaPlayer.create(SimonActivity.this,R.raw.btnazul);
        btnRojoSound = MediaPlayer.create(SimonActivity.this,R.raw.btnrojo);

        btnTop = (Button)findViewById(R.id.btnTop);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnBottom = (Button)findViewById(R.id.btnBottom);
        btnRight = (Button)findViewById(R.id.btnRight);




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
                    Picasso.with(SimonActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer1);
                    ivPlayer1.setVisibility(View.VISIBLE);
                    lblPlayer1.setText(_objJugador.getUser());
                    lblPlayer1.setVisibility(View.VISIBLE);
                    ivStarP1.setVisibility(View.VISIBLE);
                    scoreP1.setVisibility(View.VISIBLE);

                    scoreP1.setText(String.valueOf(_objJugador.getPuntos()));



                    break;
                case 1:
                    Picasso.with(SimonActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer2);
                    ivPlayer2.setVisibility(View.VISIBLE);
                    lblPlayer2.setText(_objJugador.getUser());
                    lblPlayer2.setVisibility(View.VISIBLE);
                    ivStarP2.setVisibility(View.VISIBLE);
                    scoreP2.setVisibility(View.VISIBLE);

                    scoreP2.setText(String.valueOf(_objJugador.getPuntos()));




                    break;
                case 2:
                    Picasso.with(SimonActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer3);
                    ivPlayer3.setVisibility(View.VISIBLE);
                    lblPlayer3.setText(_objJugador.getUser());
                    lblPlayer3.setVisibility(View.VISIBLE);

                    ivStarP3.setVisibility(View.VISIBLE);
                    scoreP3.setVisibility(View.VISIBLE);

                    scoreP3.setText(String.valueOf(_objJugador.getPuntos()));


                    break;
                case 3:
                    Picasso.with(SimonActivity.this).load(_objJugador.getUrlImageUser()).error(R.mipmap.ic_launcher).fit().centerInside().into(ivPlayer4);
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

        AddSecuencia();

        new AlertDialogTwoButtons(SimonActivity.this,"Información","Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)","Aceptar","Cancelar", SimonActivity.this);

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

                new AlertDialogTwoButtons(SimonActivity.this,"Información","Presiona Aceptar para comenzar tu turno o Cancelar para regresar al menu de partidas\n(Una vez comenzado el turno no puedes regresar o se contará como perdido)","Aceptar","Cancelar",SimonActivity.this);
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
        long timeOutButton = intervaloMuestraSecuencia/2;

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
                                        btnVerdeSound = MediaPlayer.create(SimonActivity.this,R.raw.btnverde);
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
                            },intervaloMuestraSecuencia*i + timeOutButton);
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
                                        btnAmarilloSound= MediaPlayer.create(SimonActivity.this,R.raw.btnamarillo);
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
                            },intervaloMuestraSecuencia*i + timeOutButton);

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
                                        btnAzulSound = MediaPlayer.create(SimonActivity.this,R.raw.btnazul);
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
                            },intervaloMuestraSecuencia*i + timeOutButton);


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
                                        btnRojoSound = MediaPlayer.create(SimonActivity.this,R.raw.btnrojo);
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
                            },intervaloMuestraSecuencia*i + timeOutButton);

                            break;

                    }




        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnTop.setEnabled(true);
                btnLeft.setEnabled(true);
                btnBottom.setEnabled(true);
                btnRight.setEnabled(true);
                _bPlayMode = true;
                Toast.makeText(SimonActivity.this, "Comienza a jugar", Toast.LENGTH_LONG).show();
            }
        },(intervaloMuestraSecuencia*Secuencia.size()) + 500);
    }

    public void pressBtnTop(View view){
        if(btnVerdeSound.isPlaying()){
            btnVerdeSound.stop();
            btnVerdeSound.release();
            btnVerdeSound = MediaPlayer.create(SimonActivity.this,R.raw.btnverde);
        }
        btnVerdeSound.start();
        if(!_bPlayMode) {
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
                PierdeVida();
            }
        }

    }

    public void pressBtnLeft(View view){
        if(btnAmarilloSound.isPlaying()){
            btnAmarilloSound.stop();
            btnAmarilloSound.release();
            btnAmarilloSound = MediaPlayer.create(SimonActivity.this,R.raw.btnamarillo);
        }
        btnAmarilloSound.start();
        if(!_bPlayMode) {
            Secuencia.add("l");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("l")){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada){
                    AgregarSecuencia();
                }
            }
            else{
                PierdeVida();
            }
        }
    }

    public void pressBtnBottom(View view){
        if(btnAzulSound.isPlaying()){
            btnAzulSound.stop();
            btnAzulSound.release();
            btnAzulSound = MediaPlayer.create(SimonActivity.this,R.raw.btnazul);
        }
        btnAzulSound.start();
        if(!_bPlayMode) {
            Secuencia.add("b");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("b")){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada){
                    AgregarSecuencia();
                }
            }
            else{
                PierdeVida();
            }
        }
    }

    public void pressBtnRight(View view){
        if(btnRojoSound.isPlaying()){
            btnRojoSound.stop();
            btnRojoSound.release();
            btnRojoSound = MediaPlayer.create(SimonActivity.this,R.raw.btnrojo);
        }
        btnRojoSound.start();
        if(!_bPlayMode) {
            Secuencia.add("r");
            _iContadorSecuencia= 0;
            FinalizarTurno(1);
        }
        else{

            if (ValidacionSecuencia("r")){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada){
                    AgregarSecuencia();
                }
            }
            else{
                PierdeVida();
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

    private void PierdeVida(){
        _iVidas -= 1;
        if(_iVidas > 0){
            if(_iVidas < 3){
                ivVida3.setVisibility(View.INVISIBLE);
            }
            if(_iVidas < 2){
                ivVida2.setVisibility(View.INVISIBLE);
            }
            _bPlayMode = false;
            _bSecuenciaFinalizada = false;
            _iContadorSecuencia = 0;
            new AlertDialogNotification(SimonActivity.this, "Has fallado", "Te quedan " + _iVidas + " vida/as", "Jugar", new ResultCallback() {
                @Override
                public void ResultCallbackDialog(int Result) {
                    Play();
                }
            });

        }
        else{
            ivVida1.setVisibility(View.INVISIBLE);
            FinalizarTurno(_iScore);
        }
    }

    private void FinalizarTurno(int RESULT){
        btnBottom.setEnabled(false);
        btnTop.setEnabled(false);
        btnRight.setEnabled(false);
        btnLeft.setEnabled(false);

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

                    new AlertDialogNotification(SimonActivity.this, "Información", "Tu turno ha finalizado!\nDebes esperar a tu rival", "Continuar", new ResultCallback() {
                        @Override
                        public void ResultCallbackDialog(int Result) {
                            Perfil.ACTIVITY_NAVIGATION = true;
                            Intent i = new Intent(SimonActivity.this, InicioActivity.class);
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


            new AlertDialogNotification(SimonActivity.this, "Información", "Partida Finalizada!\nHas finalizado en " + _sMiPuesto + "° puesto", "Continuar", new ResultCallback() {
                @Override
                public void ResultCallbackDialog(int Result) {
                    Perfil.ACTIVITY_NAVIGATION = true;
                    Intent i = new Intent(SimonActivity.this, InicioActivity.class);
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

                new AlertDialogNotification(SimonActivity.this, "Información", "Tu turno ha finalizado!\nDebes esperar a tu rival", "Continuar", new ResultCallback() {
                    @Override
                    public void ResultCallbackDialog(int Result) {
                        Perfil.ACTIVITY_NAVIGATION = true;
                        Intent i = new Intent(SimonActivity.this, InicioActivity.class);
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
                Perfil.ACTIVITY_NAVIGATION = true;
                Intent i = new Intent(SimonActivity.this,InicioActivity.class);
                startActivity(i);
                finish();

                break;
            case 1:
                Play();

                break;
        }
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

    @Override
    public void onBackPressed() {

    }
}
