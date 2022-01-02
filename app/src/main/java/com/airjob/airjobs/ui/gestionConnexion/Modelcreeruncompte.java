package com.airjob.airjobs.ui.gestionConnexion;

import android.widget.EditText;

public class Modelcreeruncompte {

    private String nom;
    private String prenom;

    public Modelcreeruncompte(String nom, String prenom) {
        this.nom = nom;
        this.prenom=prenom;
    }

    public Modelcreeruncompte() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
