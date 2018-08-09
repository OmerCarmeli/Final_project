package com.omer.final_project.Home;

//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

        import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
        import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
        import com.omer.final_project.Model.Post;
import com.omer.final_project.Model.User;
        import com.omer.final_project.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {
    private static final String TAG = "AddPostFragment";
    private static final String ARG_PARAM1 = "param1";
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    private User GUser;
    private ImageView itemPicIV;
    ProgressBar progressBar;


    public AddPostFragment() {
        // Required empty public constructor
    }

    public static AddPostFragment newInstance() {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();

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
                GUser = user;

            }
        },userid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_post, container, false);
        progressBar = view.findViewById(R.id.addPostprogressBar);
        progressBar . setVisibility(View.GONE);
        final EditText itemName=view.findViewById(R.id.nameEditText);
        final EditText itemPrice=view.findViewById(R.id.itemPriceEditText);
        final EditText description=view.findViewById(R.id.descriptionEditText);
        final EditText itemSizeEt=view.findViewById(R.id.sizeEditText);

        itemPicIV=view.findViewById(R.id.itemPicIV);
        itemPicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button saveButtun=view.findViewById(R.id.addPostButtun);
        saveButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String name=itemName.getText().toString();
               String price=itemPrice.getText().toString();
               String desc=description.getText().toString();

                Log.d(TAG, "onClick: name:"+name+" price:"+price+" description:"+desc );

                final Item item=new Item(name,price);
                item.setDescription(desc);
                item.setSize(itemSizeEt.getText().toString());
                final Post post=new Post(GUser.getUserId(),item);
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            item.setPhoto(url);
                            Log.d(TAG, "%%%%%%%%%%onDone: "+item.getPhoto());
                            Model.instance.saveImageToFile(imageBitmap,url);/////////////////////////
                            GUser.addItemToList(item);
                            Model.instance.addItemToUser(GUser,item);
                            Model.instance.addPost(post);
                            getActivity().getFragmentManager().popBackStack();
                            finish();
                        }
                    });
                }


            }
        });

        Button cancelButtun=view.findViewById(R.id.cancelButton);
        cancelButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        return view;
    }

    public void finish(){
        FeedFragment fragment=new FeedFragment();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.main_container,fragment);
        ft.commit();
        //getActivity().getFragmentManager().beginTransaction(). remove(this).commit();
    }
    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            itemPicIV.setImageBitmap(imageBitmap);
        }
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
