

package com.santaanna.friendlyreader.readabilitytoolkit.toolkit.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kricke
 */
public class GranskaReader{

    BufferedReader reader;
    
    private ArrayList<String> word = new ArrayList();
    private ArrayList<String> uword = new ArrayList();
    private ArrayList<String> lemma = new ArrayList();
    private ArrayList<String> ulemma = new ArrayList();
    private ArrayList<String> pos = new ArrayList();

    public GranskaReader(String file) throws UnsupportedEncodingException{
        try {

            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
            read();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GranskaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GranskaReader(Reader in){
        reader = new BufferedReader(in);
        read();
    }

    public ArrayList<String> getLemma() {
        return lemma;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public ArrayList<String> getUword() {
        return uword;
    }
    public ArrayList<String> getUlemma() {
        return ulemma;
    }
    public ArrayList<String> getWord() {
        return word;
    }
    
    private String read(){
        
        String line = null;
        
        try {
            while ((line = reader.readLine()) != null){

                if(line == null)
                    return line;

                line = line.replaceAll("\\[.*\\]~*", "");
                line = line.replaceAll("\\s+", "\t");

                String[] spl = line.split("\t");

                if(spl.length > 2){
                    String w = line.split("\t")[0];
                    String l = line.split("\t")[2];
                    String p = line.split("\t")[1];
                    p = p.split("\\.")[0];

                    word.add(w);

                    if(!uword.contains(w))
                        uword.add(w);
                    if(!ulemma.contains(l))
                        ulemma.add(l);
                    lemma.add(l);

                    pos.add(p);
                    
                }
                else{

                    System.err.println("ERROR IN READING GRANSKA: " + line);
                }

            }
//            System.out.println("WOOOOOORDS");
//            System.out.println(word);
            
            reader.close();

            return line;
            
        } catch (IOException ex) {
            Logger.getLogger(GranskaReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return line;
    }

    @Override
    public String toString(){
        StringBuilder str= new StringBuilder();
        return str.append("Words: ").append(word).append("\nLemma: ")
                .append(lemma).append("\nPOS: ").append(pos).toString();

    }





}
