package com.nvi0.pb.meetandplay.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.BoardGamesAtlas;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import org.json.JSONArray;
import org.json.JSONException;



public class GamesListFragment extends Fragment {

    private static final String TAG = "GamesListFragment";

    JSONArray gamesJSONArray;
    Button requestBtn;
    BoardGamesAtlas boardGamesAtlas;
    TextView gamesList;
    RecyclerView gamesListRecyclerView;

    public GamesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestBtn = view.findViewById(R.id.request_button);
        gamesList = view.findViewById(R.id.gameList_label);
        gamesListRecyclerView = view.findViewById(R.id.games_list_container);

        boardGamesAtlas = new BoardGamesAtlas(getContext());
        Log.d(TAG, boardGamesAtlas.toString() + " Initalized");

        boardGamesAtlas.getBoardGamesWithImage("Uno", new BoardGamesAtlas.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                Log.d(TAG, "Didn't get the API response");
            }

            @Override
            public void onResponse(JSONArray games) {
                Log.d(TAG, "Got a response from API");
                gamesJSONArray = games;
                GameListAdapter gameListAdapter = new GameListAdapter(getContext(), gamesJSONArray );
                gamesListRecyclerView.setAdapter(gameListAdapter);
                gamesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                gamesListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                Log.d(TAG, "Returned from API: " +  games.toString());
            }
        });


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_list, container, false);
    }

    //Recycler View Adapter

    private class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder>{

        private static final String TAG = "GameListAdapter";
        JSONArray games;
        Context mContext;

        public GameListAdapter(Context mContext, JSONArray mGames) {
            this.games = mGames;
            this.mContext = mContext;
            Log.d(TAG, "Initialized with data: " + mGames);
            Log.d(TAG, "Initialized with data: " + mContext);
        }

        @NonNull
        @Override
        public GameListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_element,parent,false);
            Log.d(TAG, "Inflated GameListView element");
            return new GameListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameListViewHolder holder, int position) {
            Log.d(TAG, "Binding View");
            try {
                holder.gameName.setText(games.getJSONObject(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                GlideApp.with(mContext)
                        .load(games.getJSONObject(position).getString("image_url"))
                        .error(R.drawable.ic_launcher_background)
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG,"Glide failed", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e(TAG,"Glide resource ready");
                        return false;
                    }
                }).into(holder.gameIcon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return games.length();
        }

        private class GameListViewHolder extends RecyclerView.ViewHolder{
            private final TextView gameName;
            private final ImageView gameIcon;

            public GameListViewHolder(@NonNull View itemView) {
                super(itemView);
                gameIcon = itemView.findViewById(R.id.game_list_item_icon);
                gameName = itemView.findViewById(R.id.game_list_item_label);
            }
        }

    }


}