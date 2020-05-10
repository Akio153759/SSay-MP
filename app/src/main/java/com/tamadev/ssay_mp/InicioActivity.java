package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.felipecsl.gifimageview.library.GifImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.RequestFriend;
import com.tamadev.ssay_mp.classes.SolicitudPartida;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.utils.AlertDialogNewRequestFriend;
import com.tamadev.ssay_mp.utils.RVAdapterRequestFriend;
import com.tamadev.ssay_mp.utils.RecyclerViewAdapter;
import com.tamadev.ssay_mp.utils.SendNotificationFCM;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InicioActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    public static DatabaseReference DBrefUsuario;
    private DatabaseReference DBrefProfileUser;

    public static ArrayList<CrearPartida> _dataListPartidas;
    public static ArrayList<CrearPartida> _dataListPartidasInactivas;
    public static DatabaseReference DBrefFriendRequest;
    public static RecyclerViewAdapter adapter;
    public static ArrayList<RequestFriend> _dataListNewRequestFriend = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private CircleImageView ivProfilePhoto, ivLvlIcon, ivProfilePhotoBar;
    private RecyclerView recyclerView;
    private ImageButton ibMore;
    private TextView tvNickName,tvNickNameBar , tvLvlTxt, tvProfileName;
    private ArrayList<SolicitudPartida> _dataListSolicitudes= new ArrayList<>();
    private ArrayList<CrearPartida> _listaSolicitudPartidas = new ArrayList<>();
    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Perfil.ACTIVITY_NAVIGATION = false;

        if(!Perfil.ONLINE){
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(true);
            Perfil.ONLINE = true;
        }

        DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("userFCM").setValue(FirebaseInstanceId.getInstance().getToken());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);

        InicioActivity.DBrefFriendRequest = FirebaseDatabase.getInstance().getReference().child("Usuarios");


        DBrefProfileUser = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Perfil.USER_ID).child("Perfil");

        layoutManager = new LinearLayoutManager(InicioActivity.this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        drawer = findViewById(R.id.drawer_layout);
        ivProfilePhotoBar = findViewById(R.id.ivProfilePhotoBar);
        tvNickNameBar = findViewById(R.id.tvProfileNickBar);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_new_request:
                        new AlertDialogNewRequestFriend(InicioActivity.this);
                        navigationView.setCheckedItem(-1);
                        break;
                    case R.id.nav_friends:
                        Perfil.ACTIVITY_NAVIGATION = true;
                        Intent i = new Intent(InicioActivity.this,AmigosActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.nav_game_history:
                        Perfil.ACTIVITY_NAVIGATION = true;
                        Intent u = new Intent(InicioActivity.this,PerfilActivity.class);
                        startActivity(u);
                        finish();
                        break;
                    case R.id.nav_preferences:
                        // Aca va a la pantalla de preferencias
                        break;
                    case R.id.nav_how_to_play:
                        // A definir como se hará. Muestra de pasos 1 por 1 o con un manual de ayuda
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        AlertDialogNewRequestFriend.adapterRequestFriend = new RVAdapterRequestFriend(InicioActivity.this, InicioActivity._dataListNewRequestFriend);
        getRequestsFriend();

    }

    private void getRequestsFriend(){
        DBrefFriendRequest.child(Perfil.USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodoPrinicpalUser) {
                _dataListNewRequestFriend.clear();
                for (DataSnapshot nodo: NodoPrinicpalUser.getChildren()){
                    switch (nodo.getKey()){
                        case "SolicitudesRecibidas":
                            for(DataSnapshot solicitudes: nodo.getChildren()){
                                //Por cada solicitud de amistad que tengo, recorro la lista completa de usuarios para usar esos datos y mapear el obj Request Friend(sobretodo la url)
                                for(UserFriendProfile ufp: AmigosActivity._dataListAllUsers){
                                    if(ufp.getUserID().equals(solicitudes.getValue().toString())){
                                        RequestFriend _objRequestFriend = new RequestFriend(solicitudes.getKey(),solicitudes.getValue().toString(),ufp.getUrlImageProfile());
                                        _dataListNewRequestFriend.add(_objRequestFriend);
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                }
                AlertDialogNewRequestFriend.adapterRequestFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvNickName = findViewById(R.id.tvProfileNick);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvLvlTxt = findViewById(R.id.tvLvl);

        DBrefProfileUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Perfil.MATCHES_PLAYED = Integer.parseInt(dataSnapshot.child("partidasJugadas").getValue().toString());
                Perfil.MAX_SCORE = Integer.parseInt(dataSnapshot.child("maxScore").getValue().toString());
                Perfil.FIRST_PLACE_GAMES = Integer.parseInt(dataSnapshot.child("partidasPrimerPuesto").getValue().toString());
                Perfil.SECOND_PLACE_GAMES = Integer.parseInt(dataSnapshot.child("partidasSegundoPuesto").getValue().toString());
                Perfil.THIRD_PLACE_GAMES = Integer.parseInt(dataSnapshot.child("partidasTercerPuesto").getValue().toString());
                Perfil.QUARTER_PLACE_GAMES = Integer.parseInt(dataSnapshot.child("partidasCuartoPuesto").getValue().toString());

                tvLvlTxt.setText(String.valueOf(Perfil.FIRST_PLACE_GAMES));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tvProfileName.setText(Perfil.NAME);
        tvNickName.setText(Perfil.USER_ID);
        tvNickNameBar.setText(Perfil.USER_ID);


        Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhoto);
        Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhotoBar);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    public void Jugar(View view) {
        Perfil.ACTIVITY_NAVIGATION = true;
        Intent i = new Intent(InicioActivity.this,CreacionPartida.class);
        startActivity(i);
        finish();
    }



    @Override
    protected void onPause() {
        super.onPause();

        if(Perfil.ONLINE && !Perfil.ACTIVITY_NAVIGATION){
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(false);
            Perfil.ONLINE = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!Perfil.ONLINE){
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(true);
            Perfil.ONLINE = true;
        }
    }
}
