package com.sanapplications.devreporttrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class verifyOTPActivity extends AppCompatActivity {

    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;

    private String verificationid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);




        TextView textView = findViewById(R.id.textmobile);
        textView.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));


        inputcode1 = findViewById(R.id.inputotp1);
        inputcode2 = findViewById(R.id.inputotp2);
        inputcode3 = findViewById(R.id.inputotp3);
        inputcode4 = findViewById(R.id.inputotp4);
        inputcode5 = findViewById( R.id.inputotp5);
        inputcode6 = findViewById(R.id.inputotp6);

        setupotpinput();

        final ProgressBar progressBar = findViewById(R.id.Progressbarverifyotp);

        final Button buttonverify = findViewById(R.id.buttonverify);

        verificationid = getIntent().getStringExtra("verfication");

        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputcode1.getText().toString().trim().isEmpty()
                && !inputcode2.getText().toString().trim().isEmpty()
                && !inputcode3.getText().toString().trim().isEmpty()
                && !inputcode4.getText().toString().trim().isEmpty()
                && !inputcode5.getText().toString().trim().isEmpty()
                && !inputcode6.getText().toString().trim().isEmpty()){

                    String code = inputcode1.getText().toString() +
                            inputcode2.getText().toString() +
                            inputcode3.getText().toString() +
                            inputcode4.getText().toString() +
                            inputcode5.getText().toString() +
                            inputcode6.getText().toString();

                    if (verificationid != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        buttonverify.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationid, code
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonverify.setVisibility(View.VISIBLE);
                                        if (task.isSuccessful()) {


                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            assert user != null;
                                            DocumentReference userRef = db.collection("USERS").document(user.getUid());

                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            // User exists in Firestore, navigate to home screen
                                                            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            // User does not exist in Firestore, navigate to register screen
                                                            Intent intent = new Intent(verifyOTPActivity.this, RegisterActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    } else {
                                                        Log.d("Error getting document: ", String.valueOf(task.getException()));
                                                    }
                                                }
                                            });


//                                            Intent intent = new Intent(verifyOTPActivity.this, RegisterActivity.class);
//                                            startActivity(intent);
//                                            finish();

//                                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(verifyOTPActivity.this, "Enter the Correct otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }else {
                    Toast.makeText(verifyOTPActivity.this, "Please enter the otp", Toast.LENGTH_SHORT).show();
                }
            }
        });




//





        findViewById(R.id.textresendotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        verifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {



                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(verifyOTPActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationid = newVerification;
                                Toast.makeText(verifyOTPActivity.this, "OTP sent again", Toast.LENGTH_SHORT).show();
                            }
                        }

                );
            }
        });



    }

    private void setupotpinput() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
