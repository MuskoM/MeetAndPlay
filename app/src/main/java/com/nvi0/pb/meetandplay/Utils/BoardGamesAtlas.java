package com.nvi0.pb.meetandplay.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class BoardGamesAtlas {

    private static final String TAG = "BoardGamesAtlas";
    final static String URL = "https://api.boardgameatlas.com/api/search?";

    Uri buildUri;

    JSONArray games = null;
//    List<BoardGameModel> queriedGames;

    Context context;

    public BoardGamesAtlas(Context context){
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(JSONArray games) throws JSONException;
    }

    public JSONArray getBoardGamesWithImage(String name, VolleyResponseListener volleyResponseListener){

        String url = URL + "name="+name+"&client_id=8bhyRCOg0t&fields=name,image_url,url,&limit=10";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse Triggered");
                            games = response.getJSONArray("games");
                            Log.d(TAG, games.toString());
                            volleyResponseListener.onResponse(games);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
    },
                error -> {
                    Log.d(TAG, error.toString());
                    volleyResponseListener.onError("SOMETHING_WRONG");
                }
        );

        RequestsQueueSingleton.getInstance(context).addToRequestQueue(request);

        return games;
    }


}
