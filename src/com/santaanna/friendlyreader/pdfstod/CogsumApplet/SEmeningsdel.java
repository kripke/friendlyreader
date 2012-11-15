/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.santaanna.friendlyreader.pdfstod.CogsumApplet;

import java.util.Vector;

/**
 *
 * @author Allan
 */
public class SEmeningsdel {
    public int pagenr; // Sida på vilken meningensdelen finns på.
    public int tb; // Textblock i vilket meningensdelen finns i.
    public boolean isarr; // Finns delen i en COSArray?
    public int arrind; // Om COSArray, vilket är indexet?
    //>>> En meningsdel skall motsvara en hel array efter omstrukturering.
    // arrind skall alltså inte behövas!
    // public int cstrdelindex; // index i cstr strukturen. Behövs inte
    // eftersom det alltid är hela strings för meningen!
    public String deltext; // Själva deltexten av meningen.

    // Vector<meningsdel> allaDelMeningar = new Vector<meningsdel>();
}
