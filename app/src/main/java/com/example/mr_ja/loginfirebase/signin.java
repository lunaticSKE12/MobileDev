package com.example.mr_ja.loginfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signin extends AppCompatActivity {

    private TextView email;
    private Button signout,newPost,viewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = (TextView) findViewById(R.id.useremail_id);
        signout = (Button) findViewById(R.id.signout_id);
        newPost = (Button) findViewById(R.id.newPost_id);
        viewPost = (Button) findViewById(R.id.viewPost_id);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String emailuser = user.getEmail();
            email.setText("Welcome "+ emailuser);

        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(signin.this, MainActivity.class));
            }
        });

         newPost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewPost.class));
             }
         });

         viewPost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), ViewPosts.class));

             }
         });



    }
}
