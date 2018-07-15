package com.omer.final_project.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final String TAG = "EditProfileFragment";
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    private User profileUser;
    private EditText userNameEt;
    private EditText userEmailEt;
    private EditText userFirstNameEt;
    private EditText userLastNameEt;
    private ImageView userProfilePic;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser= Model.instance.getCurrentFirebaseUser();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit_profile, container, false);

          userNameEt=view.findViewById(R.id.editUserNameEditText);
          userEmailEt=view.findViewById(R.id.editEmailEditText);
          userFirstNameEt=view.findViewById(R.id.editFirstNameEditText);
          userLastNameEt=view.findViewById(R.id.editLastNameEditText);

          userProfilePic=view.findViewById(R.id.profilePicImageView);
          userProfilePic.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //open camera
                  Intent takePictureIntent = new Intent(
                          MediaStore.ACTION_IMAGE_CAPTURE);
                  if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                  }
              }
          });

        Button updateButton=view.findViewById(R.id.editUpdateUserButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final User user=new User(userNameEt.getText().toString(),userEmailEt.getText().toString());
                user.setFirstName(userFirstNameEt.getText().toString());
                user.setLastName(userLastNameEt.getText().toString());
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            profileUser.setProfilePic(url);
                            Log.d(TAG, "%%%%%%%%%%onDone: "+profileUser.getProfilePic());
                            Model.instance.addUser(profileUser);
                            getActivity().getSupportFragmentManager().popBackStack();
                            //finish();
                        }
                    });
                }
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
        userNameEt.setText(profileUser.getUserName());
        userEmailEt.setText(profileUser.getEmail());
        userFirstNameEt.setText(profileUser.getFirstName());
        userLastNameEt.setText(profileUser.getLastName());
        userProfilePic.setTag(profileUser.getUserId());
        Log.d(TAG, "displayUser: "+profileUser.getProfilePic());
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

    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            userProfilePic.setImageBitmap(imageBitmap);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
