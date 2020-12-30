package com.nvi0.pb.meetandplay.Database;


public class Database {

    private static Database instance;

    private static final String CONNECTION_URL = "sdd";
    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String DATABASE = "";

    private Database(){}

    public static Database getInstance(){

        if(instance == null){
            instance = new Database();
        }
        return instance;
    }


}
