/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santaanna.friendlyreader.robke.utils;

import java.util.EventListener;

/**
 *
 * @author Robin Keskisärkkä
 */
public interface SpeakEventListener extends EventListener {
    public void speakStopOccured(SpeakEvent evt);
}

