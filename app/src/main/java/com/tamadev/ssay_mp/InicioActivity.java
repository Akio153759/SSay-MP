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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.RequestFriend;
import com.tamadev.ssay_mp.classes.SolicitudPartida;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.inicio_fragments.FriendsFragment;
import com.tamadev.ssay_mp.inicio_fragments.GameFragment;
import com.tamadev.ssay_mp.inicio_fragments.GameHistoryFragment;
import com.tamadev.ssay_mp.utils.AlertDialogNewRequestFriend;
import com.tamadev.ssay_mp.utils.RVAdapterRequestFriend;
import com.tamadev.ssay_mp.utils.RecyclerViewAdapter;

import java.util.ArrayList;

public class InicioActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    public static DatabaseReference DBrefUsuario;
    public static ArrayList<CrearPartida> _dataListPartidas;
    public static DatabaseReference DBrefFriendRequest;
    public static RecyclerViewAdapter adapter;
    public static ArrayList<RequestFriend> _dataListNewRequestFriend = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private ImageView ivProfilePhoto, ivLvlIcon, ivProfilePhotoBar;
    private RecyclerView recyclerView;
    private ImageButton ibMore;
    private TextView tvNickName,tvNickNameBar , tvLvlTxt, tvProfileName;
    private ArrayList<SolicitudPartida> _dataListSolicitudes= new ArrayList<>();
    private ArrayList<CrearPartida> _listaSolicitudPartidas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);

        InicioActivity.DBrefFriendRequest = FirebaseDatabase.getInstance().getReference().child("Usuarios");




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
                        Intent i = new Intent(InicioActivity.this,AmigosActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.nav_game_history:
                        // Aca va al historial de partidas
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

    public void Jugar(View view){
        Intent i = new Intent(InicioActivity.this,CreacionPartida.class);
        startActivity(i);
        finish();
    }
}
