package com.nvi0.pb.meetandplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nvi0.pb.meetandplay.Fragments.GamesListFragment;
import com.nvi0.pb.meetandplay.Fragments.messsenger.MessengerFragment;
import com.nvi0.pb.meetandplay.Fragments.UserProfile.UserProfileFragment;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth myAuth;
    SharedPreferences mPrefs;
    RequestQueue requestQueue;

    BottomNavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN","Activity created");
        mPrefs = getSharedPreferences(LoginActivity.LOGIN_PREFERENCE,MODE_PRIVATE);

        Bundle userDataBundle = new Bundle();
        userDataBundle.putString("User name","Mateusz");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, UserProfileFragment.class,userDataBundle)
                    .commit();
        }

        setContentView(R.layout.activity_main);

        //Utils
        myAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(this);


        //Layout btns
        navigationView = findViewById(R.id.bottomNavigationView);



        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.list_navigation_button:
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit)
                                .replace(R.id.fragment_container_view, GamesListFragment.class,userDataBundle)
                                .commit();

                        break;

                    case R.id.profile_navigation_button:
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                                .replace(R.id.fragment_container_view, UserProfileFragment.class, userDataBundle)
                                .commit();
                        break;

                    case R.id.messages_navigation_button:
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit)
                                .replace(R.id.fragment_container_view, MessengerFragment.class, userDataBundle)
                                .commit();
                        break;

                    case R.id.logout_navigation_button:
                        myAuth.signOut();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        break;

                }

                return true;
            }
        });

    }


}
