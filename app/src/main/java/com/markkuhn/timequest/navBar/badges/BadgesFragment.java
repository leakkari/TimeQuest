package com.markkuhn.timequest.navBar.badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markkuhn.timequest.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class BadgesFragment extends Fragment {
    private BadgesViewModel badgesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        badgesViewModel=
                ViewModelProviders.of(this).get(BadgesViewModel.class);
        View root = inflater.inflate(R.layout.badges_frag, container, false);

        return root;
    }
}
