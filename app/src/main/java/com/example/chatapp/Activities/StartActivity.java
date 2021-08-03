package com.example.chatapp.Activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartActivity extends AppCompatActivity {

    Button login, register;
    TextView chat_title_tv;
    Typeface MR, MRR;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");



        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        chat_title_tv = findViewById(R.id.chat_title_tv);

        login.setTypeface(MR);
        register.setTypeface(MR);
        chat_title_tv.setTypeface(MR);

        login.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, LoginActivity.class)));

        register.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, RegisterActivity.class)));
    }
}
