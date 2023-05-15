package com.sanapplications.devreporttrack.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanapplications.devreporttrack.Adapters.MyAdapter;
import com.sanapplications.devreporttrack.Models.DataModel;
import com.sanapplications.devreporttrack.R;
import com.sanapplications.devreporttrack.Activities.UploadActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    View view;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataModel> dataList;
    MyAdapter adapter;
    //SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Today's Work Report");

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        //searchView = view.findViewById(R.id.search);
       // searchView.clearFocus();
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

        //CollectionReference tutorialsRef = db.collection("UsersReport");
        // TO GET DATA, ONLY CONSOLE
//        db.collection("Android Tutorials").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot querySnapshot) {
//                        for (QueryDocumentSnapshot document : querySnapshot) {
//                            Log.d("Firestore", document.getId() + " => " + document.getData());
//                        }
//                        dialog.dismiss();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });




        dialog.show();

        ListenerRegistration eventListener = db.collection("UsersReport").document(
               FirebaseAuth.getInstance().getCurrentUser().getUid()
               // "SANCHITDANG"
        ).collection("Reports").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                dataList.clear();

                for (QueryDocumentSnapshot document : querySnapshot) {
                    DataModel dataModel = document.toObject(DataModel.class);
                    dataModel.setKey(document.getId());
                    //dataList.add(dataClass);

                    // Only add dataClass to dataList if date is equal to "today"
                    String myCurrentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

                    if(dataModel.getDataLang()!=null) {
                        if (dataModel.getDataLang().equals(myCurrentDate)) {
                            dataList.add(dataModel);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchList(newText);
//                return true;
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }


//    public void searchList(String text){
//        ArrayList<DataClass> searchList = new ArrayList<>();
//        for (DataClass dataClass: dataList){
//            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())
//                    ||
//                    dataClass.getLang().toLowerCase().contains(text.toLowerCase())
//            ){
//                searchList.add(dataClass);
//            }
//        }
//        adapter.searchDataList(searchList);
//    }

}