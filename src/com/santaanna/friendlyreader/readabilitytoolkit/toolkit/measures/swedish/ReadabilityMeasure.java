
package com.santaanna.friendlyreader.readabilitytoolkit.toolkit.measures.swedish;

/**
 * Abstract class for readability measures.
 * 
 * @author Christian
 */
public abstract class ReadabilityMeasure {
    
     /**
     * Evaluates a text, assuming one line per sentence and a single space
     * between words. 
     * 
     * @param text The text to evaluate
     * @return The value of the text
     */
    public abstract double evaluateText(String text);
    
}
