package com.nvi0.pb.meetandplay.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardGamesAtlas {

    final static String BOARD_GAME_ATLAS_BASE_URL = "https://api.boardgameatlas.com/api/search";
    final static String PARAM_NAME = "https://api.boardgameatlas.com/api/search";
    final static String PARAM_CLIENT_ID = "client_id";
    final static String PARAM_FIELDS = "fields";
    final static String PARAM_ORDER_BY = "order_by";
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
        void onResponse(JSONArray games);
    }

    public JSONArray getBoardGamesWithImage(String name, VolleyResponseListener volleyResponseListener){

        String url = URL + "name="+name+"&client_id=8bhyRCOg0t&order_by=name&fields=name";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            games = response.getJSONArray("games");
                            volleyResponseListener.onResponse(games);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
    },
                error -> {
                    Log.d("APIrequest", error.toString());
                    volleyResponseListener.onError("SOMETHING_WRONG");
                }
        );

        RequestsQueueSingleton.getInstance(context).addToRequestQueue(request);

        return games;
    }

}
