package com.example.androidassignments;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatWindowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> startActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
        clearDatabase();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    private void clearDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(context) {

            public int getReadableDatabaseVersion() {
                return 0;
            }
        };
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + ChatDatabaseHelper.TABLE_NAME);
    }

    @Test
    public void testStartChatLaunchesChatWindow() {
        onView(withId(R.id.start_chat_button)).perform(click());
        intended(hasComponent(ChatWindow.class.getName()));
    }

    @Test
    public void testSendMessageUpdatesListView() {
        onView(withId(R.id.start_chat_button)).perform(click());
        onView(withId(R.id.editTextChat)).perform(typeText("My name is Cristian"), closeSoftKeyboard());
        onView(withId(R.id.buttonSendChat)).perform(click());
        onView(withText("My name is Cristian")).check(matches(isDisplayed()));
    }

    @Test
    public void testMessagesPersistOnReopen() {
        // Send message
        onView(withId(R.id.start_chat_button)).perform(click());
        onView(withId(R.id.editTextChat)).perform(typeText("Message a"), closeSoftKeyboard());
        onView(withId(R.id.buttonSendChat)).perform(click());

        // go back
        pressBackUnconditionally();

        // re open chatwindow
        onView(withId(R.id.start_chat_button)).perform(click());
        onView(withText("Message a")).check(matches(isDisplayed()));
    }

    @Test
    public void testDatabaseUpgradeClearsMessages() {
        // Insert message
        onView(withId(R.id.start_chat_button)).perform(click());
        onView(withId(R.id.editTextChat)).perform(typeText("Message to delete"), closeSoftKeyboard());
        onView(withId(R.id.buttonSendChat)).perform(click());

        // Database upgrade
        Context context = ApplicationProvider.getApplicationContext();
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(context) {

            public int getReadableDatabaseVersion() {
                return ChatDatabaseHelper.VERSION_NUM + 1;
            }
        };
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), ChatDatabaseHelper.VERSION_NUM, ChatDatabaseHelper.VERSION_NUM + 1);

        // Reopen activity
        pressBackUnconditionally();
        onView(withId(R.id.start_chat_button)).perform(click());

        // The message is gone ?
        onView(withId(R.id.listViewChatWindow)).check(matches(hasChildCount(0)));
    }
}
