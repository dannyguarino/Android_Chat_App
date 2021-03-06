package com.example.chatapp.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.chatapp.Adapter.OnItemClick;
import com.example.chatapp.Fragments.ChatsFragment;
import com.example.chatapp.Fragments.ProfileFragment;
import com.example.chatapp.Fragments.UsersFragment;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements OnItemClick {

    //Class members
    //Flag for double back press
    boolean doubleBackToExitPressedOnce = false;

    //UI Components initialization
    //Circular view from main_layout
    CircleImageView profile_image;
    //User Name TextView
    TextView username;
    //Progress Dialog to show progress as Loading or Waiting
    ProgressDialog dialog;
    //Font references
    Typeface MR,MRR;

    //FirebaseUser class reference
    FirebaseUser firebaseUser;
    //Firebase DatabaseReference class reference
    DatabaseReference reference;

    //OnItemClick from Interface
    OnItemClick onItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign this activity to onItemClick
        this.onItemClick = this;

        //Initialize fonts from assets
        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");


        //Initialize Toolbar and set it on action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Get profile Image from resources Files
        profile_image = findViewById(R.id.profile_image);
        //Initialize TabLayout and ViewPager and assign them to their ids
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        //Add onClickListener to profile_img
        profile_image.setOnClickListener(view -> {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();

        });

        //Get username from from UI component id and set font to the text
        username = findViewById(R.id.username);
        username.setTypeface(MR);

        //Get user from firebase auth
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get firebase user reference
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        //Get users from Firebase and make profile
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Get Firebase user details into user class
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profile_img);
                } else {
                    //change this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Firebase chat references
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        dialog = Utils.showLoader(MainActivity.this);

        //Retrieve all chats and users in chat
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    //check for isseen flag in chat
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }

                if (unread == 0){
                    viewPagerAdapter.addFragment(ChatsFragment.newInstance(onItemClick), "Chats");
                } else {
                    viewPagerAdapter.addFragment(ChatsFragment.newInstance(onItemClick), "("+unread+") Chats");
                }

                //Add user Fragment on viewpager
                viewPagerAdapter.addFragment(UsersFragment.newInstance(onItemClick), "Users");
                //add profile fragment on viewpager
                viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);
                if(dialog!=null){
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //add Menu item on Homepage
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Logout after logout is pressed in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            // change this code beacuse your app will crash
            startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }

        return false;
    }

    //Show Fragments as clicked on tabLayout
    @Override
    public void onItemCLick(String uid, View view) {

        ViewProfileActivity viewProfileActivity =
                ViewProfileActivity.newInstance(uid,this);
        viewProfileActivity.show(getSupportFragmentManager(),
                "view_profile");

    }

    //View Pager Adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    //Exit if double back press
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);    }
}
