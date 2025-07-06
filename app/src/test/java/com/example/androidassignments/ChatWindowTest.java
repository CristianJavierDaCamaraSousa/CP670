package com.example.androidassignments;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatWindowTest {

    @Test
    public void testAddMessageToList() {
        ArrayList<String> messages = new ArrayList<>();
        String newMessage = "Hello again";

        if (!newMessage.isEmpty()) {
            messages.add(newMessage);
        }

        assertEquals(1, messages.size());
        assertEquals("Hello again", messages.get(0));
    }

    @Test
    public void testEmptyMessageNotAdded() {
        ArrayList<String> messages = new ArrayList<>();
        String newMessage = "";

        if (!newMessage.isEmpty()) {
            messages.add(newMessage);
        }

        assertEquals(0, messages.size());
    }
}
