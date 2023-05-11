package com.sanapplications.devreporttrack;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

// ...



public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    View view;
    //FloatingActionButton fab;
   // ImageView imgView;
    DatabaseReference databaseReference;
    //ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        //fab = view.findViewById(R.id.fab);
        searchView = view.findViewById(R.id.search);
       // imgView = view.findViewById(R.id.recImage);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);

        builder.setView(R.layout.progress_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
        dataList = new ArrayList<>();
        adapter = new MyAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tutorialsRef = db.collection("UsersReport");

        db.collection("UsersReport").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Log.d("Firestore", document.getId() + " => " + document.getData());
                        }
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


        dialog.show();

        ListenerRegistration eventListener = db.collection("UsersReport").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Reports").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                dataList.clear();

                for (QueryDocumentSnapshot document : querySnapshot) {
                    DataClass dataClass = document.toObject(DataClass.class);
                    dataClass.setKey(document.getId());
                    dataList.add(dataClass);
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

//        imgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://firebasestorage.googleapis.com/v0/b/devreporttrack.appspot.com/o/Android%20Images%2Fmsf%3A73?alt=media&token=44647fde-ae72-46ca-bbb2-65622c25b448";
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setTitle("File Download");
//                request.setDescription("Downloading file...");
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.pdf");
//
//                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
//                long downloadId = downloadManager.enqueue(request);
//
//                // Create a broadcast receiver to listen for download completion
//                BroadcastReceiver onComplete = new BroadcastReceiver() {
//                    public void onReceive(Context context, Intent intent) {
//                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                        if (id == downloadId) {
//                            // Handle the download completion
//                            Toast.makeText(context, "Download complete!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                };
//
//                // Register the broadcast receiver
//                getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//            }
//        });


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), UploadActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;

    }


    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())
            ||
                    dataClass.getLang().toLowerCase().contains(text.toLowerCase())
            ){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }



}