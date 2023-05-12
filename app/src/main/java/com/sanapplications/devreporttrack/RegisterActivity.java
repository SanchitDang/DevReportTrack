package com.sanapplications.devreporttrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;

    private EditText editTextName, editTextAddress;
    private ImageView imageViewPhoto;
    private Button  buttonChooseResume, buttonUpload;
    private Uri photoUri, resumeUri;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Registration");

        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        //buttonChoosePhoto = findViewById(R.id.buttonChoosePhoto);
        buttonChooseResume = findViewById(R.id.buttonChooseResume);
        buttonUpload = findViewById(R.id.buttonUpload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        buttonChooseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF_REQUEST);
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        final String name = editTextName.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            editTextAddress.setError("Please enter your address");
            return;
        }

        if (photoUri == null) {
            Toast.makeText(this, "Please choose a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resumeUri == null) {
            Toast.makeText(this, "Please choose a resume", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(RegisterActivity.this, "Uploading docs. Pleas Wait!", Toast.LENGTH_LONG).show();

        final StorageReference photoRef = storageReference.child("photos/" + UUID.randomUUID().toString());
        final StorageReference resumeRef = storageReference.child("resumes/" + UUID.randomUUID().toString());

        photoRef.putFile(photoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final Uri photoUrl = task.getResult();

                    resumeRef.putFile(resumeUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {

                                throw task.getException();
                            }                        return resumeRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri resumeUrl = task.getResult();

                            final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("address", address);
                                user.put("photoUrl", photoUrl.toString());
                                user.put("resumeUrl", resumeUrl.toString());

                                db.collection("USERS").document(
                                        currentUserId
                                       // "SANCHITDANG"
                                        ).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to upload resume", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
            imageViewPhoto.setImageURI(photoUri);
        }

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            resumeUri = data.getData();
        }
    }
}




