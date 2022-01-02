package com.airjob.airjobs.ui.gestionConnexion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airjob.airjobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Recuperationmotdepasse extends AppCompatActivity {

    private static final String TAG = "Recuperationmotdepasse";
    private EditText loginrecup;

    private Button buttonrecup;

    private FirebaseAuth firebaseAuth;

    private void init() {
        loginrecup = findViewById(R.id.loginrecup);
        buttonrecup = findViewById(R.id.buttonrecup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperationmotdepasse);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        init();
    }

    public void resetpassword(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        String emailAddress = loginrecup.getText().toString();

        firebaseAuth.setLanguageCode("fr");
        firebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(Recuperationmotdepasse.this, "successful", Toast.LENGTH_SHORT).show();
                        }else{ Log.d(TAG, "Error");
                            Toast.makeText(Recuperationmotdepasse.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}