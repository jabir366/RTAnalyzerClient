package com.example.mca15a.rtanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by elisa on 29/3/18.
 */

public class HomeFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.content_home,container,false);
        // return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }
}
