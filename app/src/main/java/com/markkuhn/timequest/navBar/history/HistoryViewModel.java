package com.markkuhn.timequest.navBar.history;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.markkuhn.timequest.R;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is History fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }


}
