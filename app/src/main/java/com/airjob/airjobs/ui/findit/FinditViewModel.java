package com.airjob.airjobs.ui.findit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FinditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FinditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Find It fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}