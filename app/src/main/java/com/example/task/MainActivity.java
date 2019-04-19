package com.example.task;

import android.content.Intent;

import android.database.Cursor;
import android.graphics.Color;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;



public class MainActivity extends AppCompatActivity {
    private    FloatingActionButton FAB;
    private TextView vouchers,wishlist,rewards,language,helpCenter,about,notification;
    private ImageView banner;
    private   LinearLayout card1;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        toolbar = findViewById(R.id.toolbar);
        FAB = findViewById(R.id.float_btn);
        wishlist = findViewById(R.id.wishlist);
        rewards = findViewById(R.id.rewards);
        language = findViewById(R.id.language);
        helpCenter = findViewById(R.id.helpCenter);
        about = findViewById(R.id.about);
        notification = findViewById(R.id.notification);
        vouchers = findViewById(R.id.vouchers);
        banner = findViewById(R.id.banner);
        card1 = findViewById(R.id.card1);
////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);


        try{

            Picasso.get().load("https://cdn.pixabay.com/photo/2017/08/05/18/53/mountain-2585069_1280.jpg").into(banner);


            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setElevation(0);

                if (firebaseUser != null) {
                    getSupportActionBar().setTitle(firebaseUser.getDisplayName());
                    //sample data inserted in firebase firestore for further activities to work
                    addSampleData();
                    FirebaseStorage.getInstance().getReference().child("images/" + firebaseUser.getUid() + "/profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().into(banner);
                        }
                    });

                    db.collection("users").document(firebaseUser.getUid()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                                        TextView textView = new TextView(MainActivity.this);
                                        textView.setText("Credits : " + userProfile.getCreditCount());
                                        textView.setTextSize((float) 15);
                                        textView.setTextColor(Color.BLACK);
                                        textView.setBackgroundColor(Color.WHITE);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(0, 10, 0, 10);
                                        textView.setLayoutParams(params);
                                        textView.setPadding(20, 10, 20, 10);
                                        card1.addView(textView);
                                        TextView textView1 = new TextView(MainActivity.this);
                                        textView1.setText("Promo Codes : " + userProfile.getPromoCodeCount());
                                        textView1.setTextSize((float) 15);
                                        textView1.setTextColor(Color.BLACK);
                                        textView1.setBackgroundColor(Color.WHITE);
                                        textView1.setLayoutParams(params);
                                        textView1.setPadding(20, 10, 20, 10);
                                        card1.addView(textView1);

                                    }
                                }
                            });


                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "show the vouchers just like promo codes", Toast.LENGTH_SHORT).show();
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(MainActivity.this, WishListActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Login first", Toast.LENGTH_SHORT).show();
                }

            }

        });
        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "refer & earn activity", Toast.LENGTH_SHORT).show();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "show all notification just like wishlist", Toast.LENGTH_SHORT).show();
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Language_Activity.class));
            }
        });
        helpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "redirect to help center", Toast.LENGTH_SHORT).show();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "redirect to about", Toast.LENGTH_SHORT).show();
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Log in to set up your profile ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(MainActivity.this, PromoCodesActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Login first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    private void addSampleData()
    {
        db.collection("users").document(firebaseUser.getUid()).collection("promoCode").document("pc1").set(new PromoCodes("PETROL20", "12/11/2020"));
        db.collection("users").document(firebaseUser.getUid()).collection("promoCode").document("pc2").set(new PromoCodes("NEW40", "12/11/2020"));
        db.collection("users").document(firebaseUser.getUid()).collection("wishList").document("wl1").set(new WishList("12/11/2020", 11));
        db.collection("users").document(firebaseUser.getUid()).collection("wishList").document("wl2").set(new WishList("12/11/2021", 12));


    }
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

}

