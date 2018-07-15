package com.omer.final_project.Model;

import android.os.AsyncTask;

import java.util.List;

public class PostAsynchDao {

    public interface PostAsynchDaoListener<T>{
        void onComplete(T data);
    }
    static public void getAll(final PostAsynchDaoListener<List<Post>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Post>> {
            @Override
            protected List<Post> doInBackground(String... strings) {
                List<Post> pList = AppLocalDb.db.postDao().getAll();
                return pList;
            }

            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);
                listener.onComplete(posts);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }


    public static void insertAll(final List<Post> posts, final PostAsynchDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Post>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Post>... posts) {
                for (Post p:posts[0]) {
                    AppLocalDb.db.postDao().insertAll(p);
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
        task.execute(posts);
    }



}
