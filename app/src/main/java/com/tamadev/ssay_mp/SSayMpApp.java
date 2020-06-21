package com.tamadev.ssay_mp;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.utils.AlertDialogNotification;
import com.tamadev.ssay_mp.utils.ResultCallback;

public class SSayMpApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }
}
