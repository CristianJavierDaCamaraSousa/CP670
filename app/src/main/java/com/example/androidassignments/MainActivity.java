package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mainButton = (Button) findViewById(R.id.button_main);

        mainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), ListItemsActivity.class);
                startActivityForResult(intent,10);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == 10)
            Log.i(TAG,"Returned to MainActivity.onAtivityResult");
        if(responseCode == MainActivity.RESULT_OK){ //requestCode or responseCode?
            String messagePassedFromIntent = data.getStringExtra("Response");
            Log.i(TAG,"Intent passed " + messagePassedFromIntent);
            print("Intent passed " + messagePassedFromIntent);
        }
    }

    public void print(String strMessage){
        Toast toast = Toast.makeText(this,strMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    public void startChat(View view){
        Log.i(TAG,"User Clicked Start Chat");
        Intent intent = new Intent(this, ChatWindow.class);
        startActivity(intent);
    }
}