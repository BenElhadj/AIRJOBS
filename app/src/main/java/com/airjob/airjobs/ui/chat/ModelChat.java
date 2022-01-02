package com.airjob.airjobs.ui.chat;

public class ModelChat {
    int image;
    String name;
    String hours;

    public ModelChat() {
    }

    public ModelChat(int image, String name, String hours) {
        this.image = image;
        this.name = name;
        this.hours = hours;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
