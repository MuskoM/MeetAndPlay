package com.nvi0.pb.meetandplay.Fragments.game_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.DataModels.GameDataModel;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.FavouriteGamesAdapter;
import com.nvi0.pb.meetandplay.Utils.GamesMenager;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;


public class FavouriteGamesFragment extends Fragment {
    private static final String TAG = "FavouriteGamesFragment";
    RecyclerView favouriteGamesrecyclerView;

    public FavouriteGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: FAVOURITE GAMES VIEW CREATED!!");
        super.onViewCreated(view, savedInstanceState);
        favouriteGamesrecyclerView = view.findViewById(R.id.favourite_games_list_container);
        GamesMenager.getFavouriteGames(new GamesMenager.GamesListener() {
            @Override
            public void onSuccess(List<GameDataModel> gamesList) {
                favouriteGamesrecyclerView.setAdapter(new FavouriteGamesAdapter(gamesList,getContext()));
                favouriteGamesrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onError(DatabaseError error) {
                Snackbar.make(getView(),error.toString(),Snackbar.LENGTH_SHORT);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_games, container, false);
    }



}