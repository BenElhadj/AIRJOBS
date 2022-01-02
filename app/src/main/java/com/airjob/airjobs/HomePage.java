package com.airjob.airjobs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.airjob.airjobs.databinding.ActivityBarNavBinding;
import com.airjob.airjobs.ui.chat.MessageActivity;
import com.airjob.airjobs.ui.manageProfil.ModelProfilCandidat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {
    private FirebaseFirestore db;
    private DocumentReference noteRef;
    private NavOptions navOptions;
    private Boolean i = false;


    private ActivityBarNavBinding binding;

//    public void deco(View view) {
//        FirebaseAuth.getInstance().signOut();
////        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
////        firebaseUser = null;
//        startActivity(new Intent(HomePage.this, MainActivity.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityBarNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_setting,
                R.id.navigation_localisation,
                R.id.navigation_findit,
                R.id.navigation_chat,
                R.id.navigation_profil)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);


        noteRef = db.document("Candidat/"+uid);
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot2) {
                                              if (documentSnapshot2.exists()) {

                                                  ModelProfilCandidat contenuNote = documentSnapshot2.toObject(ModelProfilCandidat.class);

                                                  if (contenuNote.getChamps()==null) {

                                                      navOptions = new NavOptions.Builder()
                                                              .setPopUpTo(R.id.navigation_findit, true)
                                                              .build();
                                                          navController.navigate(R.id.action_firstFragment_to_secondFragment, savedInstanceState, navOptions);
                                                      }
                                                  }
                                              }
                                      });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.







        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


}