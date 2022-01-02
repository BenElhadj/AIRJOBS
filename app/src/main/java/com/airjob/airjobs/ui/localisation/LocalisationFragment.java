package com.airjob.airjobs.ui.localisation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airjob.airjobs.ItemAdapter;
import com.airjob.airjobs.R;
import com.airjob.airjobs.databinding.FragmentLocalisationBinding;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.airjob.airjobs.ui.manageProfil.ModelProfilCandidat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class LocalisationFragment extends Fragment implements OnMapReadyCallback {


    private DocumentReference noteRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    private CollectionReference candidat;

    private String profil;
    private String profilmetier;


    private FirebaseFirestore db;
    private GoogleMap mMap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    ArrayList<String> arrayList2 = new ArrayList<String>();



    RecyclerView recyclerView;
    List<ModelProfilCandidat> itemList;

//    LatLng paris1 = new LatLng(48.856387640762975, 2.365493852819937);
//     LatLng paris2 = new LatLng(48.861661119386106, 2.3537722827135332);
//    LatLng paris3 = new LatLng(48.86615714762331, 2.3211951047523773);


    private LocalisationViewModel localisationViewModel;
    private FragmentLocalisationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();


        localisationViewModel =
                new ViewModelProvider(this).get(LocalisationViewModel.class);

        binding = FragmentLocalisationBinding.inflate(inflater, null, false);
        View root = binding.getRoot();


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        String address = "22 rue Rambuteau, 75003 Paris";
//        GeoCodeLocation locationAddress = new GeoCodeLocation();
////        locationAddress.getAddressFromLocation(address, getActivity(), new
////                GeoCoderHandler());
////        onMapReady(mMap);
//
////         arrayList.add(paris1);
////        arrayList.add(paris2);
////        arrayList.add(paris3);


        return root;
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
    }


    // TODO Ajouter le code pour afficher les markers en fonction des jobs recherchés ====>

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

//        System.out.println("#############  map ready");
//        mMap = googleMap;
//
//        for (int i = 0; i < arrayList.size(); i++) {
//
//            System.out.println("#############  map ready" + arrayList.size());
//
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
//            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
//
//        }

        mMap = googleMap;

        initData();


        // creating a variable for document reference.
        DocumentReference documentReference = db.collection("Candidat").document("0DgmLhQwXtMqMnGNjFfene4oEBh1");

        // calling document reference class with on snap shot listener.
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    // below line is to create a geo point and we are getting
                    // geo point from firebase and setting to it.
                    GeoPoint geoPoint = value.getGeoPoint("geoprofil");

                    // getting latitude and longitude from geo point
                    // and setting it to our location.
                    LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                    // adding marker to each location on google maps
                    mMap.addMarker(new MarkerOptions().position(location).title("Marker"));

                    // below line is use to move camera.
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                } else {
                    //Toast.makeText(MapsActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initData() {

        db = FirebaseFirestore.getInstance();

        noteRef = db.document("Candidat/"+uid);
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot2) {
                        if(documentSnapshot2.exists()){

                            System.out.println("hello3: ");
                            ModelProfilCandidat contenuNote= documentSnapshot2.toObject(ModelProfilCandidat.class);

                            if(contenuNote.getChamps().equals("Employeur")){
                                profil="Demandeur d'emploi";
                                profilmetier=contenuNote.getJob();
                            }else{
                                profil="Employeur";
                                profilmetier=contenuNote.getJob();
                            }

                        }else{

                        }
                       // recyclerView.setAdapter(new ItemAdapter(itemList,getContext()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        candidat= db.collection("Candidat");

        //System.out.println("candid: " + candidat.getId());


        noteRef = db.document("Candidat/"+uid);

        itemList=new ArrayList<ModelProfilCandidat>();

        //profil="Candidat";


        candidat.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot listeSnapshots) {


                        for(QueryDocumentSnapshot documentSnapshot : listeSnapshots){
                            // pour chaque element(snapshot) de la liste

                            ModelProfilCandidat contenuNote6 = documentSnapshot.toObject(ModelProfilCandidat.class);

                            System.out.println("id7: " + documentSnapshot.getId()+ " profil: "+profil+" profilmetier: "+ profilmetier);
//                            System.out.println("contenu de la note candidat :" + contenuNote.getJob());

                            System.out.println("profil check: "+profil);
//                            profil="Candidat";

                            noteRef = db.document("Candidat/"+documentSnapshot.getId());
                            noteRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot2) {
                                            if(documentSnapshot2.exists()){

                                                System.out.println("hello3: ");
                                                ModelProfilCandidat contenuNote= documentSnapshot2.toObject(ModelProfilCandidat.class);

                                                if(contenuNote.getJob()!=null) {
                                                    if (contenuNote.getJob().equals(profilmetier) && contenuNote.getChamps().equals(profil)) {

//                                                        System.out.println("job :" + contenuNote.getJob());
                                                        if(documentSnapshot2.getGeoPoint("geoprofil") != null) {
                                                            GeoPoint geoPoint = documentSnapshot2.getGeoPoint("geoprofil");

                                                            // getting latitude and longitude from geo point
                                                            // and setting it to our location.
                                                            LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                                                            // adding marker to each location on google maps
                                                            mMap.addMarker(new MarkerOptions().position(location).title(contenuNote.getNom()));

                                                            // below line is use to move camera.
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                                        } else {
//                                                            LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
//
//                                                            // adding marker to each location on google maps
//                                                            mMap.addMarker(new MarkerOptions().position(location).title(contenuNote.getNom()));
//
//                                                            // below line is use to move camera.
//                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                                                        }

//                                                        itemList.add(new ModelProfilCandidat(contenuNote.getChamps(), contenuNote.getSecteur(), contenuNote.getJob(), contenuNote.getDescription(),
//                                                                contenuNote.getEmail2(), contenuNote.getNom(), contenuNote.getPrenom(), contenuNote.getimageurl(), contenuNote.getPdfurl(),
//                                                                contenuNote.getHobbie1(), contenuNote.getHobbie2(), contenuNote.getHobbie3(), contenuNote.getHobbie4(), contenuNote.getHobbie5(),
//                                                                contenuNote.getTraitdep1(), contenuNote.getTraitdep2(), contenuNote.getTraitdep3(), contenuNote.getTraitdep4(), contenuNote.getTraitdep5(),
//                                                                contenuNote.getExperience(),contenuNote.getIDprofil(), contenuNote.getStatus(), contenuNote.getSearch()));

                                                    }
                                                }

                                                System.out.println("regarde la"+itemList);

                                            }else{

                                            }
                                           // recyclerView.setAdapter(new ItemAdapter(itemList,getContext()));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        //tvSavedNote.setText(notes);
                    }
                });
    }

}

