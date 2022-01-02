package com.airjob.airjobs.ui.chat.Model;

import java.util.List;

public class ChatlistModel {
    public List<String> id;

    public ChatlistModel() {
    }

    public ChatlistModel(List<String> id) {
        this.id = id;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }
}
