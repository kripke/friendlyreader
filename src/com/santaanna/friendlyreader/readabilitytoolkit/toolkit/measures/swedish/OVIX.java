
package toolkit.measures.swedish;

import java.util.HashMap;
import toolkit.utils.DocumentUtils;

/**
 *
 * @author christian
 */
public class OVIX extends ReadabilityMeasure{
    
    
    /**
     * Evaluates a text, assuming one line per sentence and a single space
     * between words. 
     * 
     * @param text The text to evaluate
     * @return The OVIX value of the text
     */
    @Override
    public double evaluateText(String text){
        
        // holds all unique words
        HashMap uniqueWords = new HashMap();
        
        double numWords = 0;
        double numUniqueWords = 0;
        
        String[] sents = text.split("\n");
        
        for(String sentence : sents){
            
            // retain only words
            String currSentence = sentence.replaceAll("[^A-ZÅÄÖa-zåäö ]", "");
            String[] words = currSentence.split(" ");
            
            for(String w : words){
                uniqueWords.put(w, new Object());
            }
            
            // count words
            numWords += words.length;
        }
        
        numUniqueWords = uniqueWords.size();
        
        double ovix = Math.log(numWords) / 
                Math.log(2 - (Math.log(numUniqueWords) / Math.log(numWords)));

        return ovix;
        
    }
    

    public static void main(String[] args){

        OVIX ovix = new OVIX();
        String text = DocumentUtils.readFile(args[0]);

        double d = ovix.evaluateText(text);
        
        System.out.println(d);
        
    }
}
