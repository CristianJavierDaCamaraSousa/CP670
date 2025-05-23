package com.example.androidassignments;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    public static final String TAG = "ListItemsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
}