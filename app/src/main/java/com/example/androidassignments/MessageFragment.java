package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;



public class MessageFragment extends Fragment {

    private ChatWindow parentActivity;

    public MessageFragment() {
        // Necesario para reconstrucción automática
    }

    public MessageFragment(ChatWindow activity) {
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            if (parentActivity != null) {
                //parentActivity.deleteMessageById(id);
                getParentFragmentManager().beginTransaction().remove(this).commit();
            } else {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }

        });

        return view;
    }


}