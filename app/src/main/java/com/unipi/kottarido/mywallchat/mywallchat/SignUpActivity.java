package com.unipi.kottarido.mywallchat.mywallchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText UsernameText;
    private EditText EmailText;
    private EditText PasswordText;
    private Button SignInButton;

    private String username;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize to instance tis FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        UsernameText = findViewById(R.id.UsernameEditText_SignUp);
        EmailText = findViewById(R.id.EmailEditText_SignUp);
        PasswordText = findViewById(R.id.PasswordEditText_SignUp);
        SignInButton = findViewById(R.id.SignUpButton_SignUp);

        //sto patima tou Sign Up button
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //diavazei ta stixia tou xristi apo ta paidia
                username = UsernameText.getText().toString().trim();
                email = EmailText.getText().toString().trim();
                password = PasswordText.getText().toString().trim();

                SignUpMethod(); }
        });


    }
    //create Account
    private void SignUpMethod(){
         mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING","Sing up Successful"+ task.isSuccessful());
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sign up faild", Toast.LENGTH_LONG).show();
                        }
                        else {
                            updateUI();
                            Toast.makeText(getApplicationContext(),"Account Creaeted",Toast.LENGTH_LONG).show();
                            Log.d("TESTING","Create Account");
                        }
                    }
                });
    }

    private void updateUI(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Log.d("TESTING", "Proifile updated sucsessful");
                                finish();
                        }
                    });
        }

    }
}
