package com.omer.final_project.Login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText userName;
    EditText userEmail;
    EditText firstName;
    EditText lastName;
    EditText userPassword;
    ImageView profilePicIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View.OnFocusChangeListener focusChangeListener=new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        userName=findViewById(R.id.userNameEditText);
        userEmail=findViewById(R.id.emailEditText);
        firstName=findViewById(R.id.firstNameEditText);
        lastName=findViewById(R.id.lastnameEditText);
        userPassword=findViewById(R.id.passwordEditTextREG);

        userName.setOnFocusChangeListener(focusChangeListener);
        userEmail.setOnFocusChangeListener(focusChangeListener);
        firstName.setOnFocusChangeListener(focusChangeListener);
        lastName.setOnFocusChangeListener(focusChangeListener);
        userPassword.setOnFocusChangeListener(focusChangeListener);
        Button saveButton=findViewById(R.id.regSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=userEmail.getText().toString();
              final  String password=userPassword.getText().toString();
                final User user=new User(userName.getText().toString(),email);
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());

                /*
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            user.setProfilePic(url);
                            Log.d(TAG, "%%%%%%%%%%onDone: "+user.getProfilePic());
                            registerNewUser(user,password);
                          //  getActivity().getFragmentManager().popBackStack();
                            finish();
                        }
                    });
                }
                */
                registerNewUser(user,password);

            }
        });

        Button cancelButton=findViewById(R.id.regCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView addProfilePicButton=findViewById(R.id.addProfilepicIV);
        addProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open camera
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }


            }
        });
       // profilePicIV=findViewById(R.id.profilePicIV);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            profilePicIV.setImageBitmap(imageBitmap);
        }
    }



///////////////////////REGISTER TO DB///////////////////////////////////////////////

    public void registerNewUser(final User user, String password){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userFb = mAuth.getCurrentUser();
                            user.setUserId(userFb.getUid());
                            //User u=new User("usernameTEMP",user.getEmail());
                            Model.instance.addUser(user);
                            Log.d(TAG, "onComplete: ###########################################################3");
                            Log.d(TAG, "onComplete: user added to DB-user id: "+user.getUserId());
                            // updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                    }
                });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
