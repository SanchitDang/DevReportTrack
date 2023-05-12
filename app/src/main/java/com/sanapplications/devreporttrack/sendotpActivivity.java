package com.sanapplications.devreporttrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class sendotpActivivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp_activivity);

        getSupportActionBar().hide();

        final EditText inputmobile = findViewById(R.id.input_mobile_number);
        final Button buttongetotp = findViewById(R.id.buttongetotp);

        final ProgressBar progressBar =  findViewById(R.id.progressbarforotp);

        buttongetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




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
//                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//
//                Toast.makeText(sendotpActivivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();

//




                if (!inputmobile.getText().toString().trim().isEmpty()){
                    if ((inputmobile.getText().toString().trim()).length() == 10){

                        progressBar.setVisibility(View.VISIBLE);
                        buttongetotp.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + inputmobile.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                sendotpActivivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        progressBar.setVisibility(View.GONE);
                                        buttongetotp.setVisibility(View.VISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        buttongetotp.setVisibility(View.VISIBLE);
                                        Toast.makeText(sendotpActivivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verficationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        buttongetotp.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getApplicationContext(),verifyOTPActivity.class);
                                        intent.putExtra("mobile",inputmobile.getText().toString());
                                        intent.putExtra("verfication",verficationid);
                                        startActivity(intent);
                                    }
                                }

                        );



                    }else {
                        Toast.makeText(sendotpActivivity.this,"Please enter correct number",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(sendotpActivivity.this,"Enter Mobile number",Toast.LENGTH_SHORT).show();
                }









            }
        });
    }
}
