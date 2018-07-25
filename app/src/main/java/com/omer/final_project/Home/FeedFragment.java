package com.omer.final_project.Home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.Post;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    //List<Post> data = new LinkedList<>();
    MyAdapter myAdapter = new MyAdapter();
    ListView list ;
    FeedViewModel dataModel;
    User postUser;


    public FeedFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
       // Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        dataModel.getData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + posts.size());
            }
        });
        /*
        Model.instance.getAllPosts(new Model.GetAllPostsListener() {
            @Override
            public void onSuccess(List<Post> postList) {
                data = postList;
                myAdapter.notifyDataSetChanged();
            }
        });
    */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        list = view.findViewById(R.id.postsListView);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancellGetAllPosts();
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



///////////////////////////////////////////////////////////////
    ////////////////////                  ///////////////////////
    //////////////////        Adapter     //////////////////////////
////////////////////////////////////////////////////////////////////////////


    class MyAdapter extends BaseAdapter {

        public MyAdapter(){
            /*
            Post p1 = new Post("item1",new Item("item one","10.0"));
           Post p2=new Post("item2",new Item("item two","20"));
            data.add(p1);
            data.add(p2);
            */
        }

        @Override
        public int getCount() {
            //return data.size();
            Log.d("TAG","list size:" + dataModel.getData().getValue().size());

            return dataModel.getData().getValue().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_feed_list, null);
            }

          final Post p = dataModel.getData().getValue().get(position);;
            final View finalConvertView = convertView;
            Model.instance.getUserFormDb(new Model.getUserListener() {
                @Override
                public void onSuccess(User user) {
                    postUser = user;

                    //displayUser();

                    TextView userName= finalConvertView.findViewById(R.id.postUserNameTV);
                    TextView itemName= finalConvertView.findViewById(R.id.postItemNameTV);
                    TextView itemPrice= finalConvertView.findViewById(R.id.PostPriceTV);
                    TextView descripsionTv = finalConvertView.findViewById(R.id.descriptionTV);
                    final ImageView userProfilePic= finalConvertView.findViewById(R.id.postUserProfilePic);
                    final ImageView itemPhoto= finalConvertView.findViewById(R.id.postItemPhoto);

                    userName.setText(postUser.getUserName());
                    itemName.setText(p.getItem().getName());
                    itemPrice.setText("Price: "+p.getItem().getPrice()+"$");
                    descripsionTv.setText(p.getItem().getDescription());

                    userProfilePic.setImageResource(R.drawable.ic_android);
                    userProfilePic.setTag(postUser.getUserId());
                    if (postUser.getProfilePic() != null){
                        Model.instance.getImage(postUser.getProfilePic(), new Model.GetImageListener() {
                            @Override
                            public void onDone(Bitmap imageBitmap) {
                                if (postUser.getUserId().equals(userProfilePic.getTag()) && imageBitmap != null) {
                                    userProfilePic.setImageBitmap(imageBitmap);
                                }
                            }
                        });
                    }

                    itemPhoto.setTag(p.getItem().getName());
                    if (p.getItem().getPhoto() != null){
                        Model.instance.getImage(p.getItem().getPhoto(), new Model.GetImageListener() {
                            @Override
                            public void onDone(Bitmap imageBitmap) {
                                if (p.getItem().getName().equals(itemPhoto.getTag()) && imageBitmap != null) {
                                    itemPhoto.setImageBitmap(imageBitmap);
                                }
                            }
                        });
                    }
                }
            },p.getUserId());


            return convertView;
        }
    }

}
