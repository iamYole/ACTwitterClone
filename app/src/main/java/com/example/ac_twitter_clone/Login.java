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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity {

    private TextView email,password;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.txtLoginEail);
        password=findViewById(R.id.txtLoginPword);

        btnSignup=findViewById(R.id.btnLogin_SignUp);
        btnLogin=findViewById(R.id.btnLogin_Login);
    }

    public void login(View view){
        if(email.getText().toString().equals("") ||  password.getText().toString().equals("")){
            FancyToast.makeText(this,"All fields are required", Toast.LENGTH_LONG,FancyToast.INFO,false).show();
        }
        else {
            final ProgressDialog progressBar = new ProgressDialog(this);
            progressBar.setMessage("Loging ... ");
            progressBar.show();
            try
            {
                ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null){
                            FancyToast.makeText(Login.this,user.getUsername()+" Logged in successfully", Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                            startTwitter();
                            finish();
                        }
                        else {
                            FancyToast.makeText(Login.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        }
                        progressBar.dismiss();
                    }
                });
            }catch(Exception ex){
                FancyToast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        }
    }
    public void signUp(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startTwitter(){
        Intent intent=new Intent(Login.this,TwitterUsers.class);
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
