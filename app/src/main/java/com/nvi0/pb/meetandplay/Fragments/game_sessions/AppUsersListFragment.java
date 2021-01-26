package com.nvi0.pb.meetandplay.Fragments.game_sessions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nvi0.pb.meetandplay.DataModels.UserDataModel;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.SessionMenager;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;


public class AppUsersListFragment extends Fragment {

    private static final String TAG = "AppUsersListFragment";
    MessengerFriendsRecycleViewAdapter messengerFriendsRecycleViewAdapter;
    List<UserDataModel> userDataModelList = new ArrayList<>();

    //Elements
    RecyclerView messengerFriendsRecyclerView;

    public AppUsersListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messengerFriendsRecyclerView = view.findViewById(R.id.messenger_friends_recycler_view);

        SessionMenager.getUsersOnApp(new SessionMenager.onUserFriendsFetched() {
            @Override
            public void onSuccess(List<UserDataModel> stringIds) {
                Log.d(TAG, "onSuccess: " + stringIds.toString());
                userDataModelList.addAll(stringIds);
                messengerFriendsRecycleViewAdapter = new MessengerFriendsRecycleViewAdapter(getContext(),userDataModelList); // <---------------
                messengerFriendsRecyclerView.setAdapter(messengerFriendsRecycleViewAdapter);
                messengerFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                messengerFriendsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));
            }

            @Override
            public void onError(String err) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger_friends, container, false);
    }

    public class MessengerFriendsRecycleViewAdapter extends RecyclerView.Adapter<MessengerFriendsRecycleViewAdapter.MessengerFriendsViewHolder> {

        List<UserDataModel> friendsUserDataModelList = new ArrayList<>();
        Context mContext;

        public MessengerFriendsRecycleViewAdapter(@NonNull Context context, List<UserDataModel> friendsList) {

            mContext = context;
            friendsUserDataModelList = friendsList;
            Log.d(TAG, "MessengerFriendsRecycleViewAdapter: firendsUserDataModelList " + friendsUserDataModelList.toString());

        }

        @NonNull
        @Override
        public MessengerFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messenger_friends_list_element,parent,false);
            Log.e(TAG, "onCreateViewHolder: Inflated MessengerFriends View" );
            return new MessengerFriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessengerFriendsViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: bindingView " );

            
            holder.userName.setText(friendsUserDataModelList.get(position).getProfileName());

            StorageReference storageAvatarReference = FirebaseStorage.getInstance()
                    .getReference("users").child(friendsUserDataModelList.get(position).getUserId()).child("avatar.jpg");
            GlideApp.with(mContext)
                    .load(storageAvatarReference)
                    .error(R.drawable.ic_baseline_exit)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e(TAG, "onLoadFailed: LoadingImageFailed" );
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e(TAG, "onResourceReady: Image Loaded");
                            return false;
                        }
                    }).into(holder.avatar);
        }

        @Override
        public int getItemCount() {
            return friendsUserDataModelList.size();
        }


        private class MessengerFriendsViewHolder extends RecyclerView.ViewHolder {

            private final TextView userName;
            private final ImageView avatar;

            public MessengerFriendsViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.messenger_friends_list_element_label);
                avatar = itemView.findViewById(R.id.messenger_friends_list_element_icon);
                Log.d(TAG, "MessengerFriendsViewHolder: " + userName.toString() + " " + avatar.toString());
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.profile_menu_edit == item.getItemId()){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            final EditText input = new EditText(getContext());
            dialogBuilder.setTitle("Input search string").setView(input).setView(input);

            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SessionMenager.addGameSession(input.getText().toString());
                }
            });
            dialogBuilder.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}