package com.omer.final_project.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Home.HomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final String TAG = "Model";
    public static Model instance = new Model();
    ModelFirebase modelFirebase;
    private Model(){
        modelFirebase = new ModelFirebase();
    }
    public void addPost(Post post) {
        modelFirebase.addPost(post);
    }

    public void removePost(Item item,String userId){modelFirebase.removePost(item,userId);}

    //public void removeItem(Item item){modelFirebase.removeItem(item);}

    public void addUser(User user){modelFirebase.addUserToDb(user);}

    public void addItemToUser(User user ,Item item){modelFirebase.addItemToUser(user,item);}
    public void updateItemToUser(User user ,Item item){modelFirebase.updateItemToUser(user,item);}
    public FirebaseUser getCurrentFirebaseUser(){
        return modelFirebase.getCurrentUser();

    }

    public interface getUserListener{
        public void onSuccess(User user);
    }

    public void getUserFormDb(final getUserListener listener, String userId ){
         modelFirebase.getUserFromDb(new ModelFirebase.getUserListener() {
             @Override
             public void onSuccess(User user) {
                 listener.onSuccess(user);
             }
         },userId);
    }


    public interface getItemListener{
        public void onSuccess(Item item);
    }

    public void getItemFormDb(final getUserListener listener, String userId,String itemId ){

        modelFirebase.getUserFromDb(new ModelFirebase.getUserListener() {
            @Override
            public void onSuccess(User user) {
                listener.onSuccess(user);
            }
        },userId);
    }

    public void signOutUser(){
        modelFirebase.signOut();
    }

    /////LiveData//////////////////

    class PostListData extends MutableLiveData<List<Post>> {

        @Override
        protected void onActive() {
            super.onActive();
            // new thread tsks
            // 1. get the students list from the local DB
            PostAsynchDao.getAll(new PostAsynchDao.PostAsynchDaoListener<List<Post>>() {
                @Override
                public void onComplete(List<Post> data) {
                    // 2. update the live data with the new student list
                    setValue(data);
                    Log.d("TAG","got students from local DB " + data.size());

                    // 3. get the posts list from firebase
                    modelFirebase.getAllPosts(new ModelFirebase.GetAllPostsListener() {
                        @Override
                        public void onSuccess(List<Post> postslist) {
                            // 4. update the live data with the new student list
                            setValue(postslist);
                            Log.d("TAG","got students from firebase " + postslist.size());

                            // 5. update the local DB
                            PostAsynchDao.insertAll(postslist, new PostAsynchDao.PostAsynchDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    Log.d(TAG, "onComplete: insert data to ROOM!");
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancellGetAllPosts();
            Log.d("TAG","cancellGetAllPosts");
        }

        public PostListData() {
            super();
            //setValue(AppLocalDb.db.studentDao().getAll());
            setValue(new LinkedList<Post>());
        }
    }  /////End of PostLiveData class//////////////////
    PostListData postListData = new PostListData();

    public LiveData<List<Post>> getAllPosts(){
        return postListData;
    }

    public void cancellGetAllPosts() {
        modelFirebase.cancellGetAllPosts();
    }


    class ItemListData extends MutableLiveData<List<Item>> {

        @Override
        protected void onActive() {
            super.onActive();
            // new thread tsks
            // 1. get the students list from the local DB
            ItemAsynchDao.getAll(new ItemAsynchDao.ItemAsynchDaoListener<List<Item>>() {
                @Override
                public void onComplete(List<Item> data) {
                    // 2. update the live data with the new student list
                    setValue(data);
                    Log.d("TAG","got students from local DB " + data.size());

                    // 3. get the items list from firebase
                    modelFirebase.getMyItems(new ModelFirebase.GetMyItemsListener() {
                        @Override
                        public void onSuccess(List<Item> itemslist) {
                            // 4. update the live data with the new student list
                            setValue(itemslist);
                            Log.d("TAG","got students from firebase " + itemslist.size());

                            // 5. update the local DB
                            ItemAsynchDao.insertAll(itemslist, new ItemAsynchDao.ItemAsynchDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    Log.d(TAG, "onComplete: insert data to ROOM!");
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancellGetMyItems();
            Log.d("TAG","cancellGetMyItems");
        }

        public ItemListData() {
            super();

            setValue(new LinkedList<Item>());
        }
    }  /////End of ItemLiveData class//////////////////

    ItemListData itemListData = new ItemListData();
    public LiveData<List<Item>> getMyItems(){
        return itemListData;
    }

    public void cancellGetMyItems() {
        modelFirebase.cancellGetMyItems();
    }




/* only firebase no live data and Room
    public interface GetAllPostsListener{
        void onSuccess(List<Post>postsList);
    }

    public void getAllPosts(final GetAllPostsListener listener){
        modelFirebase.getAllStudents(new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onSuccess(List<Post> postslist) {
                listener.onSuccess(postslist);
            }
        });
        //return modelSql.getAllStudents();
    }
*/

//////////////////photos////////////////////////
    public interface SaveImageListener{
        void onDone(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,listener);
    }



    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }
    public void getImage(final String url, final GetImageListener listener ){
       String localFileName = URLUtil.guessFileName(url, null, null);
        //String localFileName=null;
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (imageBitmap == null) {
                        listener.onDone(null);
                    }else {
                        //2.  save the image localy
                        String localFileName = URLUtil.guessFileName(url, null, null);


                        saveImageToFile(imageBitmap, localFileName);
                        Log.d(TAG, "*&*&*&*&*&*&*&*&*&*&*save image to cache: " + localFileName);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onDone(image);
        }
    }

    ///////////Local files////////////

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        if (imageBitmap == null) return;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();


           addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        HomeActivity.context.sendBroadcast(mediaScanIntent);
        Log.d(TAG, "9999999999999999addPicureToGallery-added: "+mediaScanIntent.getData().toString());

    }
/*
    private void addPicureToGallery(File imageFile) {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        Log.d(TAG, "addPicureToGallery: "+mediaScanIntent.getData().toString());
        HomeActivity.context.sendBroadcast(mediaScanIntent);

    }
    */
}
