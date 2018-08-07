package com.omer.final_project.Model;

import android.os.AsyncTask;

import java.util.List;

public class ItemAsynchDao {

    public interface ItemAsynchDaoListener<T>{
        void onComplete(T data);
    }

    static public void getAll(final ItemAsynchDao.ItemAsynchDaoListener<List<Item>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Item>> {
            @Override
            protected List<Item> doInBackground(String... strings) {
                List<Item> iList = AppLocalDb.db.itemDao().getAll();
                return iList;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                listener.onComplete(items);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }


    public static void insertAll(final List<Item> items, final ItemAsynchDao.ItemAsynchDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Item>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Item>... items) {
                for (Item i:items[0]) {
                    AppLocalDb.db.itemDao().insertAll(i);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(items);
    }



}
