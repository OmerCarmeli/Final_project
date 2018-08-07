package com.omer.final_project.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    private static final String TAG = "ModelFirebase";
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
/*
    public ModelFirebase() {
      //  DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("test1").setValue("hello");
        Log.d(TAG, "ModelFirebase:c'tor ");
    }
*/

    public void addPost(Post post){
        mDatabase.child("Posts").child(post.getPostId()).setValue(post);
    }

    public void removePost(Post post){
        mDatabase.child("Posts").child(post.getPostId()).removeValue();
    }

    public void addUserToDb(User user){
        mDatabase.child("Users").child(user.getUserId()).setValue(user);
    }

    public FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    interface getUserListener{
        public void onSuccess(User user);
    }
   // User u;

    public void getUserFromDb(final getUserListener listener,String userId){
        DatabaseReference userRef=mDatabase.child("Users").child(userId);//chang to current user
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User userformDB =dataSnapshot.getValue(User.class);
               listener.onSuccess(userformDB);
                //u=new User(userformDB);
               // u=userformDB;
            //    Log.d(TAG, "^^^^^^^^^^^^^^onDataChange: "+u.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addItemToUser(User user ,Item item){

        mDatabase.child("Users").child(user.getUserId()).child("items").child(item.getItemId()).setValue(item);

    }
    public void signOut(){
        mAuth.signOut();
    }


    interface GetAllPostsListener{
        public void onSuccess(List<Post> postslist);
    }

    ValueEventListener eventListener;

    public void getAllPosts(final GetAllPostsListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> postsList = new LinkedList<>();

                for (DataSnapshot pSnapshot: dataSnapshot.getChildren()) {
                    Post p = pSnapshot.getValue(Post.class);
                    postsList.add(p);
                }
                listener.onSuccess(postsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cancellGetAllPosts() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        stRef.removeEventListener(eventListener);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    interface GetMyItemsListener{
        public void onSuccess(List<Item> itemslist);
    }

    ValueEventListener itemseventListener;

    public void getMyItems(final GetMyItemsListener listener) {
        ///change the path--if it makes problems -> add the currnet user in the function
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUser().getUid()).child("items");

        itemseventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> itemsList = new LinkedList<>();

                for (DataSnapshot iSnapshot: dataSnapshot.getChildren()) {
                    Item i = iSnapshot.getValue(Item.class);
                    itemsList.add(i);
                }
                listener.onSuccess(itemsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cancellGetMyItems() {
        //change the path
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUser().getUid()).child("items");
        stRef.removeEventListener(itemseventListener);
    }

    ///////////////////////////////////////Login//////////////////////////////

    public FirebaseUser registerUser(String email,String password){
        final FirebaseUser[] user1 = new FirebaseUser[1];
         /*
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        //    updateUI(user);
                            user1[0] = user; //to update UI
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
        */
        return user1[0];
    }

    public void signInUser(){

    }

    public FirebaseUser checkIfSignIn(){
        return mAuth.getCurrentUser();
        //need to be check if the user is  not null
    }





    //////////////////////////////////Managing Files///////////////////////////

    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Date d = new Date();
        String name = ""+ d.getTime();
       final StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imagesRef.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    listener.onDone(downloadUri.toString());
                }
            }
        });
        /*
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                listener.onDone(downloadUrl.toString());
            }
        });
        */
    }


    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Log.d("TAG","get image from firebase success");
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                Log.d("TAG","get image from firebase Failed");
                listener.onDone(null);
            }
        });
    }


}
