package com.example.charlie.practicaandroid;

/**
 * Created by Charlie on 23/01/2017.
 */

public abstract class MessageReceiver {
    abstract boolean receiveMessage(String sender,String message);
    boolean sendMessage(String sender,String message){
       return receiveMessage(sender,message);
    }
}
