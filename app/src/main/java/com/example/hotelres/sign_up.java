package com.example.hotelres;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    private EditText createEmail, createPassword;
    private RadioGroup genderGroup;
    private RadioButton selectedGender;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        this.setTitle("Create New Account");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        createEmail = findViewById(R.id.createemail);
        createPassword = findViewById(R.id.createpass);
        genderGroup = findViewById(R.id.genderGroup);
        signUpButton = findViewById(R.id.signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = createEmail.getText().toString();
                final String password = createPassword.getText().toString();
                int selectedGenderId = genderGroup.getCheckedRadioButtonId();
                selectedGender = findViewById(selectedGenderId);

                if (validateSignUp(email, password)) {
                    // Create user with Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Save additional user data to Firebase Realtime Database
                                        String userId = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDb = mDatabase.child(userId);
                                        currentUserDb.child("email").setValue(email);
                                        currentUserDb.child("gender").setValue(selectedGender.getText().toString());

                                        // Navigate to Login Activity after successful sign-up
                                        Intent intent = new Intent(sign_up.this, login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(sign_up.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(sign_up.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateSignUp(String email, String password) {
        // Add your validation logic here
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                email.contains("@") && password.length() >= 6 && genderGroup.getCheckedRadioButtonId() != -1;
    }
}
