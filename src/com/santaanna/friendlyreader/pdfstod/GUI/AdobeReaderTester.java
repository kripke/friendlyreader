/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.santaanna.friendlyreader.pdfstod.GUI;

import java.io.IOException;
import java.lang.Runtime;
import java.lang.Process;

/**
 *
 * @author Allan
 */
public class AdobeReaderTester {
    
    public static AdobeReaderTester art = null;
    // public Process proc = null;
    public Runtime rt = null;

    public AdobeReaderTester()
    {
        // proc = new Process();
        rt = Runtime.getRuntime();
    }

    /*
     public static void initiera()
    {
        // lokart = new AdobeReaderTester();
    }
     * 
     */

    public void execute(String execstring)
    {
        System.out.println("execute exekveras.");
        try
        {
            rt.exec(execstring);
        } catch (java.io.IOException jiio)
        {
            System.out.println("IOException in execute.");
        }
    }

    public void executefile(String file)
    {
        String execstring = "C:\\Program Files (x86)\\Adobe\\Reader 10.0\\Reader\\AcroRd32.exe ";
        execstring += file;
        System.out.println("executefile exekveras.");
        try
        {
            rt.exec( execstring );
        } catch (java.io.IOException jiio)
        {
            System.out.println("IOException in execute.");
        }
    }

    public static void main(String args[])
    {
        System.out.println("main exekveras.");
        art = new  AdobeReaderTester();
        // art.initiera();
        String pdffil = "AHPrepout.pdf"; // AHDocWordPDF2.pdf";
        String expath = "C:\\Program Files (x86)\\Adobe\\Reader 10.0\\Reader\\AcroRd32.exe ";
        // Om AcroRd32.exe finns i variabeln Path så behövs bara namnet.
        expath += pdffil;
        art.execute( expath );
        System.out.println("Slut på main.");
    }
}
