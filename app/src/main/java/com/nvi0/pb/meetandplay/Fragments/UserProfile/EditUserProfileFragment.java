package com.nvi0.pb.meetandplay.Fragments.UserProfile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nvi0.pb.meetandplay.R;
import com.nvi0.pb.meetandplay.Utils.GlideApp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class EditUserProfileFragment extends Fragment implements ValueEventListener {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "EditUserProfileFragment";

    //Firebase
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageAvatarReference = FirebaseStorage.getInstance()
            .getReference("users").child(user.getUid()).child("avatar.jpg");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference userProfileDataReference = FirebaseDatabase.getInstance()
            .getReference()
            .child("users")
            .child(firebaseUser.getUid());

    Uri outputFileUri;

    ImageView profileEditAvatar;
    EditText profileEditprofileName;
    EditText profileEditDescription;
    EditText profileEditAddress;
    EditText profileEditMail;
    EditText profileEditPhone;
    Button saveChangesButton;
    private Uri pictureFilePath;

    public EditUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Find elements
        profileEditAddress = view.findViewById(R.id.profile_edit_details_address);
        profileEditAvatar = view.findViewById(R.id.profile_edit_image);
        profileEditprofileName = view.findViewById(R.id.profile_edit_username);
        profileEditDescription = view.findViewById(R.id.profile_edit_description);
        profileEditMail = view.findViewById(R.id.profile_edit_details_mail);
        profileEditPhone = view.findViewById(R.id.profile_edit_details_phone_number);
        saveChangesButton = view.findViewById(R.id.profile_edit_save_button);

        refreshAvatar();

        //Hint listener
        userProfileDataReference.addValueEventListener(this);

        profileEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    try {
                        createPhotoName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFilePath);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("profileName", profileEditprofileName.getText().toString());
                data.put("mail", profileEditMail.getText().toString());
                data.put("description", profileEditDescription.getText().toString());
                data.put("phone", profileEditPhone.getText().toString());
                data.put("address", profileEditAddress.getText().toString());
                userProfileDataReference.updateChildren(data);

                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                        .replace(R.id.fragment_container_view, UserProfileFragment.class, null)
                        .commit();

            }
        });

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot data : snapshot.getChildren()
        ) {
            switch (data.getKey()) {

                case "profileName":
                    profileEditprofileName.setText(data.getValue(String.class));
                    break;
                case "mail":
                    profileEditMail.setText(data.getValue(String.class));
                    break;
                case "description":
                    profileEditDescription.setText(data.getValue(String.class));
                    break;
                case "phone":
                    profileEditPhone.setText(data.getValue(String.class));
                    break;
                case "address":
                    profileEditAddress.setText(data.getValue(String.class));
                    break;

            }
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAvatar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();

                try {
                    createPhotoName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFilePath);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            storageAvatarReference.putFile(pictureFilePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "Failed to upload picture to cloud storage");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),
                            "Image has been uploaded to cloud storage",
                            Toast.LENGTH_SHORT).show();
                }
            });
            refreshAvatar();
        }
    }

    private void createPhotoName() throws IOException {

        String pictureFile = "avatar";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);

        Uri imageUri = FileProvider.getUriForFile(getContext(),
                "com.nvi0.pb.meetandplay.provider", image);

        pictureFilePath = imageUri;

    }

    private void refreshAvatar() {
        GlideApp.with(this).load(storageAvatarReference).error(R.drawable.ic_launcher_background).listener(new RequestListener<Drawable>() {
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
        }).into(profileEditAvatar);
    }


}