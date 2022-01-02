package com.airjob.airjobs.ui.manageProfil;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airjob.airjobs.HomePage;
import com.airjob.airjobs.MainActivity;
import com.airjob.airjobs.R;
import com.airjob.airjobs.databinding.FragmentProfilBinding;
import com.airjob.airjobs.ui.gestionConnexion.Modelcreeruncompte;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import java.net.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ProfilFragment extends Fragment implements android.widget.AdapterView.OnItemSelectedListener {

    private Uri imageUri;
    private String uid;

    private DocumentReference noteRef;
    private String variableGlobalJob;
    private String variableGlobalSecteur;
    private String variableGlobalChamps;
    private String variableGlobalExp;


    private FragmentProfilBinding binding;
    private LinearLayout linearFormulaire;
    private TextView tvQuestionHobbies, tvQuestionPerso, tvDescription;
    private EditText etNom, etPrenom, etEntreprise, etPoste, etDescription, etHobbies1, etHobbies2, etHobbies3, etHobbies4, etHobbies5, etPerso1, etPerso2, etPerso3, etPerso4, etPerso5;
    private Spinner sSectorActivity, sProfilType, sJob, sExp;
    private Button AddBdd;
    private Button uploadPdf;
    private  ImageView ivAvatar;
    java.net.URI juri;
    java.net.URI juripdf;

    String[] sMyExp = {
            "Niveau d'expérience",
            "0-2 ans : Junior",
            "2-5 ans : Expérimenté",
            "5 ans+ : Senior"
    };

    // Variables Firebase
    private FirebaseFirestore db;
    private CollectionReference profilsRef;
    private CollectionReference secteurRef;
    private CollectionReference metierRef;
    private FirebaseUser firebaseUser;
    private StorageReference fileStorage;

    FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextView usernameProfil;
    /**
     * 1.3 Variables globales pour les URI
     **/
    private Uri localFileUri, serverFileUri, pdfUri;


    private String urlname;

    private String pdforimage;

    // Variables des listes
    List<String> profilList = new ArrayList<>();
    List<String> secteurList = new ArrayList<>();
    List<String> metierList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

//        loadImage(ivAvatar,R.drawable.add_photo);

        binding = FragmentProfilBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        TextView name=binding.usernameProfil;


        ImageView imageInit= binding.ivAvatar;


        noteRef = db.document("Candidat/" + uid);

        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name.setText(documentSnapshot.get("nom") + " " + documentSnapshot.get("prenom"));

                if (documentSnapshot.get("imageurl") == null || documentSnapshot.get("imageurl").equals("") ) {

                    Glide.with(getContext())
                            .load(R.drawable.add_photo)
                            .circleCrop()
                            .override(100,100)
                            .into(ivAvatar);

                }else{
                    Glide.with(getContext())
                            .load(documentSnapshot.get("imageurl"))
                            .fitCenter()
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivAvatar);
                }
            }
        });

//        name.setText((CharSequence) noteRef.get(Source.valueOf("nom")));

        db.collection("Type de profils")
                .whereEqualTo("Employeur", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });

        Button layout = binding.AddBdd;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String variableGlobalNom = etNom.getText().toString();
                String variableGlobalPrenom = etPrenom.getText().toString();
                String hobbie1 = etHobbies1.getText().toString();
                String hobbie2 = etHobbies2.getText().toString();
                String hobbie3 = etHobbies3.getText().toString();
                String hobbie4 = etHobbies4.getText().toString();
                String hobbie5 = etHobbies5.getText().toString();
                String traitdp1 = etPerso1.getText().toString();
                String traitdp2 = etPerso2.getText().toString();
                String traitdp3 = etPerso3.getText().toString();
                String traitdp4 = etPerso4.getText().toString();
                String traitdp5 = etPerso5.getText().toString();
                String description = etDescription.getText().toString();
                String listMatch= "";
                String listMatchPending= "";

                System.out.println("juriPdf : " +juripdf);

                if(juripdf == null || juripdf.toString().equals("")){
                    juripdf = URI.create("");
                }
                if(juri == null || juri.toString().equals("")){
                    juri= URI.create("");
                }

                ModelProfilCandidat contenuNote = new ModelProfilCandidat(variableGlobalChamps, variableGlobalSecteur, variableGlobalJob, description,
                        user.getEmail(),
                        variableGlobalNom, variableGlobalPrenom, juri.toString(), juripdf.toString(),
                        hobbie1, hobbie2, hobbie3, hobbie4, hobbie5, traitdp1, traitdp2, traitdp3, traitdp4, traitdp5,
                        variableGlobalExp, uid,"offline", variableGlobalNom.toLowerCase(),listMatch,listMatchPending);

                noteRef = db.document("Candidat/" + uid);

                noteRef.set(contenuNote);

                Toast.makeText(getContext(),"Profil enregistrer",Toast.LENGTH_LONG).show();

            }
        });

        imageInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdforimage="image";
                System.out.println("hello");

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 101);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
                }
            }
        });

        Button layout3 = binding.uploadPdf;
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdforimage="pdf";
                System.out.println("hellopdf");

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    selectPdf();
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        ivAvatar = binding.ivAvatar;
        init(v);
        spinnerChoix(v);
        spinnerProfil(v);
        return v;

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            } else {
                Toast.makeText(getContext(), R.string.access_permission_is_required, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(pdforimage=="image") {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 101) {
                if (resultCode == RESULT_OK) {
                    Uri localFileUri = data.getData();

                    System.out.println("verife image"+localFileUri);


                    if (localFileUri == null) {
//                        ivAvatar.setVisibility(View.VISIBLE);

                        Glide.with(getContext())
                                .load(R.drawable.add_photo)
                                .circleCrop()
                                .override(100,100)

//                .apply(RequestOptions.circleCropTransform())
                                .into(ivAvatar);
                    } else {
                        ivAvatar.setVisibility(View.VISIBLE);
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .circleCrop()
//                        .error(R.drawable.ic_user)
//                        .placeholder(R.drawable.ic_user)
                                ;

                        Glide.with(getContext())
                                .load(localFileUri)
                                .apply(options)
                                .fitCenter()
                                .circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivAvatar);


                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String strFileName = user.getUid() + ".jpg";


//                urlname=strFileName;

                    // On place la photo dans un dossier dans le storage
                    //final StorageReference fileRef = fileStorage.child("images/" + strFileName);
                    StorageReference fileRef = storageReference.child("images/" + strFileName);

                    // On fait l'upload

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                juri = new URI(uri.toString());

                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    fileRef.putFile(localFileUri);
                }
            }
        }
        else if (pdforimage=="pdf"){


            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==86 && resultCode==RESULT_OK&& data!=null){

                pdfUri=data.getData();

                if(pdfUri!=null) {
                    uploadFile(pdfUri);
                }
                else {
                    Toast.makeText(getActivity(), "select a file", Toast.LENGTH_LONG).show();
                }

                // notification.setText("A file is selected : "+ data.getData().getLastPathSegment());

            }else{
                Toast.makeText(getActivity(), "please select file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri pdfUri) {


        System.out.println("pdfUri : " + pdfUri);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid()+".pdf";

        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();


        StorageReference fileRef= storageReference.child("Uploads/" + uid);

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try {
                    juripdf = new URI(uri.toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        fileRef.putFile(pdfUri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init(View root) {
        sProfilType = root.findViewById(R.id.sTypeProfil);
        sSectorActivity = root.findViewById(R.id.sSectorActivity);
        sJob = root.findViewById(R.id.sJob);
        etEntreprise = root.findViewById(R.id.etEntreprise);
        etDescription = root.findViewById(R.id.etDescription);
        tvDescription = root.findViewById(R.id.tvDescription);
        etPoste = root.findViewById(R.id.etPoste);
        etNom = root.findViewById(R.id.etNom);
        etPrenom = root.findViewById(R.id.etPrenom);
        AddBdd = root.findViewById(R.id.AddBdd);
        tvQuestionHobbies = root.findViewById(R.id.tvQuestionHobbies);
        tvQuestionPerso = root.findViewById(R.id.tvQuestionPerso);
        etHobbies1 = root.findViewById(R.id.etHobbies1);
        etHobbies2 = root.findViewById(R.id.etHobbies2);
        etHobbies3 = root.findViewById(R.id.etHobbies3);
        etHobbies4 = root.findViewById(R.id.etHobbies4);
        etHobbies5 = root.findViewById(R.id.etHobbies5);
        etPerso1 = root.findViewById(R.id.etPerso1);
        etPerso2 = root.findViewById(R.id.etPerso2);
        etPerso3 = root.findViewById(R.id.etPerso3);
        etPerso4 = root.findViewById(R.id.etPerso4);
        etPerso5 = root.findViewById(R.id.etPerso5);
        uploadPdf = root.findViewById(R.id.uploadPdf);
        linearFormulaire=root.findViewById(R.id.linearFormulaire);
    }

    private void spinnerChoix(View root) {
        sExp = root.findViewById(R.id.sExp);
        ArrayAdapter<String> list1 = new ArrayAdapter<String>(
                root.getContext(), android.R.layout.simple_list_item_1, sMyExp
        );
        sExp.setAdapter(list1);
        sExp.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sTypeProfil:
                if (i == 0) {
                    invisibility(4);
                } else {
                    variableGlobalChamps = profilList.get(i);
                    spinnerSecteur(view, profilList.get(i));
                    if (variableGlobalChamps.equals("Employeur")) {
                        etEntreprise.setVisibility(View.VISIBLE);
                        etPoste.setVisibility(View.VISIBLE);
                        uploadPdf.setText(R.string.pdfDeposeRecruteur);

                    } else {
                        etEntreprise.setVisibility(View.GONE);
                        etPoste.setVisibility(View.GONE);
                        etPrenom.setVisibility(View.VISIBLE);
                        etNom.setVisibility(View.VISIBLE);
                        sSectorActivity.setVisibility(View.VISIBLE);
                        uploadPdf.setText(R.string.pdfDeposeCandidat);
                    }
                }
                break;
            case R.id.sSectorActivity:
                if (i == 0) {
                    sSectorActivity.setVisibility(View.VISIBLE);
                    invisibility(3);

                } else {
                    variableGlobalSecteur = secteurList.get(i);
                    spinnerJob(view, variableGlobalChamps, variableGlobalSecteur);
                    sJob.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sJob:
                if (i == 0) {
                    invisibility(2);
                } else {
                    variableGlobalJob = metierList.get(i);
                    sExp.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sExp:
                if (i == 0) {
                    invisibility(1);
                } else {
                    variableGlobalExp = sMyExp[i];
                    tvDescription.setVisibility(View.VISIBLE);
                    etDescription.setVisibility(View.VISIBLE);
                    uploadPdf.setVisibility(View.VISIBLE);
                    tvQuestionHobbies.setVisibility(View.VISIBLE);
                    etHobbies1.setVisibility(View.VISIBLE);
                    etHobbies2.setVisibility(View.VISIBLE);
                    etHobbies3.setVisibility(View.VISIBLE);
                    etHobbies4.setVisibility(View.VISIBLE);
                    etHobbies5.setVisibility(View.VISIBLE);
                    tvQuestionPerso.setVisibility(View.VISIBLE);
                    etPerso1.setVisibility(View.VISIBLE);
                    etPerso2.setVisibility(View.VISIBLE);
                    etPerso3.setVisibility(View.VISIBLE);
                    etPerso4.setVisibility(View.VISIBLE);
                    etPerso5.setVisibility(View.VISIBLE);
                    if (variableGlobalChamps.equals("Employeur")) {
                        tvQuestionHobbies.setText("Quel recherchez vous chez la personne a recruter ?");
                        etHobbies1.setHint("1er recherche");
                        etHobbies2.setHint("2eme recherche");
                        etHobbies3.setHint("3eme recherche");
                        etHobbies4.setHint("4eme recherche");
                        etHobbies5.setHint("5eme recherche");
                        tvQuestionPerso.setText("Quels avantages votre entreprise offre ?");
                        etPerso1.setHint("1er avantage");
                        etPerso2.setHint("2eme avantage");
                        etPerso3.setHint("3eme avantage");
                        etPerso4.setHint("4eme avantage");
                        etPerso5.setHint("5eme avantage");
                    } else {
                        tvQuestionHobbies.setText("Quels sont vos hobbies ?");
                        etHobbies1.setHint("1er hobby");
                        etHobbies2.setHint("2eme hobby");
                        etHobbies3.setHint("3eme hobby");
                        etHobbies4.setHint("4eme hobby");
                        etHobbies5.setHint("5eme hobby");
                        tvQuestionPerso.setText("Quels sont vos traits de personnalités ?");
                        etPerso1.setHint("1er trait personnel");
                        etPerso2.setHint("2eme trait personnel");
                        etPerso3.setHint("3eme trait personnel");
                        etPerso4.setHint("4eme trait personnel");
                        etPerso5.setHint("5eme trait personnel");
                    }
                }
                break;
            default:
                break;
        }
    }

    public void spinnerProfil(View v) {
        profilsRef = db.collection("Type de profils");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, profilList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sProfilType.setAdapter(adapter);
        profilsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String secteur = document.getId();
                                profilList.add(secteur);
                            }
                            adapter.notifyDataSetChanged();
                            sProfilType.setOnItemSelectedListener(ProfilFragment.this);
                        }
                    }
                });
    }

    public void spinnerSecteur(View v, String variableGlobalChamps) {
        secteurList.clear();
        secteurRef = db.collection("Type de profils/" + variableGlobalChamps + "/Secteurs");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, secteurList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSectorActivity.setAdapter(adapter);
        secteurRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String secteur = document.getId();
                                secteurList.add(secteur);
                            }
                            adapter.notifyDataSetChanged();
                            sSectorActivity.setOnItemSelectedListener(ProfilFragment.this);
                        }
                    }
                });
    }

    public void spinnerJob(View v, String variableGlobalChamps, String variableGlobalSecteur) {
        metierList.clear();
        metierRef = db.collection("Type de profils/" + variableGlobalChamps + "/Secteurs/" + variableGlobalSecteur + "/Metier");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, metierList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sJob.setAdapter(adapter);
        metierRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String secteur = document.getId();
                                metierList.add(secteur);
                            }
                            adapter.notifyDataSetChanged();
                            sJob.setOnItemSelectedListener(ProfilFragment.this);
                        }
                    }
                });
    }

    @Override
    public void onNothingSelected(android.widget.AdapterView<?> adapterView) {
    }

    public void invisibility(int niveau) {
        if (niveau >= 1) {
            tvQuestionHobbies.setVisibility(View.GONE);
            etHobbies1.setVisibility(View.GONE);
            etHobbies2.setVisibility(View.GONE);
            etHobbies3.setVisibility(View.GONE);
            etHobbies4.setVisibility(View.GONE);
            etHobbies5.setVisibility(View.GONE);
            tvQuestionPerso.setVisibility(View.GONE);
            etPerso1.setVisibility(View.GONE);
            etPerso2.setVisibility(View.GONE);
            etPerso3.setVisibility(View.GONE);
            etPerso4.setVisibility(View.GONE);
            etPerso5.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
            etDescription.setVisibility(View.GONE);
            uploadPdf.setVisibility(View.GONE);

        }
        if (niveau >= 2) {
            sExp.setVisibility(View.GONE);
        }
        if (niveau >= 3) {
            sJob.setVisibility(View.GONE);
        }
        if (niveau >= 4) {
            etNom.setVisibility(View.GONE);
            etPrenom.setVisibility(View.GONE);
            etEntreprise.setVisibility(View.GONE);
            etPoste.setVisibility(View.GONE);
            sSectorActivity.setVisibility(View.GONE);
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//
//        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//
//            selectPdf();
//        }
//        else{
//            Toast.makeText(getActivity(), "please provide permission", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void selectPdf(){

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);

        //pdfUri=intent.getData();

    }
}