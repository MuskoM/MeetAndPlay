package com.nvi0.pb.meetandplay.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.nvi0.pb.meetandplay.DataModels.GameDataModel;
import com.nvi0.pb.meetandplay.R;

import java.util.List;

public class FavouriteGamesAdapter extends RecyclerView.Adapter<FavouriteGamesAdapter.FavouriteGamesViewHolder> {
    private static final String TAG = "FavouriteGamesAdapter";

    List<GameDataModel> favourite_games;
    Context mContext;
    ConstraintLayout gameListElementView;

    public FavouriteGamesAdapter(List<GameDataModel> favourite_games,Context context) {
        this.favourite_games = favourite_games;
        this.mContext = context;
    }


    @NonNull
    @Override
    public FavouriteGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_element, parent, false);
        return new FavouriteGamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteGamesViewHolder holder, int position) {
        gameListElementView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(favourite_games.get(position).getUrl()));
                mContext.startActivity(webBrowserIntent);
            }
        });

        holder.isFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GamesMenager.removeFavouriteGame(favourite_games.get(position).getName());
            }
        });

        holder.gameName.setText(favourite_games.get(position).getName());
        GlideApp.with(mContext)
                .load(favourite_games.get(position).getImage_url())
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
        return favourite_games.size();
    }

    public class FavouriteGamesViewHolder extends RecyclerView.ViewHolder{
        private final TextView gameName;
        private final ImageView gameIcon;
        private final MaterialButton isFavouriteBtn;

        public FavouriteGamesViewHolder(@NonNull View itemView) {
            super(itemView);
            isFavouriteBtn = itemView.findViewById(R.id.game_list_element_isFavourite);
            gameListElementView = itemView.findViewById(R.id.game_list_element);
            gameIcon = itemView.findViewById(R.id.game_list_item_icon);
            gameName = itemView.findViewById(R.id.game_list_item_label);
        }
    }

}
