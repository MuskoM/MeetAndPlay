package com.nvi0.pb.meetandplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent LoginIntent = getIntent();
        Bundle LoginCredentials = LoginIntent.getBundleExtra(LoginActivity.USER_LOGIN_DETAILS);
        Log.d("MainActivity","Intent: " + LoginCredentials.getString("Login"));

    }

    void test(){

    }

}