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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.DataModels.UserDataModel;

import java.util.ArrayList;
import java.util.List;


public class SessionMenager {

    private static final String TAG = "SessionMenager";

    public interface onUserFriendsFetched{
        void onSuccess(List<UserDataModel> stringIds);
        void onError(String err);
    }

    public static void getUsersOnApp(onUserFriendsFetched userFriendsFetchedCallback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userFriendsReference = FirebaseDatabase.getInstance()
                .getReference("users");
        Log.d(TAG, "getUserFriends: USER_ID " + user.getUid());
        Log.d(TAG, "getUserFriends: StartedTheOperation");
        UserDataModel.converToUserDataModelList(userFriendsReference, new UserDataModel.convertUsersToListCallback() {
            @Override
            public void onSuccess(List<UserDataModel> user) {
                userFriendsFetchedCallback.onSuccess(user);
            }

            @Override
            public void onError(String err) {
                userFriendsFetchedCallback.onError("Error fetchingUsers");
            }
        });

    }


    public static void addGameSession(String name){
        Log.d(TAG, "onDataChange: AddUserFriend ");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userFriendsReference = FirebaseDatabase.getInstance()
                .getReference("users");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("profileName").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: User: " + userSnapshot.toString());
                    userFriendsReference.child(user.getUid()).child("friends_ids") .push().setValue(userSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });



    }


    public static void removeGameSession(String Uid){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userFriendsReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid()).child("friends_ids");

    }

}
