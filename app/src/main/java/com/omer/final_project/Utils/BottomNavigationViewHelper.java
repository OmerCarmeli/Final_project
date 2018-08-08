package com.omer.final_project.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.omer.final_project.Home.HomeActivity;
import com.omer.final_project.Profile.ProfileActivity;
import com.omer.final_project.R;


public class BottomNavigationViewHelper {


    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationView){

    }

    public static void enableNavigation(final Context context,BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent intent1=new Intent(context, HomeActivity.class);//ACTIVITY_NUM=0
                        context.startActivity(intent1);
                        break;
                    /*
                    case R.id.ic_search:
                        Intent intent2=new Intent(context, HomeActivity.class);//ACTIVITY_NUM=1
                        context.startActivity(intent2);

                        break;

                    case R.id.ic_circle:
                        Intent intent3=new Intent(context, HomeActivity.class);//ACTIVITY_NUM=2
                        context.startActivity(intent3);

                        break;
                    case R.id.ic_alert:
                        Intent intent4=new Intent(context, HomeActivity.class);//ACTIVITY_NUM=3
                        context.startActivity(intent4);

                        break;
                    */
                    case R.id.ic_android:
                        Intent intent5=new Intent(context, ProfileActivity.class);//ACTIVITY_NUM=4
                        context.startActivity(intent5);

                        break;
                }



                return false;
            }
        });
    }
}
