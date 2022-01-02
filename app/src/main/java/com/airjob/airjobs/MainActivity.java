package com.airjob.airjobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airjob.airjobs.ui.gestionConnexion.Creeuncompte;
import com.airjob.airjobs.ui.gestionConnexion.Recuperationmotdepasse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText login1;
    private EditText motdepasselog1;

    private String login, motdepasselog;

    private FirebaseFirestore db;
    private DocumentReference noteRef;
    private CollectionReference noteCollectionRef;

    private TextView creeuncompte;
    private TextView motdepassoublie;
    private TextView alert1;
    private TextView alert2;

    private Button buttonlog;

    private String logs;

    private String ok;

    private void init(){

        creeuncompte = findViewById(R.id.creeuncompte);
        motdepassoublie = findViewById(R.id.motdepassoublie);
        buttonlog=findViewById(R.id.buttonrecup);

        login1=findViewById(R.id.loginrecup);
        motdepasselog1=findViewById(R.id.motdepasselog);

        db=FirebaseFirestore.getInstance();
        noteCollectionRef = db.collection("notes");

        alert1=findViewById(R.id.alert1);
        alert2=findViewById(R.id.alert2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        init();

        creeuncompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        motdepassoublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity3();

            }
        });
    }

    public void btnLoginClick(View view) {
        login = login1.getText().toString().trim();
        motdepasselog = motdepasselog1.getText().toString().trim();

        // Vérification du remplissage des champs email et password
        if ( login.equals("")) {
            login1.setError("enteremail");}
        if (!Patterns.EMAIL_ADDRESS.matcher( login).matches()) {
            alert1.setVisibility(View.VISIBLE);}
        else{alert1.setVisibility(View.INVISIBLE);}

        if (motdepasselog.equals("")) {
            motdepasselog1.setError("enter password");
        } else {
            // 9 Ajout de la vérification de la connection internet
            if (Util.connectionAvailable(this)) // Si la connexion fonctionne
            { // Alors on exécute la méthode
                // 8.2 Si la connexion se fait alors on affiche la progressBar
                /** 5 Connexion à authenticator en utilisant les tools Firebase cf cours**/
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(login, motdepasselog)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // 8.3 Que la connexion se fasse ou non on fait disparaître la progressBar
                                if (task.isSuccessful()) {
                                    // Remplissage par la suite
                                    /** 7 Ajout du lien vers mainActivity si l'utilisateur est bien connecté **/
                                    startActivity(new Intent(MainActivity.this, HomePage.class));
                                    // Utilisation de finish() pour fermer l'activité présente
                                    finish();
                                } else {
                                    // Affichage de l'erreur de connexion, il est possible de
                                    // personnaliser, manuelement, le message en fonction du type d'erreur
                                    Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                                    //alert2.setVisibility(View.VISIBLE);

                                }
                            }
                        });
                // 9.1 Sinon
            } else {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openActivity2(){
        Intent intent = new Intent(this, Creeuncompte.class);
        startActivity(intent);
    }

    public void openActivity3(){
        Intent intent = new Intent(this, Recuperationmotdepasse.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {

            startActivity(new Intent(MainActivity.this, HomePage.class));
            finish();
        }
    }

    /** 12 Ajout des boutons next et send à la place du retour chariot du keyboard **/
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // Utilisation de actionId qui correspond à l'action ajouter dans le xml
            switch (actionId){
                case EditorInfo.IME_ACTION_DONE:
                    btnLoginClick(v);
            }
            return false; // On laisse le return à false pour empêcher le comportement normal du clavier
        }
    };




}