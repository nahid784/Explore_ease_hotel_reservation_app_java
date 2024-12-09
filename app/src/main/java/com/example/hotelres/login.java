package com.example.hotelres;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView newUser, offlineAccess;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.setTitle("Sign In Activity");

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginemail);
        loginPassword = findViewById(R.id.loginpass);
        loginButton = findViewById(R.id.login);
        newUser = findViewById(R.id.newuser);
        offlineAccess = findViewById(R.id.offline);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (validateLogin(email, password)) {
                    // Sign in with Firebase Authentication
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(login.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();
                                        // Proceed to next activity
                                        Intent intent = new Intent(login.this, get_started.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(login.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Sign Up activity
                Intent intent = new Intent(login.this, sign_up.class);
                startActivity(intent);
            }
        });

        offlineAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(login.this, get_started.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateLogin(String email, String password) {
        // Add your own validation logic if needed
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }
}
