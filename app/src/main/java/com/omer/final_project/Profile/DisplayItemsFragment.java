package com.omer.final_project.Profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.omer.final_project.Model.User;
import com.omer.final_project.R;

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
    //ItemsViewModel dataModel;
    User currentUser;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_display_items, container, false);

        return view ;
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
            return 0;
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
            return null;
        }
    }









}
