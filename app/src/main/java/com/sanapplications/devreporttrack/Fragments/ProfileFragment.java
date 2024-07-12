package com.sanapplications.devreporttrack.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sanapplications.devreporttrack.Activities.MainActivity;
import com.sanapplications.devreporttrack.R;
import com.sanapplications.devreporttrack.Activities.RegisterActivity;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    View view;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;
    TextView userName, emailTextView, addressTextView, phoneTextView, logoutButtonTextView, homeButtonTextView;
    ImageButton user_profile_photo;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");


        userName = view.findViewById(R.id.userName);
        emailTextView = view.findViewById(R.id.emailTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);

        logoutButtonTextView = view.findViewById(R.id.logoutButton);
        homeButtonTextView = view.findViewById(R.id.homeButton);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fauth.getCurrentUser()).getUid();

        //userID = "1ahFwe4HVgY0OojqETj429qENnC2";
        DocumentReference df = fstore.collection("USERS").document(userID);
        df.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException error) {
                assert ds != null;

                String imageUrl = ds.getString("photoUrl");
                userName.setText(ds.getString("name"));
                emailTextView.setText("Email: sanchit@gmail.com");
                addressTextView.setText(String.format("Address: %s", ds.getString("address")));
                phoneTextView.setText("Phone: 8810625566");

//                Glide.with(requireContext())
//                        .load(imageUrl)
//                        .placeholder(R.drawable.baseline_show_chart_24)
//                        .into(user_profile_photo);
            }
        });


        logoutButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });


        homeButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);

            }
        });




        return view;


    }
}