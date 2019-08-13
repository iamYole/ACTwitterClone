package com.example.ac_twitter_clone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {

    private TextView email,username,password;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();


        email=findViewById(R.id.txtSignUpEmail);
        username=findViewById(R.id.txtSignUpUsername);
        password=findViewById(R.id.txtSignUpPword);

        btnSignup=findViewById(R.id.btnSignUp);
        btnLogin=findViewById(R.id.btnLogin);
    }
    public void login(View view){
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }
    public void signUp(View view){

        if(email.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("")){
            FancyToast.makeText(this,"All fields are required", Toast.LENGTH_LONG,FancyToast.INFO,false).show();
        }
        else {
            try
            {
                ParseUser user=new ParseUser();
                user.setEmail(email.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                final ProgressDialog progressBar = new ProgressDialog(this);
                progressBar.setMessage("Signing up " + username.getText().toString());
                progressBar.show();

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(MainActivity.this,username.getText().toString()+" signed up successfully", Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                            progressBar.dismiss();
                            startTwitter();
                            finish();
                        }
                        else {
                            FancyToast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        }
                    }
                });
            }
            catch (Exception ex){
                FancyToast.makeText(MainActivity.this,ex.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        }
    }

    private void startTwitter(){
        Intent intent=new Intent(MainActivity.this,TwitterUsers.class);
        startActivity(intent);
    }

    public void hideKeyboard(View view){
        try {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){

        }
    }
}
