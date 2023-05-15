package com.sanapplications.devreporttrack.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sanapplications.devreporttrack.R;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;
    TextView userName, emailTextView, addressTextView, phoneTextView, logoutButtonTextView, homeButtonTextView;
    ImageButton user_profile_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.userName);
        emailTextView = findViewById(R.id.emailTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);

        logoutButtonTextView = findViewById(R.id.logoutButton);
        homeButtonTextView = findViewById(R.id.homeButton);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fauth.getCurrentUser()).getUid();

        DocumentReference df = fstore.collection("USERS").document(userID);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException error) {
                assert ds != null;

                String imageUrl = ds.getString("photoUrl");
                userName.setText(ds.getString("name"));
                addressTextView.setText(String.format("Address: %s", ds.getString("address")));

                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_show_chart_24)
                        .into(user_profile_photo);
            }
        });


        logoutButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });


        homeButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }


}