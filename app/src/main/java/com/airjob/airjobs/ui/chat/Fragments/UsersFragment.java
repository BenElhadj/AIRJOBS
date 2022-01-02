package com.airjob.airjobs.ui.chat.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.airjob.airjobs.ui.chat.Model.UserModel;
import com.airjob.airjobs.ui.chat.Adapter.UserAdapter;
import com.airjob.airjobs.ui.manageProfil.ModelProfilCandidat;
import com.airjob.airjobs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    // Var des widgets
    private RecyclerView recyclerView;
    private EditText search_users;

    // Var globales
    private View view;
    private UserAdapter userAdapter;
    private List<ModelProfilCandidat> mUsers;

    // Var Firebase
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    // Initialisation des widgets
    private void init() {
        recyclerView = view.findViewById(R.id.recycler_view_user_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_users = view.findViewById(R.id.search_users);

        mUsers = new ArrayList<>();
    }

    // Initialisation de FirebaseUser
    private void initFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialisation des widgets
        init();
        // Initialisation de Firebase
        initFirebase();

        showUsers();

        // Le texteChangedListener pour écouter l'editText
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void searchUsers(String s) {

        Query query1 = db
                .collection("Candidat")
                .orderBy("search")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query1
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mUsers.clear();

                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            ModelProfilCandidat user = documentSnapshot.toObject(ModelProfilCandidat.class);

                            assert user != null;
                            assert firebaseUser != null;
                            if (!user.getIDprofil().equals(firebaseUser.getUid())){
//                            if (!user.getIDprofil().contentEquals(firebaseUser.getUid())){ //###a la base etais comme ça
                                mUsers.add(user);
                            }
                        }
                        userAdapter = new UserAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                });

    }

    private void showUsers() {
        db
                .collection("Candidat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mUsers.clear();

                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            ModelProfilCandidat user = documentSnapshot.toObject(ModelProfilCandidat.class);
                            Log.i("###----->>>", "user -----> dans le boucle For: " + user);

                            assert user != null;
                            assert firebaseUser != null;
                            if (!user.getIDprofil().equals(firebaseUser.getUid())) {
                            //if (!user.getIDprofil().contentEquals(firebaseUser.getUid())){ //###a la base etais comme ça

                                Log.i("###----->>>", "autre profils -----> user.getIDprofil: " + user.getIDprofil());

                                mUsers.add(user);
                            } //else {// ce else est a supprimer
//                                Log.i("###----->>>", "mon profil --------> user.currentUser: "
//                                        + firebaseUser.getUid());
//                            }
                        }
                        userAdapter = new UserAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                });
    }
}