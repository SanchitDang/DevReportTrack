package com.sanapplications.devreporttrack.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanapplications.devreporttrack.Adapters.MyAdapterHistory;
import com.sanapplications.devreporttrack.Models.DataModel;
import com.sanapplications.devreporttrack.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recyclerView;
    List<DataModel> dataList;
    MyAdapterHistory adapter;
    SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Work Report History");

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);

        builder.setView(R.layout.progress_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
        dataList = new ArrayList<>();
        adapter = new MyAdapterHistory(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


//        CollectionReference tutorialsRef = db.collection("UsersReport");
//        db.collection("UsersReport").get()
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
                //"SANCHITDANG"
        ).collection("Reports").orderBy("dataLang", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    dataList.add(dataModel);

                    //Toast.makeText(getContext(), "Clicked item: " + dataClass.getKey(), Toast.LENGTH_SHORT).show(); // Add this line to show toast message

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
//
//
//                String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/devreporttrack.appspot.com/o/resumes%2F00f9a272-5988-4906-bab2-c1255c72405a?alt=media&token=f7566d8f-8a96-48c2-bc54-5026694a46f0";
//
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
//                        downloadUrl
//                ));
//
//                String title = URLUtil.guessFileName(
//                        downloadUrl
//                        , null, "application/pdf");
//                request.setTitle(title);
//                request.setDescription("Downloading pls wait");
//                String cookie = CookieManager.getInstance().getCookie(downloadUrl);
//                request.addRequestHeader("cookie",cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
//
//                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//
//                Toast.makeText(getActivity(), "Downloading Started", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//


        return view;

    }


    public void searchList(String text){
        ArrayList<DataModel> searchList = new ArrayList<>();
        for (DataModel dataModel : dataList){
            if (dataModel.getDataTitle().toLowerCase().contains(text.toLowerCase())
            ||
                    dataModel.getLang().toLowerCase().contains(text.toLowerCase())
            ){
                searchList.add(dataModel);
            }
        }
        adapter.searchDataList(searchList);
    }



}