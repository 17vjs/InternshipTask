package com.example.task;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PromoCodesActivity extends AppCompatActivity {
private     LinearLayout linearLayout;
private FirebaseUser firebaseUser;
private FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
      db = FirebaseFirestore.getInstance();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
      linearLayout=findViewById(R.id.linearLayout);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        db.collection("users").document(firebaseUser.getUid()).collection("promoCode").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              PromoCodes promoCodes=document.toObject(PromoCodes.class);
                                TextView textView = new TextView(PromoCodesActivity.this);
                                textView.setText(promoCodes.getCode()+"\nValid upto : "+promoCodes.getValidity());
                                textView.setTextSize((float) 20);
                                textView.setTextColor(Color.RED);
                                textView.setBackgroundColor(Color.WHITE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 10, 0, 10);
                                textView.setLayoutParams(params);
                                textView.setPadding(20, 10, 20, 10);
                                linearLayout.addView(textView);
                            }
                        } else {
                            Toast.makeText(PromoCodesActivity.this, "Unable to fetch now", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
