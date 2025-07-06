package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chat.db";
    public static final int VERSION_NUM = 3;

    public static final String TABLE_NAME = "messages";
    public static final String KEY_ID ="_id";
    public static final String KEY_MESSAGE = "message";

    private static final String DATABASE_CREATE= "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_MESSAGE
            + " text not null);";

    private static final String DATABASE_DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL(DATABASE_DROP); //I drop the table and then call onCreate to update it
        onCreate(db);

    }


}
