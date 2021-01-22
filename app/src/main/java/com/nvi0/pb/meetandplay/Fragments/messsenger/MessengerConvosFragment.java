package com.nvi0.pb.meetandplay.Fragments.messsenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nvi0.pb.meetandplay.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessengerConvosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessengerConvosFragment extends Fragment {


    public MessengerConvosFragment() {
        // Required empty public constructor
    }

    public static MessengerConvosFragment newInstance() {
        MessengerConvosFragment fragment = new MessengerConvosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger_convos, container, false);
    }
}