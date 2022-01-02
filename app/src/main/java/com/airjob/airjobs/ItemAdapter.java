package com.airjob.airjobs;

import android.annotation.SuppressLint;
import android.app.DownloadManager;

import android.content.Context;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airjob.airjobs.ui.manageProfil.ModelProfilCandidat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<ModelProfilCandidat> itemList1;
    private Context context;

    public ItemAdapter(List<ModelProfilCandidat> itemList, Context context) {

        this.itemList1=itemList;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profil,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        RequestOptions options = new RequestOptions()
                .error(R.drawable.rabbit)
                .placeholder(R.drawable.cheguevara)
                ;

        Glide.with(this.context)
                .load(itemList1.get(position).getimageurl())
                .circleCrop()
                .override(200,200)
                .apply(options)

//                .apply(RequestOptions.circleCropTransform())
                .into(holder.itemImage);

        if(itemList1.get(position).getChamps().equals("Employeur")){

            holder.itemBioInfo.setText("L'entreprise");
            holder.itemCentreInteret.setText("Profil recherché");
            holder.itemPersonnalite.setText("Avantages de l'entreprise");
            holder.btnTelecharger.setText("Télécharger Fiche poste");
        }

        holder.itemPrenom.setText(itemList1.get(position).getNom()+" "+itemList1.get(position).getPrenom());
        holder.itemJob.setText(itemList1.get(position).getJob());

        holder.itemDescription.setText(itemList1.get(position).getDescription());

        holder.itemActivite1.setText(itemList1.get(position).getHobbie1());
        holder.itemActivite2.setText(itemList1.get(position).getHobbie2());
        holder.itemActivite3.setText(itemList1.get(position).getHobbie3());
        holder.itemActivite4.setText(itemList1.get(position).getHobbie4());
        holder.itemActivite5.setText(itemList1.get(position).getHobbie5());


        holder.itemQualite1.setText(itemList1.get(position).getTraitdep1());
        holder.itemQualite2.setText(itemList1.get(position).getTraitdep2());
        holder.itemQualite3.setText(itemList1.get(position).getTraitdep3());
        holder.itemQualite4.setText(itemList1.get(position).getTraitdep4());
        holder.itemQualite5.setText(itemList1.get(position).getTraitdep5());



        holder.btnTelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hi there");

                String nomFicher = "Cv_"+itemList1.get(position).getNom()+"_"+itemList1.get(position).getPrenom();

                   downloadFile(context, nomFicher,".pdf","downloads", itemList1.get(position).getPdfurl());

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemJob;
        TextView itemDescription,
                 itemBioInfo,
                 itemCentreInteret,
                 itemPersonnalite;
        TextView itemPrenom,
                 itemActivite1,
                 itemActivite2,
                 itemActivite3,
                 itemActivite4,
                 itemActivite5;
        TextView itemQualite1;
        TextView itemQualite2;
        TextView itemQualite3;
        TextView itemQualite4;
        TextView itemQualite5;
        Button btnTelecharger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage=itemView.findViewById(R.id.ivProfil);
            itemPrenom=itemView.findViewById(R.id.tvPrenomNom);

            itemJob = itemView.findViewById(R.id.tvMetier);
            itemDescription= itemView.findViewById(R.id.tvBio);

            itemBioInfo=itemView.findViewById(R.id.tvBioInfo);
            itemCentreInteret=itemView.findViewById(R.id.tvCentre);
            itemPersonnalite=itemView.findViewById(R.id.tvPersonalite);

            itemActivite1 = itemView.findViewById(R.id.tvActivite1);
            itemActivite2 = itemView.findViewById(R.id.tvActivite2);
            itemActivite3 = itemView.findViewById(R.id.tvActivite3);
            itemActivite4 = itemView.findViewById(R.id.tvActivite4);
            itemActivite5 = itemView.findViewById(R.id.tvActivite5);

            itemQualite1 = itemView.findViewById(R.id.tvQualite1);
            itemQualite2 = itemView.findViewById(R.id.tvQualite2);
            itemQualite3 = itemView.findViewById(R.id.tvQualite3);
            itemQualite4 = itemView.findViewById(R.id.tvQualite4);
            itemQualite5 = itemView.findViewById(R.id.tvQualite5);

            btnTelecharger=itemView.findViewById(R.id.btnTelecharger);

        }
    }




    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

}
