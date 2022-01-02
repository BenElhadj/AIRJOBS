package com.airjob.airjobs.ui.settings;

import static com.airjob.airjobs.R.id.nav_host_fragment_activity_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.airjob.airjobs.HomePage;
import com.airjob.airjobs.R;
import com.airjob.airjobs.databinding.FragmentSettingsBinding;
import com.airjob.airjobs.ui.findit.FinditFragment;
import com.airjob.airjobs.ui.localisation.LocalisationFragment;
import com.airjob.airjobs.ui.manageProfil.ProfilFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private DocumentReference noteRef;
    private FirebaseFirestore db;
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView name=binding.tvParaNom;
        TextView prenom= binding.tvParaPrenom;
        ImageView imageParam=binding.ivPara;

        LinearLayout modifProfil= binding.lLModifProfil;
        LinearLayout geoLoc= binding.lLGeoLoc;
        LinearLayout filtreProfil= binding.lLFiltreProfil;

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         uid = user.getUid();




        noteRef = db.document("Candidat/" + uid);

        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name.setText((CharSequence) documentSnapshot.get("nom"));
                prenom.setText((CharSequence) documentSnapshot.get("prenom"));

                if (documentSnapshot.get("imageurl") == null || documentSnapshot.get("imageurl").equals("") ) {


                    Glide.with(getContext())
                            .load(R.drawable.add_photo)
                            .circleCrop()
                            .override(100,100)
                            .into(imageParam);


                }else{
                    Glide.with(getContext())
                            .load(documentSnapshot.get("imageurl"))
                            .fitCenter()
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageParam);

                }
            }
        });



        imageParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.actionModifProfil);
            }
        });


        modifProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.actionModifProfil);
            }
        });

        geoLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.actionGeoloc);
            }
        });

        filtreProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.actionFiltre);
            }
        });








        LinearLayout layout = binding.lLDeconnexion;
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), com.airjob.airjobs.MainActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void moveTo(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(nav_host_fragment_activity_main, fragment,"findThisFragment")
                .addToBackStack(null)
                .commit();

    }



}