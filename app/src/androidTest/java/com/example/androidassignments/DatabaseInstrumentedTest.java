package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new ChatDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + ChatDatabaseHelper.TABLE_NAME);
    }

    @Test
    public void testDBCreation() {
        Context context = ApplicationProvider.getApplicationContext();
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        assertTrue(db.isOpen());

        db.close();
    }

    @Test
    public void testInsertMessage() {
        // Insert Message
        ContentValues values = new ContentValues();
        String testMessage = "New message";
        values.put(ChatDatabaseHelper.KEY_MESSAGE, testMessage);
        long rowId = db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

        assertTrue("Failed insertion", rowId != -1);

        // Read Message
        Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        assertTrue("Empty Cursor", cursor.moveToFirst());

        int messageColIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        assertNotEquals("Column could not  be finded", -1, messageColIndex);

        String retrievedMessage = cursor.getString(messageColIndex);
        assertEquals("Message doesn't match", testMessage, retrievedMessage);

        cursor.close();
    }
}
