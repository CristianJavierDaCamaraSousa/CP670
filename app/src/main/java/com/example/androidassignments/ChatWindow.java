package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
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
    Button buttonSend;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    private ChatDatabaseHelper dhHelper;
    private SQLiteDatabase myDB;

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
        messageAdapter = new ChatAdapter( this,messages );
        listViewChatWindow.setAdapter(messageAdapter);

        /*
        * DataBase
        * */

        dhHelper = new ChatDatabaseHelper(this);
        myDB = dhHelper.getWritableDatabase();

        Cursor cur = myDB.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME,null);


        //TODO CHECK LATER

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

    }

    private class ChatAdapter extends ArrayAdapter<String>{
        ArrayList<String> messages;
        public ChatAdapter(@NonNull Context context, ArrayList<String> messages) {
            super(context, 0);
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
    }


    public void sendMessage(View view){
        //use it to insert values into the DB
        ContentValues cValues = new ContentValues();

        String newMessage = editTextChat.getText().toString();
        if(!newMessage.isEmpty()){
            messages.add(newMessage);
            messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
            editTextChat.setText("");
            cValues.put(ChatDatabaseHelper.KEY_MESSAGE,newMessage);
            myDB.insert(ChatDatabaseHelper.TABLE_NAME,null,cValues);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    /*
    *   ContentValues Cvalues = new ContentValues();
        cValues.put(“FirstName”,  “Abdul-Rahman”);
        cValues.put(“LastName”,  “Mawlood-Yunis”);
        cValues.put(“email”,  “amawloodyunis@wlu.ca” );

        dataBaseName.insert(“TableName”, “NullPlaceholder”, cValues)
        *
        *
        *
        * public Item createItem(Item item) {

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_ITEM, item.getItem());

            long insertId = database.insert(SQLiteHelper.TABLE_ITEMS, null,
                values);
…
}
    *
    * */

}