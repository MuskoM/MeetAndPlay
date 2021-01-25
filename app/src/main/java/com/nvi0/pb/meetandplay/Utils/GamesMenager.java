package com.nvi0.pb.meetandplay.Utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.DataModels.GameDataModel;

import java.util.ArrayList;
import java.util.List;

public class GamesMenager {

    private static final String TAG = "GamesMenager";

    public interface GamesListener{
        void onSuccess(List<GameDataModel> gamesList);
        void onError(DatabaseError error);

    }

    public static void getFavouriteGames(GamesListener gamesListener){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favouriteGamesReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid()).child("favourite_games");

        favouriteGamesReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<GameDataModel> favouriteGames = new ArrayList<>();
                snapshot.getChildren().forEach(e->{
                    favouriteGames.add(new GameDataModel(e.getKey(),
                            e.child("image_url").getValue(String.class),
                            e.child("url").getValue(String.class)));
                    Log.d(TAG, "onDataChange: " + e.getKey());
                });
                gamesListener.onSuccess(favouriteGames);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                gamesListener.onError(error);
            }
        });
    }

    public static void  addFavouriteGame(GameDataModel gameDataModel){
        Log.d(TAG, "addFavouriteGame: GameDataModel " + gameDataModel.toString());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "addFavouriteGame: UserId" + user.getUid());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/users")
                .child(user.getUid()).child("favourite_games").child(gameDataModel.getName());


        dbRef.child("image_url").setValue(gameDataModel.getImage_url());
        dbRef.child("url").setValue(gameDataModel.getUrl());
    }

    public static void removeFavouriteGame(String name){
        Log.d(TAG, "addFavouriteGame: Game to remove " + name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "addFavouriteGame: UserId" + user.getUid());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/users")
                .child(user.getUid()).child("favourite_games");
        dbRef.child(name).removeValue();
    }

}
