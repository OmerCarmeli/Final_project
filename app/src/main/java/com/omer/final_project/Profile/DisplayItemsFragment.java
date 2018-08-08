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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.omer.final_project.Home.FeedViewModel;
import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.Post;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

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



    private OnFragmentInteractionListener mListener;
    MyAdapter myAdapter=new MyAdapter();
    ListView list ;
    ItemsViewModel dataModel;
    User currentUser;
    String userID;
    String itemID;

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
//
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

                //build item from view
                 userID="123";

                EditItemFragment fragment=new EditItemFragment();
                Bundle bundle=new Bundle();
                bundle.putString("userID","123");
                bundle.putString("itemID",itemID);
                fragment.setArguments(bundle);
                FragmentTransaction ft=getFragmentManager().beginTransaction();
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

            final Item i = dataModel.getData().getValue().get(position);;
            //final View finalConvertView = convertView;
            itemID=i.getItemId();
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


            //////////////////////////////////////////////////////////////////////
            /*
            Model.instance.getUserFormDb(new Model.getUserListener() {
                @Override
                public void onSuccess(User user) {
                    currentUser = user;

                    //displayitem();
                    TextView itemNameTV=finalConvertView.findViewById(R.id.itemDNameTV);
                    TextView itemPriceTV=finalConvertView.findViewById(R.id.itemDPriceTV);
                    TextView itemDescTV=finalConvertView.findViewById(R.id.itemDescriptionDTV);
                    final ImageView itemPhoto= finalConvertView.findViewById(R.id.itemDPhoto);

                    itemNameTV.setText();


                }
            },Model.instance.getCurrentFirebaseUser().getUid());
        */




            return convertView;
        }
    }









}
