package com.nvi0.pb.meetandplay.DataModels;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GameSessionDataModel {

    private UUID sessionId;
    private GameDataModel playedGame;
    private List<UserDataModel> players;
    private UserDataModel host;
    private Date sessionDate;
    private UserDataModel winner;

}
