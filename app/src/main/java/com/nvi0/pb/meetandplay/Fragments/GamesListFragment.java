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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.BoardGamesAtlas;
import com.nvi0.pb.meetandplay.Utils.RequestsQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;


public class GamesListFragment extends Fragment {

    Button requestBtn;
    BoardGamesAtlas boardGamesAtlas;
    TextView gamesList;

    public GamesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestBtn = view.findViewById(R.id.request_button);
        gamesList = view.findViewById(R.id.gameList_label);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardGamesAtlas = new BoardGamesAtlas(getContext());
                JSONArray games = boardGamesAtlas.getBoardGamesWithImage("Uno", new BoardGamesAtlas.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONArray games) {
                        gamesList.setText(games.toString());
                    }
                });
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_list, container, false);
    }



}