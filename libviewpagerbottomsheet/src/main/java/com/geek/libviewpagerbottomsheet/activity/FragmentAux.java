package com.geek.libviewpagerbottomsheet.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geek.libviewpagerbottomsheet.R;

public class FragmentAux extends Fragment {

    public static FragmentAux newInstance() {
        Bundle bundle = new Bundle();
        FragmentAux fragmentPagerImage = new FragmentAux();
        fragmentPagerImage.setArguments(bundle);
        return fragmentPagerImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aux, container, false);
        return view;
    }

}