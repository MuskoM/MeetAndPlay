package com.nvi0.pb.meetandplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_LOGIN_DETAILS = "com.nvi0.pb.login.USER_LOGIN_DETAILS";
    String login_string,password_string;
    Button loginUserBtn;
    Button registerUserBtn;

    EditText userCredentialsLogin, userCredentialsPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent userDetailsIntent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle userCredentialsBundle = new Bundle();
                userCredentialsBundle.putString("Login",login_string);
                userCredentialsBundle.putString("Password",password_string);
                userDetailsIntent.putExtra(USER_LOGIN_DETAILS,userCredentialsBundle);
                startActivity(userDetailsIntent);

            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}