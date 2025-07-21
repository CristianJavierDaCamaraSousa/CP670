package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;



public class MessageFragment extends Fragment {

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_details, container, false);

        Bundle args = getArguments();
        String message = args.getString("message");
        long id = args.getLong("id");

        TextView msgText = view.findViewById(R.id.fragmentMessage);
        TextView idText = view.findViewById(R.id.fragmentMessageId);
        Button deleteButton = view.findViewById(R.id.fragmentDeleteButton);

        msgText.setText(message);
        idText.setText("ID: " + id);


        deleteButton.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof ChatWindow) {
                ((ChatWindow) activity).deleteMessageById(id);
                getParentFragmentManager().beginTransaction().remove(this).commit();
            } else {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });

        return view;
    }




}