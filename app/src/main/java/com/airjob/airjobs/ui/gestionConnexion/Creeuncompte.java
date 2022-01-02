package com.airjob.airjobs.ui.gestionConnexion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airjob.airjobs.HomePage;
import com.airjob.airjobs.MainActivity;
import com.airjob.airjobs.R;
import com.airjob.airjobs.ui.manageProfil.ProfilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Creeuncompte extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference noteRef;

    private EditText Email, NomInscription, PrenomInscription, motdepasse, motdepasse2;
    private String uid;

    private TextView textView3, textView7, textView4;

    private void init() {

        Email = findViewById(R.id.emailEnregistrement);
        NomInscription = findViewById(R.id.nomEnregistrer);
        PrenomInscription=findViewById(R.id.prenomEnregistrer);
        motdepasse = findViewById(R.id.motdepasse);
        motdepasse2 = findViewById(R.id.motdepasse2);

        db = FirebaseFirestore.getInstance();


        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView7 = findViewById(R.id.textView7);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creeuncompte);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        init();

    }

    public void addcompte(View view) {

        String Email1 = Email.getText().toString().trim();
        String NomInscription1 = NomInscription.getText().toString().trim();
        String PrenomInscription1 = PrenomInscription.getText().toString().trim();
        String motdepasse1 = motdepasse.getText().toString().trim();
        String motdepassebis = motdepasse2.getText().toString().trim();

        if (motdepasse1.length() < 6) {
            textView3.setVisibility(View.VISIBLE);}
        else{textView3.setVisibility(View.INVISIBLE);}
        if (!Patterns.EMAIL_ADDRESS.matcher(Email1).matches()) {
            textView7.setVisibility(View.VISIBLE);

        }  if (!motdepasse1.equals(motdepassebis)) {
            textView4.setVisibility(View.VISIBLE);
        } else{textView4.setVisibility(View.INVISIBLE);}

        if (NomInscription1.equals("")) {
            NomInscription.setError("Enter name");
        } else if (Email1.equals("")) {
            Email.setError("Enter Email");
        } else if (motdepasse1.equals("")) {
            motdepasse.setError("enterpassword");
        } else if (motdepassebis.equals("")) {
            motdepasse2.setError("enterpassword");
        }
        else {
            // 12 Ajout de la vérification de la connection internet
            if(com.airjob.airjobs.Util.connectionAvailable(this))// Si la connexion fonctionne

            { // Alors on exécute la méthode
                // 11.9 ProgressBar
                // progressBar.setVisibility(View.VISIBLE);


                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(Email1, motdepasse1)
                        // Ajout la méthode addOnCompleteListener pour vérifier la bonne transmition des
                        // informations à Firebase Authenticator
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                // 11.10 ProgressBar
                                // progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    /** 5.2 Association de l'utilisateur courant à FirebaseUser dans le cadre du
                                     * changement de nom **/
                                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    db = FirebaseFirestore.getInstance();

                                    uid = firebaseUser.getUid();

                                    Modelcreeruncompte contenuNote = new Modelcreeruncompte(NomInscription.getText().toString(), PrenomInscription.getText().toString());


                                    noteRef = db.document("Candidat/" + uid);

                                    noteRef.set(contenuNote);
                                    Toast.makeText(Creeuncompte.this, "Compte crée", Toast.LENGTH_LONG).show();


                                    startActivity(new Intent(Creeuncompte.this, MainActivity.class));

                                }
                            }
                        });
            }
        }
    }
//

    /** 12 Ajout des boutons next et send à la place du retour chariot du keyboard **/
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // Utilisation de actionId qui correspond à l'action ajouter dans le xml
            switch (actionId){
                case EditorInfo.IME_ACTION_DONE:
                    addcompte(v);
            }
            return false; // On laisse le return à false pour empêcher le comportement normal du clavier
        }
    };
}