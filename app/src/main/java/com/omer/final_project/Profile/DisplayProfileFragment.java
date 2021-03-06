package com.omer.final_project.Profile;

//import android.app.Fragment;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
//import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;


public class DisplayProfileFragment extends Fragment {
    private static final String TAG = "DisplayProfileFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private User profileUser;
    private TextView userNameTv;
    private TextView userEmailTv;
    private TextView userFirstNameTv;
    private TextView userLastNameTv;
    private ImageView userProfilePic;

    private OnFragmentInteractionListener mListener;

    public DisplayProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayProfileFragment newInstance(String param1, String param2) {
        DisplayProfileFragment fragment = new DisplayProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser=Model.instance.getCurrentFirebaseUser();
        Log.d(TAG, "onCreate: &&&&&&&&& uid: "+currentUser.getUid());
        String userid=currentUser.getUid();
        Model.instance.getUserFormDb(new Model.getUserListener() {
            @Override
            public void onSuccess(User user) {
                profileUser = user;

                displayUser();

            }
        },userid);
    }
    //private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_display_profile, container, false);


        userNameTv=view.findViewById(R.id.userNametv);
        userEmailTv=view.findViewById(R.id.userEmailTV);
        userFirstNameTv=view.findViewById(R.id.postUserNameTV);
        userLastNameTv=view.findViewById(R.id.lastNameTv);

        userProfilePic=view.findViewById(R.id.itemPhotoImageView);


        Button editProfileButton=view.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment fragment=new EditProfileFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_container,fragment,"EditProfileFragment");
                ft.addToBackStack("EditProfileFragment");

                ft.commit();
               // getActivity().getSupportFragmentManager().executePendingTransactions();


            }
        });

        Button myItemsButton=view.findViewById(R.id.itemsButton);
        myItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayItemsFragment fragment=new DisplayItemsFragment();
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container,fragment,"DisplayItemsFragment");
                ft.addToBackStack("DisplayItemsFragment");
                ft.commit();
               // getActivity().getSupportFragmentManager().executePendingTransactions();

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void displayUser(){
//        Log.d(TAG, "####################displayUser: "+u.getUserName());
        userNameTv.setText(profileUser.getUserName());
        userEmailTv.setText(profileUser.getEmail());
        userFirstNameTv.setText(profileUser.getFirstName());
        userLastNameTv.setText(profileUser.getLastName());
        Log.d(TAG, "displayUser: "+profileUser.getProfilePic());
        userProfilePic.setTag(profileUser.getUserId());
        if (profileUser.getProfilePic() != null){
            Model.instance.getImage(profileUser.getProfilePic(), new Model.GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (profileUser.getUserId().equals(userProfilePic.getTag()) && imageBitmap != null) {
                        userProfilePic.setImageBitmap(imageBitmap);
                    }
                }
            });
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
