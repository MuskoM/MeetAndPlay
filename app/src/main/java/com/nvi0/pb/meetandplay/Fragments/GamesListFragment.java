package com.nvi0.pb.meetandplay.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nvi0.pb.meetandplay.DataModels.GameDataModel;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.BoardGamesAtlas;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;


public class GamesListFragment extends Fragment {

    private static final String TAG = "GamesListFragment";

    List<GameDataModel> gameDataModelList = new ArrayList<>();
    BoardGamesAtlas boardGamesAtlas;
    RecyclerView gamesListRecyclerView;
    private GameListAdapter gameListAdapter;
    ConstraintLayout gameListElementView;

    public GamesListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesListRecyclerView = view.findViewById(R.id.games_list_container);
        boardGamesAtlas = new BoardGamesAtlas(getContext());
        Log.d(TAG, boardGamesAtlas.toString() + " Initalized");

        boardGamesAtlas.getBoardGamesWithImage("Uno", new BoardGamesAtlas.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Didn't get the API response");
            }

            @Override
            public void onResponse(JSONArray games) throws JSONException {
                Log.d(TAG, "Got a response from API");
                for (int i = 0; i < games.length(); i++) {
                    JSONObject obj = games.getJSONObject(i);
                    gameDataModelList.add(new GameDataModel(obj.getString("name"),
                            obj.getString("image_url"), obj.getString("url")));
                }
                gameListAdapter = new GameListAdapter(getContext(), gameDataModelList);
                gamesListRecyclerView.setAdapter(gameListAdapter);
                gamesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                gamesListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                Log.d(TAG, "Returned from API: " + games.toString());
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

    private class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {

        private static final String TAG = "GameListAdapter";
        List<GameDataModel> games;
        Context mContext;

        public GameListAdapter(Context mContext, List<GameDataModel> mGames) {
            this.games = mGames;
            this.mContext = mContext;
            Log.d(TAG, "Initialized with data: " + mGames);
            Log.d(TAG, "Initialized with data: " + mContext);
        }

        @NonNull
        @Override
        public GameListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_element, parent, false);
            Log.d(TAG, "Inflated GameListView element");
            return new GameListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameListViewHolder holder, int position) {
            Log.d(TAG, "Binding View");

            gameListElementView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent webBrowserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(gameDataModelList.get(position).getUrl()));
                    startActivity(webBrowserIntent);
                }
            });

            holder.gameName.setText(gameDataModelList.get(position).getName());
                GlideApp.with(mContext)
                        .load(gameDataModelList.get(position).getImage_url())
                        .error(R.drawable.ic_launcher_background)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e(TAG, "Glide failed", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.e(TAG, "Glide resource ready");
                                return false;
                            }
                        }).into(holder.gameIcon);
        }

        @Override
        public int getItemCount() {
            return games.size();
        }

        private class GameListViewHolder extends RecyclerView.ViewHolder {
            private final TextView gameName;
            private final ImageView gameIcon;

            public GameListViewHolder(@NonNull View itemView) {
                super(itemView);
                gameListElementView = itemView.findViewById(R.id.game_list_element);
                gameIcon = itemView.findViewById(R.id.game_list_item_icon);
                gameName = itemView.findViewById(R.id.game_list_item_label);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "Fragment: " + menu + " Inflater:" + inflater);
        inflater.inflate(R.menu.game_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.game_list_menu_search:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());
                dialogBuilder.setTitle("Input search string").setView(input).setView(input);

                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), input.getText(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, boardGamesAtlas.toString() + " Initalized inside Option Search");

                        boardGamesAtlas.getBoardGamesWithImage(input.getText().toString(), new BoardGamesAtlas.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Didn't get the API response");
                            }

                            @Override
                            public void onResponse(JSONArray games) throws JSONException {
                                Log.d(TAG, "Got a response from API");
                                gameDataModelList.clear();
                                for (int i = 0; i < games.length(); i++) {
                                    JSONObject obj = games.getJSONObject(i);
                                    gameDataModelList.add(new GameDataModel(obj.getString("name"),
                                            obj.getString("image_url"), obj.getString("url")));
                                }
                                gameListAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Returned from API: " + games.toString());
                            }
                        });
                    }
                });
                dialogBuilder.show();
                Log.d(TAG,"Changed array " + gameDataModelList.get(0).toString());
                return true;
            case R.id.game_list_menu_order_by:
                Toast.makeText(getContext(), "Order By", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}