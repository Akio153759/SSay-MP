package com.tamadev.ssay_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import com.tamadev.ssay_mp.utils.tamatools;

import database.SQLiteDB;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean _bDBExistente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        String pathDB = getDatabasePath("db").toString();
        tamatools tool = new tamatools();

        _bDBExistente = tool.checkDataBase(pathDB);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(_bDBExistente){
                    Intent i = new Intent(SplashScreenActivity.this, MenuPrincipalActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplashScreenActivity.this,PantallaInicialActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);

    }
    @Override
    public void onBackPressed(){

    }


}
