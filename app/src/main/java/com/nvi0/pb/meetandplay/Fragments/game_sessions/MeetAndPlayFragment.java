package com.nvi0.pb.meetandplay.Fragments.game_sessions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nvi0.pb.meetandplay.Fragments.game_list.GamesListFragment;
import com.nvi0.pb.meetandplay.R;


public class MeetAndPlayFragment extends Fragment {

    private static final String TAG = "MessengerFragment";
    DatabaseReference messagesDbReference = FirebaseDatabase.getInstance().getReference("message");

    //Elements
    TabLayout messengerTabLayout;
    ViewPager2 viewPager;
    MessengerFragmentAdapter messengerFragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messengerFragmentAdapter = new MessengerFragmentAdapter(this);
        viewPager = view.findViewById(R.id.messenger_pager);
        viewPager.setAdapter(messengerFragmentAdapter);

        messengerTabLayout = view.findViewById(R.id.messenger_pager_tab_layout);
        new TabLayoutMediator(messengerTabLayout, viewPager,
                (tab, position) -> {
                if (position == 0)
                    tab.setText("Friends");
                else
                    tab.setText("List");
                }).attach();

    }

    public class MessengerFragmentAdapter extends FragmentStateAdapter {

        public MessengerFragmentAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            if (position == 0)
                fragment = new AppUsersListFragment();
            else
                fragment = new GamesListFragment();

            return fragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}