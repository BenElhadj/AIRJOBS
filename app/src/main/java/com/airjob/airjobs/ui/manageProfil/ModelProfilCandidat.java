package com.airjob.airjobs.ui.manageProfil;

import java.util.List;

public class ModelProfilCandidat {
    private String champs;
    private String secteur;
    private String job;
    private String description;
    private String email2;
    private String nom;
    private String prenom;
    private String imageurl;
    private String pdfurl;
    private String hobbie1;
    private String hobbie2;
    private String hobbie3;
    private String hobbie4;
    private String hobbie5;
    private String traitdep1;
    private String traitdep2;
    private String traitdep3;
    private String traitdep4;
    private String traitdep5;
    private String experience;
    private String IDprofil;
    private String status;
    private String search;
    private String isMatched;
    private String matchPending;

    public ModelProfilCandidat() {
    }

    public ModelProfilCandidat(String champs, String secteur, String job, String description,
                               String email2, String nom, String prenom, String imageurl, String pdfurl,
                               String hobbie1, String hobbie2, String hobbie3, String hobbie4, String hobbie5,
                               String traitdep1, String traitdep2, String traitdep3, String traitdep4,
                               String traitdep5, String experience, String IDprofil, String status,
                               String search, String isMatched, String matchPending) {
        this.champs = champs;
        this.secteur = secteur;
        this.job = job;
        this.description = description;
        this.email2 = email2;
        this.nom = nom;
        this.prenom = prenom;
        this.imageurl = imageurl;
        this.pdfurl = pdfurl;
        this.hobbie1 = hobbie1;
        this.hobbie2 = hobbie2;
        this.hobbie3 = hobbie3;
        this.hobbie4 = hobbie4;
        this.hobbie5 = hobbie5;
        this.traitdep1 = traitdep1;
        this.traitdep2 = traitdep2;
        this.traitdep3 = traitdep3;
        this.traitdep4 = traitdep4;
        this.traitdep5 = traitdep5;
        this.experience = experience;
        this.IDprofil = IDprofil;
        this.status = status;
        this.search = search;
        this.isMatched = isMatched;
        this.matchPending = matchPending;
    }

    public String getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(String isMatched) {
        this.isMatched = isMatched;
    }

    public String getMatchPending() {
        return matchPending;
    }

    public void setMatchPending(String matchPending) {
        this.matchPending = matchPending;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getChamps() {
        return champs;
    }

    public void setChamps(String champs) {
        this.champs = champs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getimageurl() {
        return imageurl;
    }

    public void setimageurl(String imageurl) {
        this.imageurl = imageurl;
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

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public String getHobbie1() {
        return hobbie1;
    }

    public void setHobbie1(String hobbie1) {
        this.hobbie1 = hobbie1;
    }

    public String getHobbie2() {
        return hobbie2;
    }

    public void setHobbie2(String hobbie2) {
        this.hobbie2 = hobbie2;
    }

    public String getHobbie3() {
        return hobbie3;
    }

    public void setHobbie3(String hobbie3) {
        this.hobbie3 = hobbie3;
    }

    public String getHobbie4() {
        return hobbie4;
    }

    public void setHobbie4(String hobbie4) {
        this.hobbie4 = hobbie4;
    }

    public String getHobbie5() {
        return hobbie5;
    }

    public void setHobbie5(String hobbie5) {
        this.hobbie5 = hobbie5;
    }

    public String getTraitdep1() {
        return traitdep1;
    }

    public void setTraitdep1(String traitdep1) {
        this.traitdep1 = traitdep1;
    }

    public String getTraitdep2() {
        return traitdep2;
    }

    public void setTraitdep2(String traitdep2) {
        this.traitdep2 = traitdep2;
    }

    public String getTraitdep3() {
        return traitdep3;
    }

    public void setTraitdep3(String traitdep3) {
        this.traitdep3 = traitdep3;
    }

    public String getTraitdep4() {
        return traitdep4;
    }

    public void setTraitdep4(String traitdep4) {
        this.traitdep4 = traitdep4;
    }

    public String getTraitdep5() {
        return traitdep5;
    }

    public void setTraitdep5(String traitdep5) {
        this.traitdep5 = traitdep5;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIDprofil() {
        return IDprofil;
    }

    public void setIDprofil(String IDprofil) {
        this.IDprofil = IDprofil;
    }
}