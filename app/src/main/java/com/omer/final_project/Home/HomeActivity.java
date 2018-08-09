package com.omer.final_project.Home;

import android.Manifest;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.omer.final_project.Profile.DisplayProfileFragment;
import com.omer.final_project.R;
import com.omer.final_project.Utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    final int REQUEST_WRITE_STORAGE = 1;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        FirebaseUser currentUser=Model.instance.getCurrentFirebaseUser();
        if (currentUser!=null) {

            Log.d(TAG, "onCreate:*********** the user: "+currentUser.getUid());
            setContentView(R.layout.activity_home);
            setupBottomNavigationView();
            setupToolBar();

            if (savedInstanceState == null) {
                FeedFragment fragment = new FeedFragment();
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                tran.add(R.id.main_container, fragment);
                tran.commit();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                }
            }

        }else {
            final Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolBar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "setupToolBar:");
        ImageView addPostIV=(ImageView) findViewById(R.id.addPost);
        addPostIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: camera icon clicked");
                AddPostFragment addPostFragment=new AddPostFragment();
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
               tran.replace(R.id.main_container,addPostFragment);
               tran.commit();


            }
        });



    }
}