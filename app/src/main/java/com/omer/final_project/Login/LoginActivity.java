package com.omer.final_project.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omer.final_project.Home.HomeActivity;
import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.User;
import com.omer.final_project.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.loginProgressBar);
        progressBar . setVisibility(View.GONE);
        ImageView logo=findViewById(R.id.logoImageView);
        //logo.setImageResource(R.mipmap.ic_applogo);
        final EditText emailEditText=findViewById(R.id.emailEditText);
        final EditText passwordEditText=findViewById(R.id.passwordEditText);

        final Intent intent=new Intent (this, RegisterActivity .class);
        Button registerButton=findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(intent,1);


                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();

              //  registerNewUser(email,password);


            }
        });

        Button signInButton=findViewById(R.id.signinButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                signInUser(email,password);

            }
        });

    }



    ////////////////////////////////////////////////////////




  public void signInUser(String email,String password){
    final Intent intent= new Intent(this, HomeActivity.class);
      mAuth.signInWithEmailAndPassword(email, password)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                          // Sign in success, update UI with the signed-in user's information
                          Log.d(TAG, "signInWithEmail:success");
                          FirebaseUser user = mAuth.getCurrentUser();
                          Toast.makeText(LoginActivity.this, "Authentication success.",
                                  Toast.LENGTH_SHORT).show();
                          startActivity(intent);
                          //updateUI(user);
                      } else {
                          // If sign in fails, display a message to the user.
                          Log.w(TAG, "signInWithEmail:failure", task.getException());
                          Toast.makeText(LoginActivity.this, "Authentication failed.",
                                  Toast.LENGTH_SHORT).show();
                          //updateUI(null);
                      }

                  }
              });
  }

}
