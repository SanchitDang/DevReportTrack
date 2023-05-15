package com.sanapplications.devreporttrack.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sanapplications.devreporttrack.R;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang;

    Button buttonOpenPdf, buttonDownloadPdf;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("Report Details");

        detailDesc = findViewById(R.id.detailDesc);
        buttonOpenPdf = findViewById(R.id.buttonOpenPdf);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        buttonDownloadPdf = findViewById(R.id.buttonDownloadPdf);
        //editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            //Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("UsersReport").document(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                        //"SANCHITDANG"
                ).collection("Reports").document(key);

                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //reference.child(key).removeValue();
                        docRef.delete();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(DetailActivity.this, HistoryActivity.class));
                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        });

        buttonOpenPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String pdfUrl = "http://www.example.com/sample.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(imageUrl), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // If no PDF viewer app is installed, prompt the user to install one
                    Toast.makeText(DetailActivity.this, "No PDF viewer app found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageUrl = "https://firebasestorage.googleapis.com/v0/b/devreporttrack.appspot.com/o/Android%20Images%2Fmsf%3A73?alt=media&token=abe190d0-4ce7-44db-9c31-34a131c33aac";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
                        imageUrl
                ));

                String title = URLUtil.guessFileName(
                        imageUrl
                        , null, "application/pdf");
                request.setTitle(title);
                request.setDescription("Downloading pls wait");
                String cookie = CookieManager.getInstance().getCookie(imageUrl);
                request.addRequestHeader("cookie",cookie);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

//                DownloadManager.Query query = new DownloadManager.Query();
//                query.setFilterByStatus(DownloadManager.STATUS_PENDING);
//                Cursor cursor = downloadManager.query(query);
//                while (cursor.moveToNext()) {
//                    @SuppressLint("Range") long requestId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
//                    downloadManager.remove(requestId);
//                }
//                cursor.close();

               downloadManager.enqueue(request);

                Toast.makeText(DetailActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();

            }
        });



//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
//                        .putExtra("Title", detailTitle.getText().toString())
//                        .putExtra("Description", detailDesc.getText().toString())
//                        .putExtra("Language", detailLang.getText().toString())
//                        .putExtra("Image", imageUrl)
//                        .putExtra("Key", key);
//                startActivity(intent);
//            }
//        });

    }
}