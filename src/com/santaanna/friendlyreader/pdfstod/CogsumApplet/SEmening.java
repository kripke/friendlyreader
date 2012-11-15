/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.santaanna.friendlyreader.pdfstod.CogsumApplet;

import java.util.Vector;

/**
 * Varje meningsdel är ett fragment av en mening.
 * Meningsdelarna sparas i en vector som bildar hela meningen!
 * @author Allan
 */

public class SEmening {
    public String helameningen; // Den kompletta texten i meningen.
    public Vector<SEmeningsdel> allaDelar = new Vector<SEmeningsdel>();
    // Alla delar av meningen med positionsinfo i strukturen.
}
