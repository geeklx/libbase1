package com.geek.libviewpagerbottomsheet.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geek.libviewpagerbottomsheet.R;

public class FragmentMain extends Fragment {

    public static FragmentMain newInstance() {
        Bundle bundle = new Bundle();
        FragmentMain fragmentPagerImage = new FragmentMain();
        fragmentPagerImage.setArguments(bundle);
        return fragmentPagerImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}