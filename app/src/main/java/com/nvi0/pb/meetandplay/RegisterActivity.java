package com.nvi0.pb.meetandplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nvi0.pb.meetandplay.DataModels.UserDataModel;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("users");

    String email,password,repeated_password;
    EditText email_user_input,password_user_input;
    EditText password_repeat_input;
    Button registerBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email_user_input = findViewById(R.id.register_view_login_user_input);
        password_user_input = findViewById(R.id.register_view_password_user_input);
        registerBtn = findViewById(R.id.register_button);
        password_repeat_input = findViewById(R.id.register_view_repeat_password_user_input);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.equals(repeated_password)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserDataModel userDataModel = new UserDataModel(user.getUid(),user.getEmail());
                                        userDataModel.createProfileNameFromEmail(user.getEmail());
                                        databaseReference.child(user.getUid()).child("mail").setValue(email);
                                        databaseReference.child(user.getUid()).child("profileName").setValue(userDataModel.getProfileName());
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }else {
                    Snackbar.make(v,"Incorrect credentials", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

        email_user_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email=s.toString();
            }
        });

        password_user_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            password = s.toString();
            }
        });


        password_repeat_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repeated_password = s.toString();
            }
        });

    }

    private void  updateUI(FirebaseUser user){

        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);

    }
}