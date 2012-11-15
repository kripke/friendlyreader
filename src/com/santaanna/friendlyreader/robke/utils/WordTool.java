/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santaanna.friendlyreader.robke.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.santaanna.friendlyreader.robke.utils.etStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Robin Keskisärkkä
 */
public class WordTool {

    private HashMap map = new HashMap();
    private String path = "./resource/synonyms.xml";

    public WordTool() {
        //Load map
        loadSynonyms();
    }

    /**
     * Return all synonyms in tuple form of a specific word, ordered by synonym
     * strength.
     *
     * @param word
     * @return
     */
    public ArrayList<String> getSynonyms(String word) {
        return getSynonyms(word, -1);
    }

    /**
     * Return synonyms for a specific word. Limit sets the maximum amount of
     * synonym tuples returned. The synonyms are ordered by synonym strength.
     * Returns null if the no synonyms are found.
     *
     * @param word
     * @param limit
     * @return
     */
    public ArrayList<String> getSynonyms(String word, int limit) {
        word = word.trim().toLowerCase();
        //Try to use granska to get lemma
        String[] info = getWordInfo(word);
        String lemma = info[info.length-1];
        
        if (!map.containsKey(lemma)) {
            return null;
        }

        //Sort the synonyms according to frequency
        ArrayList<Tuple> synonyms = (ArrayList<Tuple>) map.get(lemma);
        if (synonyms != null) {
            TupleComparator comparator = new TupleComparator();
            Collections.sort(synonyms, comparator);
        }

        ArrayList<String> list = new ArrayList<String>();
        list.add(lemma);
        int i = 0;
        while (i < synonyms.size() && i < limit) {
            list.add((String)synonyms.get(i).T2);
            i++;
        }
        return list;
    }

    /**
     * Loads the file containing synonyms into a hash map where each word has an
     * array containing its synonyms as value.
     */
    private void loadSynonyms() {
        try {
            map = new HashMap();
            File file = new File(path);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList listOfWordPairs = doc.getElementsByTagName("syn");

            for (int s = 0; s < listOfWordPairs.getLength(); s++) {
                Node entryNode = listOfWordPairs.item(s);
                Element entryElement = (Element) entryNode;

                //Get word 1
                NodeList wordList = entryElement.getElementsByTagName("w1");
                Element wordElement = (Element) wordList.item(0);
                NodeList wordElementList = wordElement.getChildNodes();
                String word1 = ((Node) wordElementList.item(0)).getNodeValue();

                //Get word 2
                wordList = entryElement.getElementsByTagName("w2");
                wordElement = (Element) wordList.item(0);
                wordElementList = wordElement.getChildNodes();
                String word2 = ((Node) wordElementList.item(0)).getNodeValue();

                String level = entryElement.getAttribute("level");
                Tuple t = new Tuple(level, word2);

                if (map.containsKey(word1)) {
                    ArrayList<Tuple> set = (ArrayList<Tuple>) map.get(word1);
                    set.add(t);
                } else {
                    ArrayList<Tuple> set = new ArrayList<Tuple>();
                    map.put(word1, set);
                }
            }
            System.out.println("This is the end of loading fused xml. Has size " + map.size());
        } catch (SAXException ex) {
            Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Uses granska to find the lemma of the passed word. If an error occurs or
     * granska is not available nul is returned
     * @param lemma
     * @return 
     */
    public String[] getWordInfo(String word) {
        PrintWriter writer = null;
        try {
            File f = new File("./data/temp");
            String filePath = f.getCanonicalPath();
            writer = new PrintWriter(filePath,"iso-8859-1");
            writer.write(word);
            writer.close();
            
            //Run granska on the file
            String cmd = "lib\\Granska\\tagg.exe " + "-l "
                    + "lib\\Granska\\lex "  + "-B " + filePath;
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("win")) {
                cmd = "wine " + cmd;
            }
            Runtime r = Runtime.getRuntime();
            try {
                //FileOutputStream fos = new FileOutputStream(this.outputfilename);
                PrintWriter fos = new PrintWriter(filePath + "Granska", "iso-8859-1");
                Process p = r.exec(cmd);

                etStreamWriter errstream = new etStreamWriter(p.getErrorStream(),
                        new PrintWriter(System.out));
                etStreamWriter ostream = new etStreamWriter(p.getInputStream(),
                        fos);
                errstream.start();
                ostream.start();
                
                int exitVal = 0;
                try {
                    exitVal = p.waitFor();
                } catch (InterruptedException ex) {
                    //Pass
                }
                fos.close();

            } catch (IOException ex) {
                System.err.println("Failed to save postags");
            }
            
            //Read the file back, should be read as a stream directly but it
            //doesn't work completely, rather it freezes (to be fixed)
            Scanner sc = new Scanner(new FileInputStream(filePath + "Granska"),
                    "iso-8859-1");
            String text = "";
            while(sc.hasNext()){
                text += sc.nextLine();
            }
            System.out.println(text);
            return text.split("\t");

        } catch (IOException ex) {
            Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Convert stream to string
     * @param is
     * @return 
     */
    private String convertStreamToString(InputStream is) {
        //Doesnt work right now
        try {
            return new Scanner(is, "iso-8859-1").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}
