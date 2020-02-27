package com.markkuhn.timequest.navBar.badges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BadgesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public BadgesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is friends fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
