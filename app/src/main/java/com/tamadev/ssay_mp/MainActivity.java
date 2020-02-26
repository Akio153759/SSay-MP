package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        _sIDPartida = extras.getString("IDPartida");
        setContentView(R.layout.activity_main);

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
        MostraSecuencia();
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
        Toast.makeText(this, "Comienza a jugar", Toast.LENGTH_LONG).show();

        for (final String btn : Secuencia) {
            Thread x = new Thread();
            try{

                x.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            switch (btn){
                case "t":
                    CambiarAspectoBtn(btnTop,"#8EFF89","presiona el verde");
                    Thread t = new Thread();
                    try{
                        t.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    CambiarAspectoBtn(btnTop,"#137200","suelta el verde");
                    break;

                case "l":
                    CambiarAspectoBtn(btnLeft,"#FFFF89","presiona el amarillo");
                    Thread l = new Thread();
                    try{
                        l.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    CambiarAspectoBtn(btnLeft,"#584C00","suelta el amarillo");
                    break;

                case "b":
                    CambiarAspectoBtn(btnBottom,"#ADFFF8","presiona el azul");
                    Thread b = new Thread();
                    try{
                        b.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    CambiarAspectoBtn(btnBottom,"#00505E","suelta el azul");
                    break;

                case "r":
                    CambiarAspectoBtn(btnRight,"#FFADAD","presiona el rojo");
                    Thread r = new Thread();
                    try{
                        r.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    CambiarAspectoBtn(btnRight,"#770000","suelta el rojo");
                    break;

            }
        }
    }

    public void pressBtnTop(View view){
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
                Play();
                break;
        }
    }
}
