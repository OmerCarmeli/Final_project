package com.omer.final_project.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Home.FeedFragment;
import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends Fragment {
    private static final String TAG = "EditItemFragment";
    static final int REQUEST_IMAGE_CAPTURE = 4;


    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "itemID";
    private String mParam1;
    private String mParam2;

    private Item thisItem;
    private User profileUser;
    private EditText itemNameEt;
    private EditText itemPriceEt;
    private EditText itemDescNameEt;
    private EditText itemSizeET;
    //private EditText userLastNameEt;
    private ImageView itemPhotoIV;
    Bitmap imageBitmap;
    boolean photoChanged=false;
    String userId;
    private OnFragmentInteractionListener mListener;

    public EditItemFragment() {
        // Required empty public constructor
    }

    public static EditItemFragment newInstance(String param1, String param2) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

     ;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d(TAG, "onCreate: "+mParam1);
            Log.d(TAG, "onCreate: "+mParam2);
        }

        FirebaseUser currentUser= Model.instance.getCurrentFirebaseUser();
        Log.d(TAG, "onCreate: &&&&&&&&& uid: "+currentUser.getUid());
         userId=currentUser.getUid();
        Model.instance.getUserFormDb(new Model.getUserListener() {
            @Override
            public void onSuccess(User user) {
                profileUser = user;
                thisItem=profileUser.getItem(mParam2);
                Log.d(TAG, "onSuccess44444444444444444: "+thisItem.getItemId());
                Activity activity = getActivity();
                if(activity instanceof ProfileActivity){
                    ProfileActivity myactivity = (ProfileActivity) activity;
                    myactivity.setupToolBar();
                }
                displayItem();

            }
        },userId);

    }

    public void displayItem(){

        itemNameEt.setText(thisItem.getName());
        itemPriceEt.setText(thisItem.getPrice());
        itemDescNameEt.setText(thisItem.getDescription());
        itemPhotoIV.setTag(thisItem.getItemId());
        itemSizeET.setText(thisItem.getSize());

        if (thisItem.getPhoto() != null){
            Model.instance.getImage(thisItem.getPhoto(), new Model.GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (thisItem.getItemId().equals(itemPhotoIV.getTag()) && imageBitmap != null) {
                        itemPhotoIV.setImageBitmap(imageBitmap);
                    }
                }
            });
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            itemPhotoIV.setImageBitmap(imageBitmap);
            photoChanged=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit_item, container, false);
        itemNameEt=view.findViewById(R.id.nameEditTexte);
        itemPriceEt=view.findViewById(R.id.itemPriceEditTexte);
        itemDescNameEt=view.findViewById(R.id.descriptionEditTexte);
        itemSizeET=view.findViewById(R.id.sizeEditTexte);
        itemPhotoIV=view.findViewById(R.id.itemPicIVe);

        itemPhotoIV.setOnClickListener(new View.OnClickListener() {
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

        Button updateButton=view.findViewById(R.id.updateItemButtun);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: update button clicked");

                final Item item=new Item(itemNameEt.getText().toString(),itemPriceEt.getText().toString());
                item.setItemId(thisItem.getItemId());
                item.setDescription(itemDescNameEt.getText().toString());
                item.setSize(itemSizeET.getText().toString());


                if (photoChanged) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            item.setPhoto(url);
                            Log.d(TAG, "%%%%%%%%%%onDone photo changed: ");
                            Model.instance.updateItemToUser(profileUser,item);
                            getActivity().getSupportFragmentManager().popBackStack();
                    //        finish();
                        }
                    });
                }else {
                    item.setPhoto(thisItem.getPhoto());
                    Log.d(TAG, "%%%%%%%%%%onDone no change: ");
                    Model.instance.updateItemToUser(profileUser,item);
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            }
        });

        Button deleteButton=view.findViewById(R.id.deleteButtone);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Model.instance.removePost(thisItem,profileUser.getUserId());
            //getActivity().getSupportFragmentManager().popBackStack();

            finish();
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



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void finish(){
        DisplayItemsFragment fragment=new DisplayItemsFragment();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.main_container,fragment);
        ft.commit();
        ft.addToBackStack("DisplayItemsFragment");
        //getActivity().getFragmentManager().beginTransaction(). remove(this).commit();
    }
}
