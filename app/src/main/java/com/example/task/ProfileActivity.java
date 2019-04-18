package com.example.task;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;


public class ProfileActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    EditText name, emailAddress, country, phone;
    Button save;
    Switch email, sms, pushNotification;
    ImageView photo;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.eTname);
        emailAddress = findViewById(R.id.eTmail);
        country = findViewById(R.id.eTcountry);
        phone = findViewById(R.id.eTphone);
        save = findViewById(R.id.btnSave);
        photo=findViewById(R.id.selectPic);
        email = findViewById(R.id.switchEmail);
        sms = findViewById(R.id.switchSms);
        pushNotification = findViewById(R.id.switchPush);
        db.collection("users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                            name.setText(userProfile.getName());
                            country.setText(userProfile.getCountry());
                            emailAddress.setText(userProfile.getEmailAddress());
                            phone.setText(userProfile.getPhone());
                            email.setChecked(userProfile.isEmail());
                            sms.setChecked(userProfile.isSms());
                            pushNotification.setChecked(userProfile.isPush());
                        }
                        else
                        {
                            emailAddress.setText(firebaseUser.getEmail());
                        }

                    }
                });

        FirebaseStorage.getInstance().getReference().child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(photo);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSaveBtn();
            }
        });
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == PICK_IMAGE) {
                    Uri selectedImageUri = data.getData();
                    uri=selectedImageUri;
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    photo.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }
    private void setSaveBtn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString().trim())
                .build();
        user.updateProfile(profileUpdates);
        StorageReference storageRef;
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageRef.child("images/"+user.getUid()+"/profilePic.jpg");
        profileRef.putFile(uri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                      //  Toast.makeText(ProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               // Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();

            }
        });

        try {
            UserProfile userProfile = new UserProfile(name.getText().toString(), country.getText().toString(), emailAddress.getText().toString(), phone.getText().toString(),email.isChecked(), sms.isChecked(), pushNotification.isChecked());


            db.collection("users").document(user.getUid()).set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileActivity.this, "saved successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }catch (Exception e)
        {
          //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }
}
