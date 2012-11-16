
package com.santaanna.friendlyreader.readabilitytoolkit.toolkit.utils;

import com.santaanna.friendlyreader.readabilitytoolkit.toolkit.measures.swedish.LIX;
import com.santaanna.friendlyreader.readabilitytoolkit.toolkit.measures.swedish.NR;
import com.santaanna.friendlyreader.readabilitytoolkit.toolkit.measures.swedish.OVIX;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import toolkit.utils.GranskaWriter;

/**
 * Complete wrapper for various readability indices. LIX, NR, OVIX plus some other
 * shallow statistics (number of words, sentence length etc...)
 * 
 * @author Christian
 */
public class ReadabilityTool {


    double notokens = 0;
    double uniquewords = 0;
    double avgwordlength = 0;
    double nosentences = 0;
    double avgsentencelength = 0;
    double extralongwordratio = 0;
    double longwordratio = 0;
    double propernounratio = 0;
    double lix = 0;
    double nr = 0;
    double ovix = 0;
    
    /**
     * All evaluations are performed upon creation. A text with one line per 
     * sentence and with a single space between words is expected.
     * 
     * @param text The text to analyze.
     */
    public ReadabilityTool(String text){
        process(text);
    }

    /**
     * 
     * @return Average sentence length
     */
    public double getAvgsentencelength() {
        return avgsentencelength;
    }

    /**
     * 
     * @return Average word length
     */
    public double getAvgwordlength() {
        return avgwordlength;
    }

    /**
     * 
     * @return Ratio of extra long words (> 14 chars)
     */
    public double getExtralongwordRatio() {
        return extralongwordratio;
    }

    /**
     * 
     * @return Long word ratio (> 6 chars)
     */
    public double getLongwordRatio() {
        return longwordratio;
    }

    /**
     * 
     * @return Number of sentences
     */
    public double getNosentences() {
        return nosentences;
    }

    /**
     * 
     * @return Number of tokens
     */
    public double getNotokens() {
        return notokens;
    }

    /**
     * 
     * @return Ratio of proper nouns
     */
    public double getProperNounRatio() {
        return propernounratio;
    }

    /**
     * 
     * @return Number of unique words
     */
    public double getUniquewords() {
        return uniquewords;
    }

    /**
     * 
     * @return LIX-value
     */
    public double getLix() {
        return lix;
    }

    /**
     * 
     * @return Nominal Ratio-value
     */
    public double getNr() {
        return nr;
    }

    /**
     * 
     * @return OVIX-value
     */
    public double getOvix() {
        return ovix;
    }

    
    /**
     * Processes a text and calculates all indices. Called at constructor.
     * @param text 
     */
    private void process(String text){
        try {
            // tag
            GranskaWriter.write(text,"tagged");
            GranskaReader gr = new GranskaReader("tagged");
            
            LIX Lix = new LIX();
            lix = Lix.evaluateText(text);
            
            NR Nr = new NR(gr);
            nr = Nr.evaluateText(text);
            
            OVIX Ovix = new OVIX();
            ovix = Ovix.evaluateText(text);
            
            notokens = gr.getWord().size();
            uniquewords = gr.getUword().size();

            double sum = 0;
            for(String s : gr.getWord()){
                sum +=s.length();
            }
            sum /= (double)gr.getWord().size();

            avgwordlength = sum;
            nosentences =  text.split("\n").length;
            avgsentencelength = gr.getWord().size() / nosentences;

            sum = 0;
            for(String lw : gr.getWord()){
                if(lw.length() > 14)
                    sum++;
                }
            sum /= (double)gr.getWord().size();
            extralongwordratio = sum;

            longwordratio = Lix.getNumOfLongWords() / (double)gr.getWord().size();

            sum=0;
            for(String lw : gr.getPos()){
                if(lw.equals("pm"))
                    sum++;
            }
            propernounratio = (double)sum/gr.getWord().size();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadabilityTool.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
