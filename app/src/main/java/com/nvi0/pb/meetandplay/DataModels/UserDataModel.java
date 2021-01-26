package com.nvi0.pb.meetandplay.DataModels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.mongodb.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataModel {

    private static final String TAG = "UserDataModel";
    private static DatabaseReference usersReference = FirebaseDatabase.getInstance()
            .getReference("users");

    private String userId;
    private String profileName;
    private String firstName;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private String description;
    private String address;
    private List<GameDataModel> ownedGames;
    private List<UserDataModel> friendsList;

    public UserDataModel(String _id, String mail) {
        userId = _id;
        this.mail = mail;
    }

    public String getProfileName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else
            return profileName;
    }

    public String createProfileNameFromEmail(String email) {

        String profileNameCreated = email.split("@")[0];
        this.profileName = profileNameCreated;
        return profileName;

    }

    public interface convertUserCallback {
        void onSuccess(UserDataModel user);

        void onError(String err);
    }

    public interface convertUsersToListCallback {
        void onSuccess(List<UserDataModel> user);

        void onError(String err);
    }


    public static void converToUserDataModelList(DatabaseReference dbRef, convertUsersToListCallback convertUsersToListCallback) {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserDataModel> userDataModels = new ArrayList<>();
                final HashMap<String, String> credentials = new HashMap<>();

                for (DataSnapshot ds : snapshot.getChildren()
                ) {

                    for (DataSnapshot user_details : ds.getChildren()
                    ) {
                        Log.d(TAG, "onDataChange: KEY: " + user_details.getValue());
                        switch (user_details.getKey()) {
                            case "profileName":
                                credentials.put("profileName", user_details.getValue(String.class));
                                break;
                            case "mail":
                                credentials.put("mail", user_details.getValue(String.class));
                                break;
                            case "description":
                                credentials.put("description", user_details.getValue(String.class));
                                break;
                            case "phone":
                                credentials.put("phone", user_details.getValue(String.class));
                                break;
                            case "address":
                                credentials.put("address", user_details.getValue(String.class));
                                break;

                        }
                    }

                    UserDataModel userDataModel = new UserDataModel(ds.getKey(), credentials.get("mail"));
                    userDataModel.profileName = credentials.get("profileName");
                    userDataModel.description = credentials.get("description");
                    userDataModel.phoneNumber = credentials.get("phone");
                    userDataModel.address = credentials.get("address");
                    userDataModels.add(userDataModel);

                }

                convertUsersToListCallback.onSuccess(userDataModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static UserDataModel convertToUserDataModel(String Uid, convertUserCallback callback) {
        final HashMap<String, String> credentials = new HashMap<>();
        final HashMap<String, UserDataModel> userModels = new HashMap<>();

        Log.d(TAG, "REFERENCE " + usersReference.child(Uid).toString());

        usersReference.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user_details : snapshot.getChildren()
                ) {
                    Log.d(TAG, "onDataChange: KEY: " + user_details.getValue());
                    switch (user_details.getKey()) {
                        case "profileName":
                            credentials.put("profileName", user_details.getValue(String.class));
                            break;
                        case "mail":
                            credentials.put("mail", user_details.getValue(String.class));
                            break;
                        case "description":
                            credentials.put("description", user_details.getValue(String.class));
                            break;
                        case "phone":
                            credentials.put("phone", user_details.getValue(String.class));
                            break;
                        case "address":
                            credentials.put("address", user_details.getValue(String.class));
                            break;

                    }
                }

                UserDataModel userDataModel = new UserDataModel(Uid, credentials.get("mail"));
                userDataModel.profileName = credentials.get("profileName");
                userDataModel.description = credentials.get("description");
                userDataModel.phoneNumber = credentials.get("phone");
                userDataModel.address = credentials.get("address");

                callback.onSuccess(userDataModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Couldn't convert the data");
            }
        });
        return userModels.get("convertedUser");

    }

    @Override
    public String toString() {
        return "UserDataModel{" +
                "userId='" + userId + '\'' +
                ", profileName='" + profileName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", ownedGames=" + ownedGames +
                ", friendsList=" + friendsList +
                '}';
    }
}
