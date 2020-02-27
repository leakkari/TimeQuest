package com.markkuhn.timequest.navBar.ring;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Ring fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

