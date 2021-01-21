package com.nvi0.pb.meetandplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity" ;
    public static final String LOGIN_PREFERENCE = "UserLoginPreference";
    SharedPreferences mPrefs;
    private FirebaseAuth mAuth;

    String login_string,password_string;
    Button loginUserBtn;
    Button registerUserBtn;

    EditText userCredentialsLogin, userCredentialsPassword;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            updateUI();
        }

        loginUserBtn = findViewById(R.id.login_button);
        registerUserBtn = findViewById(R.id.login_view_register_button);

        userCredentialsLogin = findViewById(R.id.login_user_input);
        userCredentialsPassword = findViewById(R.id.password_user_input);

        userCredentialsLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                login_string = s.toString();
                Log.d("LoginActivity","Login: "+ login_string);
            }
        });

        userCredentialsPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password_string = s.toString();
                Log.d("LoginActivity","Pswd: " + password_string);
            }
        });

        loginUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(login_string, password_string)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                                    prefs.edit().putBoolean(LOGIN_PREFERENCE,true).apply();
                                    updateUI();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent realmDataIntent = new Intent(LoginActivity.this,RegisterActivity.class);
             startActivity(realmDataIntent);
            }
        });

    }

    private void  updateUI(){

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


}