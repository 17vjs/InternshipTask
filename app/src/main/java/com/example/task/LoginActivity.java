package com.example.task;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private EditText emailId,password;
 private FirebaseAuth firebaseauth;
    private Dialog dialog;

    @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       firebaseauth = FirebaseAuth.getInstance();


        //UI views
        emailId=findViewById(R.id.emailId);
        password=findViewById(R.id.password);
        Button loginbtn = findViewById(R.id.loginbtn);
        Button registerbtn = findViewById(R.id.registerbtn);
        TextView forgotpassword = findViewById(R.id.forgotPassword);

        TextView back = findViewById(R.id.back);


        ///////////


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginBtn();
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegisterBtn();
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setforgotpasswordtv();
            }
        });



    }

    private void setLoginBtn()
    {
        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);
        dialog.show();

        if(validate()){

            firebaseauth.signInWithEmailAndPassword(emailId.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        emailverification();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void setforgotpasswordtv()
    {
        if(!emailId.getText().toString().isEmpty()) {
            firebaseauth.sendPasswordResetEmail(emailId.getText().toString().trim());
            Toast.makeText(LoginActivity.this,"Follow the link to reset your password.",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(LoginActivity.this,"Enter your Email id only",Toast.LENGTH_LONG).show();
        }

    }
    private boolean validate(){

        boolean result =false;
        if(emailId.getText().toString().trim().isEmpty() ||  password.getText().toString().trim().isEmpty()) {

            dialog.dismiss();
            Toast.makeText(this,"Fill all details",Toast.LENGTH_SHORT).show();
        }else{
            result=true;
        }
        return result;
    }
    private void emailverification(){


        FirebaseUser user = firebaseauth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {

            dialog.dismiss();
          //  Toast.makeText(LoginActivity.this, "Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, "Please verify your email id by following the link sent to your email address at the time of registration.", Toast.LENGTH_LONG).show();
            firebaseauth.signOut();
        }

    }
    private void setRegisterBtn()
    {
        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.setCancelable(false);
        dialog.show();
        if(validate()){

            String user_name=emailId.getText().toString().trim();
            String pass_word=password.getText().toString().trim();
            firebaseauth.createUserWithEmailAndPassword(user_name,pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendemailverification();
                    }else{
                        dialog.dismiss(); Toast.makeText(LoginActivity.this, "registration failed....try again", Toast.LENGTH_SHORT).show();
                    }}
            });

        }
        else
        {
            dialog.dismiss();
        }


    }
    private void sendemailverification(){

        FirebaseUser user=firebaseauth.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,"click on the email verification link sent to you on your mail",Toast.LENGTH_LONG).show();
                        firebaseauth.signOut();
                        onBackPressed();

                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Registration Failed.....try again",Toast.LENGTH_SHORT).show();
                        firebaseauth.getCurrentUser().delete();

                    }
                }
            });}
        else{
            dialog.dismiss();
            Toast.makeText(this,"Registration Failed.....try again",Toast.LENGTH_SHORT).show();
            firebaseauth.getCurrentUser().delete();
        }
    }
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

