package com.example.chatapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatapp.R;
import com.example.chatapp.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;
    Typeface MR, MRR;
    TextView hint_tv;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);
        hint_tv = findViewById(R.id.hint_tv);

        send_email.setTypeface(MRR);
        btn_reset.setTypeface(MR);
        hint_tv.setTypeface(MR);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(view -> {
            String email = send_email.getText().toString();
            Utils.hideKeyboard(ResetPasswordActivity.this);

            if (email.equals("")){
                Toast.makeText(ResetPasswordActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Please check you Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
