package com.omer.final_project.Profile;

//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Login.LoginActivity;
import com.omer.final_project.Model.Model;
import com.omer.final_project.R;
import com.omer.final_project.Utils.BottomNavigationViewHelper;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM=4;
    boolean backBtn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolBar();
        setupBottomNavigationView();
        FirebaseUser currentUser=Model.instance.getCurrentFirebaseUser();
        Log.d(TAG, "onCreate: &&&&&&&&& uid: "+currentUser.getUid());

        if (savedInstanceState == null) {
            DisplayProfileFragment fragment=new DisplayProfileFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
           // tran.addToBackStack("");
            tran.commit();
        }

    }
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void setupToolBarFragment(){

    }
    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "setupToolBar: =======================================================");
        ImageView logOutIV=(ImageView) findViewById(R.id.menu);
        final Intent intent=new Intent(this, LoginActivity.class);
        logOutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: camera icon clicked");
                Model.instance.signOutUser();
               // finish();
                startActivity(intent);
            }
        });


/*
        ImageView profileMenu=(ImageView) findViewById(R.id.profileMenu1);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
 */


    }


}
