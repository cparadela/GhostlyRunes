package com.example.charlie.GhostlyRunes;

/**
 * Created by Charlie on 23/01/2017.
 */

public interface MessageReceiver {
    boolean transmitMessage(String sender,String message) throws InterruptedException;

}
