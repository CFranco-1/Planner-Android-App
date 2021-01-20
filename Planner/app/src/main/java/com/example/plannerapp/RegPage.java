package com.example.plannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegPage extends AppCompatActivity {

    private EditText email = null, pass = null;
    private Button reg = null;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registration );
        FirebaseAuth mAuth;
        email = findViewById( R.id.regEmail);
        pass = findViewById( R.id.regPassword);
        reg = findViewById( R.id.create);
    }
    public void onRegister(View v)
    {
        final String em = email.getText().toString();
        final String pas = pass.getText().toString();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(em, pas)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent( RegPage.this, LoginPage.class );
                            Bundle bundle = new Bundle( );
                            bundle.putString( "username", em );
                            bundle.putString( "password", pas);
                            intent.putExtras( bundle);
                            Toast.makeText(RegPage.this, "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            startActivity( intent );


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegPage.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    public void onLogin(View v)
    {
        // Intent to login page
        Intent intent = new Intent( RegPage.this, LoginPage.class );
        startActivity( intent );

    }
}
