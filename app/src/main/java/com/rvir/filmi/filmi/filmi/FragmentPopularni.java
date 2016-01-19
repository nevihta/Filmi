package com.rvir.filmi.filmi.filmi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rvir.filmi.filmi.R;

public class FragmentPopularni extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_filmi_fragment_popularni, container, false);
    }
}
