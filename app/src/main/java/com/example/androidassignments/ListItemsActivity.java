package com.example.androidassignments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

public class ListItemsActivity extends AppCompatActivity {

    public static final String TAG = "ListItemsActivity";
    int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton buttonImage;
    Switch switch1;
    CheckBox chkBox;

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


        buttonImage = (ImageButton) findViewById(R.id.button_image);
        switch1 = (Switch) findViewById(R.id.switch_button);
        chkBox = (CheckBox) findViewById(R.id.checkbox);

        buttonImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (ContextCompat.checkSelfPermission(ListItemsActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListItemsActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(pictureIntent,REQUEST_IMAGE_CAPTURE);
                    }
                }

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
        //print("Print debug: -finishing ListItemsActivity");
    }


    //taken from the book, page 345
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton btnImg = findViewById(R.id.button_image);
            btnImg.setImageBitmap(imageBitmap);
        }
    }

    public void setOnCheckedChanged(View view){
        if(switch1.isChecked()){
            CharSequence text = getString(R.string.switch_on);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this , text, duration);
            toast.show();
        }else{
            CharSequence text = getString(R.string.switch_off);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this , text, duration);
            toast.show();
        }
    }

    public void onCheckChecked(View view){
        AlertDialog.Builder builder= new AlertDialog.Builder(ListItemsActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent(  );
                        resultIntent.putExtra("Response", getString(R.string.here_is_my_response));
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //
                    }
                })
                .show();
    }

    //Method to print during debugging.
    public void print(String strMessage){
        Toast toast = Toast.makeText(this,strMessage, Toast.LENGTH_LONG);
        toast.show();
    }

}