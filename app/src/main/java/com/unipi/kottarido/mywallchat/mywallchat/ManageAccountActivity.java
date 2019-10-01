package com.unipi.kottarido.mywallchat.mywallchat;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ManageAccountActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST =1;

    private FirebaseAuth mAuth;

    private ImageView ProfileImage;
    private Button SelectImageButton;
    private Button ApplyButton;

    private Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //gia na emfanistei to back sto menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProfileImage = findViewById(R.id.ProfileImage_ManageProfile);
        SelectImageButton = findViewById(R.id.SelectImageButton_ManageAccount);
        ApplyButton = findViewById(R.id.ApplyButton_ManageAccount);

        mAuth = FirebaseAuth.getInstance();

        SelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                &&data!=null &&data.getData() != null){
            ImageUri = data.getData();

            ProfileImage.setImageURI(ImageUri);
        }
    }
}
