package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ChatWindow" ;
    ListView listViewChatWindow;
    EditText editTextChat;
    Button buttonSend, buttonDelete;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    private ChatDatabaseHelper dhHelper;
    private SQLiteDatabase myDB;
    protected Boolean frameLayoutExists=false;
    private Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listViewChatWindow = (ListView) findViewById(R.id.listViewChatWindow);
        editTextChat = (EditText) findViewById(R.id.editTextChat);
        buttonSend = (Button) findViewById(R.id.buttonSendChat);
        messages = new ArrayList<>();
        messageAdapter = new ChatAdapter( this,messages, cur );
        listViewChatWindow.setAdapter(messageAdapter);

        /*
         * DataBase
         * */

        dhHelper = new ChatDatabaseHelper(this);
        myDB = dhHelper.getWritableDatabase();

        cur = myDB.rawQuery("SELECT _id, " + ChatDatabaseHelper.KEY_MESSAGE + " FROM " + ChatDatabaseHelper.TABLE_NAME, null);


        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + cur.getColumnCount());
        for (int i = 0; i < cur.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name: " + cur.getColumnName(i));
        }

        int messageColIndex = cur.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        if (messageColIndex != -1) {
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cur.getString(messageColIndex));
                messages.add(cur.getString(messageColIndex));
                cur.moveToNext();
            }
        }

        cur.close();
        cur = myDB.rawQuery("SELECT _id, " + ChatDatabaseHelper.KEY_MESSAGE + " FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        listViewChatWindow.setAdapter(messageAdapter);


        if(findViewById(R.id.frameLayoutDetails)!=null){
            frameLayoutExists = true;
        }

        listViewChatWindow.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMessage = messages.get(position);
            long messageId = id;

            if (frameLayoutExists) { //tablet layout

                Bundle dataToPass = new Bundle();

                dataToPass.putString("message", selectedMessage);
                dataToPass.putLong("id", messageId);

                MessageFragment fragment = new MessageFragment();
                fragment.setArguments(dataToPass);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayoutDetails, fragment)
                        .commit();
            } else { //phone layout

                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtra("message", selectedMessage);
                intent.putExtra("id", messageId);
                startActivityForResult(intent, 1);
            }
        });
    }

    private class ChatAdapter extends android.widget.CursorAdapter{
        ArrayList<String> messages;
        public ChatAdapter(@NonNull Context context, ArrayList<String> messages, Cursor cur) {
            super(context, cur,0);
            this.messages = messages;
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem(int pos){
            return messages.get(pos);
        }


        public View getView(int pos, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(pos%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(pos));

            return result;
        }


        public long getItemId(int position) {

            if (cur != null && cur.moveToPosition(position)) {

                int idIndex = cur.getColumnIndex("_id");

                if (idIndex != -1) {
                    return cur.getLong(idIndex);
                }
            }
            return -1;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            int position = cursor.getPosition();
            int layout;

            if (position % 2 == 0) {
                layout = R.layout.chat_row_incoming;
            } else {
                layout = R.layout.chat_row_outgoing;
            }

            return inflater.inflate(layout, parent, false);        }

        @Override
        public void bindView(View view, Context context, Cursor cursor){

            TextView messageText = view.findViewById(R.id.message_text);
            String message = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));

            messageText.setText(message);

        }
    }


    public void sendMessage(View view){
        //use it to insert values into the DB
        ContentValues cValues = new ContentValues();

        String newMessage = editTextChat.getText().toString();
        if(!newMessage.isEmpty()){
            messages.add(newMessage);
            messageAdapter.notifyDataSetChanged(); //this restarts getCount() and getView()
            editTextChat.setText("");
            cValues.put(ChatDatabaseHelper.KEY_MESSAGE,newMessage);
            myDB.insert(ChatDatabaseHelper.TABLE_NAME,null,cValues);

            refreshCursor();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    public void deleteMessageById(long id) {
        int rowsDeleted = myDB.delete(ChatDatabaseHelper.TABLE_NAME, "_id = ?", new String[] {String.valueOf(id)});

        if (rowsDeleted > 0) {


            for (int i = 0; i < messages.size(); i++) {
                if (messageAdapter.getItemId(i) == id) {
                    messages.remove(i);

                    messageAdapter.notifyDataSetChanged(); //this restarts getCount() and getView()
                    refreshCursor();
                    break;
                }
            }
        } else {

            Log.w(ACTIVITY_NAME, "This id wasnnt' found");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            long dleteId = data.getLongExtra("id", -1);
            if (dleteId != -1) {
                deleteMessageById(dleteId);
            }
        }
    }

    private void refreshCursor() {
        Cursor newCursor = myDB.rawQuery("SELECT _id, " + ChatDatabaseHelper.KEY_MESSAGE + " FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        messageAdapter.changeCursor(newCursor);

        cur = newCursor;
    }

}