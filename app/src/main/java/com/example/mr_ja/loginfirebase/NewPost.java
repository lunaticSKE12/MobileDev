package com.example.mr_ja.loginfirebase;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewPost extends AppCompatActivity {

    Button selectImage,postBtn, viewPostBtn;
    ImageView imageView;
    TextView titleText;
    public static final  int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRootRef;
    private Uri mImageUri = null;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Firebase.setAndroidContext(this);
        selectImage = (Button) findViewById(R.id.selectImage_id);
        postBtn = (Button) findViewById(R.id.postBtn_id);
        titleText = (TextView) findViewById(R.id.title_id);
        imageView = (ImageView) findViewById(R.id.imageView_id);
        viewPostBtn = (Button) findViewById(R.id.viewPostBtn_id);

        //progress bar
        mProgressDialog = new ProgressDialog(NewPost.this);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Call for permission", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE);

                    }else{
                        callGallery();
                    }


                }
            }
        });

        //initialise firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mRootRef = new Firebase("https://mobiledev-be310.firebaseio.com/").child("User_detail").push();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mobiledev-be310.appspot.com/");

        //click new post
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mName = titleText.getText().toString().trim();
                if (mName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                Firebase chileRef_name = mRootRef.child("Image_Title");
                chileRef_name.setValue(mName);
                Toast.makeText(getApplicationContext(), "Updated info",Toast.LENGTH_SHORT).show();
            }
        });

        viewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewPosts.class));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode){
            case READ_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callGallery();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    //if access is granted gally will be opened
    private void callGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    //After selecting image, it will be uploaded to firebase
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("User_Image")
                    .child(mImageUri.getLastPathSegment());
            mProgressDialog.setMessage("Updating...");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    mRootRef.child("Image_URL").setValue(downloadUri.toString());
                    Glide.with(getApplicationContext()).load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(imageView);
                    Toast.makeText(getApplicationContext(),"Updated...",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
//            mImageUri = data.getData();
//            imageView.setImageURI(mImageUri);
//            StorageReference filePath = mStorage.child("User_Image").child(mImageUri.getLastPathSegment());
//
//            mProgressDialog.setMessage("Updating...");
//            mProgressDialog.show();
//
//            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUri = taskSnapshot.getUploadSessionUri();
//                    mDatabaseRef.child("Image_URL").setValue(downloadUri.toString());
////                    postBtn.setEnabled(false);
//                    Toast.makeText(getApplicationContext(),"Updated...",Toast.LENGTH_SHORT).show();
//                    mProgressDialog.dismiss();
//                }
//            });
//        }
//    }
}
