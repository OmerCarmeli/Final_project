package com.omer.final_project.Profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Home.FeedViewModel;
import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.Post;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayItemsFragment extends Fragment {


    private static final String TAG = "DisplayItemsFragment";
    private OnFragmentInteractionListener mListener;
    MyAdapter myAdapter=new MyAdapter();
    ListView list ;
    ItemsViewModel dataModel;
    FirebaseUser currentUser;
    String userID;
    String itemID;
    Bundle bundle=new Bundle();
    public DisplayItemsFragment() {

    }


    public static DisplayItemsFragment newInstance(String param1, String param2) {
        DisplayItemsFragment fragment = new DisplayItemsFragment();
        //  Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser=Model.instance.getCurrentFirebaseUser();
        dataModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        dataModel.getData().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + items.size());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_display_items, container, false);

        list = view.findViewById(R.id.itemsListView);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
                Item it = dataModel.getData().getValue().get(i);
                userID="123";
                EditItemFragment fragment=new EditItemFragment();
               // Bundle bundle=new Bundle();

                bundle.putString("itemID",it.getItemId());
                Log.d(TAG, "545454545454onItemClick: "+itemID);
                bundle.putString("userID","123");
                fragment.setArguments(bundle);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_container,fragment,"EditItemFragment");
                ft.addToBackStack("EditItemFragment");
                ft.commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });
/*
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                }
                return false;
            }
        });
*/

        return view ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancellGetMyItems();
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

        ArrayList<Item> iList=new ArrayList<>();
        public MyAdapter() {
        }

        @Override
        public int getCount() {
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_items_list, null);
            }

            final Item i = dataModel.getData().getValue().get(position);
            //final View finalConvertView = convertView;
            itemID=i.getItemId();
            Log.d(TAG, "555555555getView: "+itemID);
            TextView itemNameTV=convertView.findViewById(R.id.itemDNameTV);
            TextView itemPriceTV=convertView.findViewById(R.id.itemDPriceTV);
            TextView itemDescTV=convertView.findViewById(R.id.itemDescriptionDTV);
            TextView itemSizeTv=convertView.findViewById(R.id.itemSizeTextViewi);
            final ImageView itemPhoto= convertView.findViewById(R.id.itemDPhoto);

            itemNameTV.setText(i.getName());
            itemPriceTV.setText(i.getPrice()+"$");
            itemDescTV.setText(i.getDescription());
            itemSizeTv.setText("Size: "+i.getSize());
            itemPhoto.setTag(i.getName());
            if (i.getPhoto() != null){
                Model.instance.getImage(i.getPhoto(), new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (i.getName().equals(itemPhoto.getTag()) && imageBitmap != null) {
                            itemPhoto.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }
/*
            final Button dButton=convertView.findViewById(R.id.Dbutton);
            dButton.setTag(i);

            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.removePost((Item)dButton.getTag(),currentUser.getUid());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
*/

            return convertView;
        }
    }









}