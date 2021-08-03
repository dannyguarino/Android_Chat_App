package com.example.chatapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatapp.R;
import com.example.chatapp.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {


    // Class Members
    EditText email, password;
    Button btn_login;
    Typeface MR,MRR;
    ProgressDialog dialog;

    //Firebase auth variable
    FirebaseAuth auth;
    TextView forgot_password, login_tv, msg_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get Fonts from assets
        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        //Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Firebase auth Instance
        auth = FirebaseAuth.getInstance();

        //UI Components
        login_tv = findViewById(R.id.login_tv);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);
        msg_tv = findViewById(R.id.msg_tv);

        //Apply Fonts from Assets on Texts in UI components
        msg_tv.setTypeface(MRR);
        login_tv.setTypeface(MR);
        email.setTypeface(MRR);
        password.setTypeface(MRR);
        btn_login.setTypeface(MRR);
        forgot_password.setTypeface(MRR);

        //Forgot password setOnClickListener to navigate to ResetPasswordActivity
        forgot_password.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)));

        //setOnClickListener On Login Button in Login Page
        btn_login.setOnClickListener((View view) -> {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            Utils.hideKeyboard(LoginActivity.this);

            //Validate the email and password
            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {

                dialog = Utils.showLoader(LoginActivity.this);

                //Sign in with Email and Password in firebase authentication
                auth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){

                                //Initialize intent to move to MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                if(dialog!=null){
                                    dialog.dismiss();
                                }
                                startActivity(intent);
                                finish();
                            } else {
                                if(dialog!=null){
                                    dialog.dismiss();
                                }
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}
