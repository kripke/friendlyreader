/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.santaanna.friendlyreader.pdfstod.CogsumApplet;

/**
 * En medlem i en COSArray kan vara en integer (float??).
 * Denna klass lagrar en sådan integer.
 * @author Allan
 */
public class SEArrayNumber extends SEArrayIndKlass{
    public int CArrayInt;

    public SEArrayNumber(int arrint)
    {
        CArrayInt = arrint;
    }
}
