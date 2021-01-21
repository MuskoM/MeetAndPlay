package com.nvi0.pb.meetandplay.DataModels;

import java.util.List;

import lombok.Getter;

@Getter
public class UserDataModel {

    private String userId;
    private String profileName;
    private String firstName;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private List<GameDataModel> ownedGames;
    private List<GameSessionDataModel> sessionsPlayed;
    private List<GameSessionDataModel> plannedSessions;
    private List<UserDataModel> friendsList;

    public UserDataModel(String _id, String mail) {
        userId = _id;
        this.mail = mail;
    }

    public String getProfileName() {
        if (firstName != null && lastName != null){
            return  firstName + " " + lastName;
        }else
            return profileName;
    }

    public String createProfileNameFromEmail(String email){

        String profileNameCreated = email.split("@")[0];
        this.profileName = profileNameCreated;
        return profileName;

    }

}
