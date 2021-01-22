package com.nvi0.pb.meetandplay.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvi0.pb.meetandplay.DataModels.UserDataModel;

import java.util.ArrayList;
import java.util.List;


public class FriendsMenager {

    private static final String TAG = "FriendsMenager";
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static DatabaseReference userFriendsReference = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(user.getUid()).child("friends_ids");

    public interface onUserFriendsFetched{
        void onSuccess(List<UserDataModel> stringIds);
        void onError(String err);
    }

    public static void getUserFriends(onUserFriendsFetched userFriendsFetchedCallback){
        Log.d(TAG, "getUserFriends: StartedTheOperation");
        userFriendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserDataModel> stringIds = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()
                     ) {

                    UserDataModel.convertToUserDataModel(ds.getValue(String.class), new UserDataModel.convertUserCallback() {
                        @Override
                        public void onSuccess(UserDataModel user) {
                            stringIds.add(user);
                            Log.e(TAG, "onSuccess: helperFun inside " + user );
                            userFriendsFetchedCallback.onSuccess(stringIds);
                        }

                        @Override
                        public void onError(String err) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userFriendsFetchedCallback.onError("Didn't fetch Any friends :(");
                Log.e(TAG, "onCancelled: Error in getting on of the Friends" );
            }
        });

    }


    public static void addUserFriend(String Uid){

    }


    public static void removeUserFriend(String Uid){

    }

}
