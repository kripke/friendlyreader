package com.santaanna.friendlyreader.engine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;


import com.santaanna.friendlyreader.text.Sentence;
import com.santaanna.friendlyreader.text.Sentences;
import com.santaanna.friendlyreader.text.Text;
import com.santaanna.friendlyreader.utils.POSTaggerHandler;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;

/**
 * Class for storing and accessing internal representations of text data.
 *
 * @author Johan Falkenjack, Christoffer Ohlsson, Robin Keskisärkkä
 * @version 1.2
 * @since 2012-09-28
 * @see Sentence, Sentences, Text
 */
public final class FriendlyReaderAPI {
    
    //Summary related variables
    /**
     * 
     */
    public SummaryReader reader;
    
    private Text text;
    private boolean initialized = false;
    //private boolean usePOStags = false;
    
    private POSTaggerHandler taggerHandler;
    
    /**
     * Creates a usable API with a set text. This is the version you want when you're actually doing something.
     * @param text The text that should be handled by the API.
     */
    public FriendlyReaderAPI(String text, Properties props) {
    	//System.out.println(text);
    	if (props.getProperty("usePOStags").equals("true")) {
    		this.taggerHandler = new POSTaggerHandler(props.getProperty("contextPath"));
    		this.text = new Text(text, taggerHandler);
    	} else {
    		this.text = new Text(text);
    	}
    	initialized = true;
    }
    
    //TODO: add "setText" method so that a new API object doesn't have to be created every time the text changes?
    //Might provide negligible benefits though, since it doesn't do any work or big allocations.
    /**
     * Creates a dummy API with no text. Useful for doing nothing, e.g. before the user loads a file.
     * Needs to be discarded and replaced with a FriendlyReaderAPI(String text) object to be given access to its functionality.
     * 
     */
    public FriendlyReaderAPI(){}
    
    /**
     * 
     * @param text
     */
    public void loadText(String text, Properties props){
    	if (props.getProperty("usePOStags").equals("true")) {
    		if (this.taggerHandler == null) {
    			this.taggerHandler = new POSTaggerHandler(props.getProperty("contextPath"));
    		}
    		this.text = new Text(text, this.taggerHandler);
    	}
        initialized = true;
    }
    
    /**
     * Gets a single Sentence object from the text, at a specified level of simplification
     * @param index The index of the Sentence to retrieve
     * @param level The level of simplification to use for the text
     * @return A Sentence object containing a single sentence, or null if no Text is set
     */
    public Sentence getSentence(int index){
        if (initialized)
            return text.getSentence(index);
        else
            return null;
    }
    
    /**
     * Gets the Sentences object for the current Text, at a specified level of simplification
     * @param level The level of simplification to use for the text
     * @return A Sentences object containing the Text, or null if no Text is set
     */
    public Sentences getSentences(){
    	System.out.println("In getSentences");
        if (initialized) {
        	for (Sentence sentence : text.getSentences().getSentences()) {
        		System.out.println("rank: " + sentence.getRank());
        	}
            return text.getSentences();
        }
        else {
            return null;
        }
    }
    
    /**
     * Gets the number of Sentence objects for the original Text
     * @return the number of Sentence objects for the original Text, or -1 if there's no Text
     */
    public int getNumberOfSentences(){
        if (initialized)
            return text.getOriginalLength();
        else
            return -1;
    }
    
    /**
     * Returns an ArrayList of Integers containing the indexes of Sentence objects to get for displaying a text at a certain percentage of summarization.
     * @param percentageToGet The percentage to which the text should be summarized when retrieving the indexes
     * @return An ArrayList of Integers containing the indexes of a set of sentences, or an empty ArrayList when there's no Text to summarize
     */
    public ArrayList<Integer> getSumIndexes(double percentageToGet) {
        if (initialized)
            return text.getSumIndexes(percentageToGet);
        else
            return new ArrayList<Integer>();
    }
    
    public String getWorkingDirectory(){
    	File file = new File(".");
		return file.getAbsolutePath();
    }
    
}
