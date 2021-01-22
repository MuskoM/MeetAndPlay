package com.nvi0.pb.meetandplay.Fragments.messsenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nvi0.pb.meetandplay.R;


public class MessengerFriendsFragment extends Fragment {

    public static final String ARG_OBJECT = "pablo";

    public MessengerFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger_friends, container, false);
    }
}