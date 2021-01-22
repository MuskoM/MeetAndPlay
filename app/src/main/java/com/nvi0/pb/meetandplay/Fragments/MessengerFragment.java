package com.nvi0.pb.meetandplay.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.BoardGamesAtlas;

import org.json.JSONArray;


public class MessengerFragment extends Fragment {

    private static final String TAG = "MessengerFragment";
    DatabaseReference messagesDbReference = FirebaseDatabase.getInstance().getReference("message");

    TextView txt_view;
    Button insert_btn;
    BoardGamesAtlas boardGamesAtlas;

    public MessengerFragment() {
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
        return inflater.inflate(R.layout.fragment_messenger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_view = view.findViewById(R.id.text_text);
        insert_btn = view.findViewById(R.id.insert_to_db);

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesDbReference.setValue("Sample child");
            }
        });

        messagesDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String message = snapshot.getValue(String.class);
                txt_view.setText(message);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });
    }
}