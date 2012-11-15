/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santaanna.friendlyreader.robke.utils;

/**
 *
 * @author Robin Keskisärkkä
 */
public class Speak implements Runnable {
    // Create the listener list
    protected javax.swing.event.EventListenerList listenerList =
        new javax.swing.event.EventListenerList();
    private String text;
    
    //Server login
    private TranslationEngine translationEngine = new TranslationEngine();
    
    public Speak(String text) {
        this.text = text;
    }
    
    public void run() {
        translationEngine.Speak(text, "Swedish (Sweden)");
        fireStopEvent(new SpeakEvent(this));
    }
    
    // This methods allows classes to register for MyEvents
    public void addSpeakEventListener(SpeakEventListener listener) {
        listenerList.add(SpeakEventListener.class, listener);
    }

    // This methods allows classes to unregister for MyEvents
    public void removeSepakListener(SpeakEventListener listener) {
        listenerList.remove(SpeakEventListener.class, listener);
    }

    //Fire event
    void fireStopEvent(SpeakEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==SpeakEventListener.class) {
                ((SpeakEventListener)listeners[i+1]).speakStopOccured(evt);
            }
        }
    }
    


}


