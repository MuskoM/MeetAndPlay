package com.nvi0.pb.meetandplay.Fragments.UserProfile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nvi0.pb.meetandplay.DataModels.GameDataModel;
import com.nvi0.pb.meetandplay.Fragments.game_list.FavouriteGamesFragment;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.FavouriteGamesAdapter;
import com.nvi0.pb.meetandplay.Utils.GamesMenager;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import java.util.List;


public class UserProfileFragment extends Fragment implements ValueEventListener {

    private static final String TAG = "UserProfileFragment";
    //Firebase
    private FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userAuth.getUid());
    private StorageReference storageAvatarReference = FirebaseStorage.getInstance()
            .getReference("users").child(userAuth.getUid()).child("avatar.jpg");

    RecyclerView favouriteGamesRecyclerView;
    ImageView avatar;
    TextView userNameTextView;
    TextView userAddressTextView;
    TextView userMailTextView;
    TextView userPhoneTextView;
    TextView userDescriptionTextView;

    public UserProfileFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Elements
        userNameTextView = view.findViewById(R.id.profile_username);
        avatar = view.findViewById(R.id.profile_image);
        userAddressTextView = view.findViewById(R.id.profile_details_address);
        userMailTextView = view.findViewById(R.id.profile_details_mail);
        userPhoneTextView = view.findViewById(R.id.profile_details_phone_number);
        userDescriptionTextView = view.findViewById(R.id.profile_description);

        favouriteGamesRecyclerView = view.findViewById(R.id.profile_fragment_favourite_games);
        GamesMenager.getFavouriteGames(new GamesMenager.GamesListener() {
            @Override
            public void onSuccess(List<GameDataModel> gamesList) {
                favouriteGamesRecyclerView.setAdapter(new FavouriteGamesAdapter(gamesList,getContext()));
                favouriteGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onError(DatabaseError error) {
                Snackbar.make(getView(),error.toString(),Snackbar.LENGTH_SHORT);
            }
        });

        userReference.addValueEventListener(this);

        GlideApp.with(this).load(storageAvatarReference)
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(avatar);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    //Firebase ValueEventListener interface
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot data : snapshot.getChildren()
        ) {
            switch (data.getKey()) {

                case "profileName":
                    userNameTextView.setText(data.getValue(String.class));
                    break;
                case "mail":
                    userMailTextView.setText(data.getValue(String.class));
                    break;
                case "description":
                    userDescriptionTextView.setText(data.getValue(String.class));
                    break;
                case "phone":
                    userPhoneTextView.setText(data.getValue(String.class));
                    break;
                case "address":
                    userAddressTextView.setText(data.getValue(String.class));
                    break;

            }
        }

    }



    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile_menu_edit:
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit)
                        .replace(R.id.fragment_container_view, EditUserProfileFragment.class,null)
                        .commit();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}