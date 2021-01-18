package com.nvi0.pb.meetandplay.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.GlideApp;
import com.nvi0.pb.meetandplay.Utils.Glider;


public class UserProfileFragment extends Fragment {

    private StorageReference mStorageRef;
    ImageView avatar;

    public UserProfileFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userName = requireArguments().getString("User name");
        TextView profileName = view.findViewById(R.id.profile_Name);
        avatar = view.findViewById(R.id.profile_image);
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://meetandplay-ddac1.appspot.com/users/test_user/images.jpg");
        GlideApp.with(this).load(mStorageRef).error(R.drawable.ic_launcher_background).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("GLIDE ERROR","Error", e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.e("GLIDE ERROR","Ready");
                return false;
            }
        }).into(avatar);
        profileName.setText(userName);
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
}