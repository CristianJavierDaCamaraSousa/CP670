package com.example.androidassignments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    static final String preferencesFileName = "my_preferences_file";
    SharedPreferences preferences;
    //Button loginButton;
    EditText loginEmail, loginPass;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //loginButton = (Button) findViewById(R.id.button_login);
        loginEmail = (EditText) findViewById(R.id.text_login);
        loginPass = (EditText) findViewById(R.id.login_password);
        preferences = getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        String savedEmail = preferences.getString("email","");
        ((EditText) findViewById(R.id.text_login)).setText(savedEmail);

    }

    protected void onStart(){
        super.onStart();
        Log.i(TAG,"inside onCreate");
    }

    protected void onResume(){
        super.onResume();
        Log.i(TAG,"inside onResume");
    }

    protected void onPause(){
        super.onPause();
        Log.i(TAG,"inside onPause");
    }

    protected void onStop(){
        super.onStop();
        Log.i(TAG,"inside onStop");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"inside onDestroy");
    }

    public void loginSaveInfo(View view){
        String email = loginEmail.getText().toString();
        String pass = loginPass.getText().toString();
        edit = preferences.edit();
        edit.putString("email",email);
        edit.commit();

        Pattern pat = Pattern.compile("[A-Za-z0-9+-._]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pat.matcher(email);
        if(!pass.isEmpty() && mat.matches()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if(mat.matches() && pass.isEmpty()){
            View parentLayout = getWindow().getDecorView().findViewById(android.R.id.content) ;
            Snackbar snackbar= Snackbar.make
                    (parentLayout,getString(R.string.snack_password_empty),
                            Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            View parentLayout = getWindow().getDecorView().findViewById(android.R.id.content) ;
            Snackbar snackbar= Snackbar.make
                    (parentLayout,getString(R.string.snack_invalid_email),
                            Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void print(String strMessage){
        Toast toast = Toast.makeText(this,strMessage, Toast.LENGTH_LONG);
        toast.show();
    }
}