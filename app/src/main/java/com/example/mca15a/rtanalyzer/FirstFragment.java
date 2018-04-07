package com.example.mca15a.rtanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jovitto on 22-03-18.
 */

public class FirstFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.first_layout,container,false);
        //turn super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }
}