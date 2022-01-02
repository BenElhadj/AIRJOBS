package com.airjob.airjobs.ui.chat.Fragments;

import static com.airjob.airjobs.ui.chat.ConstantNode.NODE_CANDIDATS;
import static com.airjob.airjobs.ui.chat.ConstantNode.NODE_CHATLIST;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airjob.airjobs.ui.chat.Adapter.UserAdapter;
import com.airjob.airjobs.ui.chat.Model.ChatlistModel;
//import com.airjob.airjobs.ui.chat.Model.UserModel;
import com.airjob.airjobs.ui.manageProfil.ModelProfilCandidat;
import com.airjob.airjobs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<ModelProfilCandidat> mUsers;

    private List<ChatlistModel> usersList;   // ###a tester en changeant mUsersConnected par usersList
    private List<String> mUsersConnected; // Liste des utilisateurs avec lequels une session de chat est ouverte


    private FirebaseUser fuser;


    // Avec Firestore
    private FirebaseFirestore db;
    private CollectionReference chatListCollectionRef;
    private CollectionReference userCollectionReference;
    private DatabaseReference reference; // Avec RealTime




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_chat_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        mUsersConnected = new ArrayList<>();


        // Avec Firestore
        // Init de Firestore
        db = FirebaseFirestore.getInstance();
        chatListCollectionRef = db.collection(NODE_CHATLIST);
        userCollectionReference = db.collection(NODE_CANDIDATS);

//         Création de la liste des utilisateurs avec lesquels une session de chat est ouverte
        final DocumentReference chatListDocRef = chatListCollectionRef.document(fuser.getUid());
        chatListDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null && value.exists()) {
                    mUsersConnected.clear();
                    ChatlistModel chatlist = value.toObject(ChatlistModel.class);
                    for (int i = 0; i < chatlist.getId().size(); i++) {
                        String user = chatlist.getId().get(i);
                        mUsersConnected.add(user);
                    }
                    chatList();
                }
            }
        });
        return view;
    }



    private void chatList() {
        userCollectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mUsers.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            ModelProfilCandidat user = documentSnapshot.toObject(ModelProfilCandidat.class);
                            for (int i = 0; i < mUsersConnected.size(); i++) {
                                if (user.getIDprofil().equals(mUsersConnected.get(i))) {
                                    mUsers.add(user);
                                }
                            }
                        }
                        userAdapter = new UserAdapter(getContext(), mUsers, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                });
    }
}