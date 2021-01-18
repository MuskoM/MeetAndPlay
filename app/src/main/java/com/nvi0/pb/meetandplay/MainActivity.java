package com.nvi0.pb.meetandplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.Fragments.GamesListFragment;
import com.nvi0.pb.meetandplay.Fragments.UserProfileFragment;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth myAuth;
    Button logout_btn;
    Button insert_btn;
    Button userProfileBtn;
    Button gameListBtn;
    TextView txt_view;
    SharedPreferences mPrefs;
    DatabaseReference messagesDbReference = FirebaseDatabase.getInstance().getReference("message");


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
        myAuth = FirebaseAuth.getInstance();
        logout_btn = findViewById(R.id.logout_btn);
        insert_btn = findViewById(R.id.insert_to_db);
        txt_view = findViewById(R.id.text_text);
        userProfileBtn = findViewById(R.id.user_profile_btn);
        gameListBtn = findViewById(R.id.list_btn);

        messagesDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String message = snapshot.getValue(String.class);
                txt_view.setText(message);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesDbReference.setValue("Sample child");
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAuth.signOut();
//                mPrefs.edit().putBoolean(LoginActivity.LOGIN_PREFERENCE,false).apply();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit)
                        .replace(R.id.fragment_container_view, UserProfileFragment.class,userDataBundle)
                        .commit();
            }
        });

        gameListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit)
                        .replace(R.id.fragment_container_view, GamesListFragment.class, null)
                        .commit();
            }
        });


    }


}