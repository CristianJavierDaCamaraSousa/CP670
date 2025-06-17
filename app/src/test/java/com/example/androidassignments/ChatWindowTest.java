package com.example.androidassignments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import android.app.Activity;


public class ChatWindowTest extends Activity {

    @Rule
    public ActivityScenarioRule<ChatWindowTest>
            activityScenarioRule
            = new ActivityScenarioRule<>(ChatWindowTest.class);

    @Test
    public void sendMessage_displaysMessageInListView() {
        // Escribe texto en el campo de entrada
        onView(withId(R.id.editTextChat))
                .perform(ViewActions.typeText("Hola mundo"), ViewActions.closeSoftKeyboard());

        // Haz clic en el botón de enviar
        onView(withId(R.id.buttonSendChat)).perform(ViewActions.click());

        // Verifica que el mensaje aparece en pantalla (ListView)
        onView(withText("Hola mundo")).check(matches(isDisplayed()));
    }
}
