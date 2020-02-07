package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnTop, btnLeft, btnBottom, btnRight;

    private ArrayList<String> Secuencia = new ArrayList<String>();

    private int _iContadorSecuencia = 0;

    private boolean _bPlayMode = false;
    private boolean _bSecuenciaFinalizada = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AgregarSecuencia();

        btnTop = (Button)findViewById(R.id.btnTop);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnBottom = (Button)findViewById(R.id.btnBottom);
        btnRight = (Button)findViewById(R.id.btnRight);




    }
    public void Play(){
        _bPlayMode = true;
        MostraSecuencia();

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
            Play();
        }
        else{

            if (ValidacionSecuencia("t")==true){
                // continua
                _iContadorSecuencia++;
                if(_bSecuenciaFinalizada==true){
                    AgregarSecuencia();
                }
            }
            else{
                EndGame();
            }
        }

    }

    public void pressBtnLeft(View view){
        if(_bPlayMode==false) {
            Secuencia.add("l");
            _iContadorSecuencia= 0;
            Play();
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
                EndGame();
            }
        }
    }

    public void pressBtnBottom(View view){
        if(_bPlayMode==false) {
            Secuencia.add("b");
            _iContadorSecuencia= 0;
            Play();
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
                EndGame();
            }
        }
    }

    public void pressBtnRight(View view){
        if(_bPlayMode==false) {
            Secuencia.add("r");
            _iContadorSecuencia= 0;
            Play();
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
                EndGame();
            }
        }
    }

    // Metodo para verificar secuencia(principal para jugar)
    public boolean ValidacionSecuencia(String btnPress){
        boolean _bCorrecto;
        if(Secuencia.get(_iContadorSecuencia)==btnPress){
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
    public void EndGame(){
        Toast.makeText(this,"Has Perdido",Toast.LENGTH_LONG).show();
        Secuencia.clear();
    }

}
