package com.unipi.kottarido.mywallchat.mywallchat;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    //orizw ena instance tis FirebaseAuth
    private FirebaseAuth mAuth;

    private EditText EmailText;
    private EditText PasswordText;
    private Button SignInButton;
    private TextView SignUpTextView;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //initialize to instance tis FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //initialize ta instances ton views
        EmailText = findViewById(R.id.EmailEditText_SignIn);
        PasswordText = findViewById(R.id.PasswordEditText_SignIn);
        SignInButton = findViewById(R.id.SignInButton_SignIn);
        SignUpTextView = findViewById(R.id.SignUpTextView_SignIn);

        //check an o user einai sindedemenos diladi currentUser != null
        if(mAuth.getCurrentUser() != null){
            //tote paei sto mainActivity kai termatizi auto to Activity
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //diavazei to email kai to password apo ta antistixa paidia
                email = EmailText.getText().toString();
                password = PasswordText.getText().toString();

                SignInMethod();
            }
        });


        //event gia na se paei sto Sign Up activity
        SignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });

    }

    private void SignInMethod(){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TESTING", "signInWithEmail:success");
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TESTING", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
