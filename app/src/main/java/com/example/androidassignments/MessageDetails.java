package com.example.androidassignments;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MessageDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle dataToPass = getIntent().getExtras();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(dataToPass);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.message_details_frame_layout, fragment)
                .commit();
    }
}