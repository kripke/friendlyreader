package com.santaanna.friendlyreader.readabilitytoolkit;


import com.santaanna.friendlyreader.readabilitytoolkit.toolkit.utils.ReadabilityTool;

/**
 *
 * @author christian
 */
public class Main {
    
    public static void main(String[] args){
        
        // read file to string
//        String text = DocumentUtils.readFile("temp.txt");
        
        // analyze text
        ReadabilityTool rt = new ReadabilityTool("aksjbajkfbajkfas");
        
        // print something
        System.out.println("NR: " + rt.getNr());
        System.out.println("LIX: " + rt.getLix());
    }
}
