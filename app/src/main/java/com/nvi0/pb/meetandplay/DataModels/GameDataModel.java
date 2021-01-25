package com.nvi0.pb.meetandplay.DataModels;


import java.util.UUID;

import lombok.Getter;

@Getter
public class GameDataModel {

    private String gameId;
    private String name;
    private String image_url;
    private String url;

    public GameDataModel(String name, String image_url, String url) {
        this.gameId = UUID.nameUUIDFromBytes(name.getBytes()).toString();
        this.name = name;
        this.image_url = image_url;
        this.url = url;
    }

    public static GameDataModel convertToGameDataModel(){



        return null;
    }

}
