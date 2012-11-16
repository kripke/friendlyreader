
package toolkit.measures.swedish;

import java.util.HashMap;
import toolkit.utils.DocumentUtils;

/**
 * LIX = (number of words / number of sentences) + 
 * 
 * (number of words > 6 chars / number of words) * 100
 *
 * 
 * @author Christian Smith
 */
public class LIX extends ReadabilityMeasure{
    private double numWords;
    private double numLongWords;

    
    /**
     * Evaluates a text, assuming one line per sentence and a single space
     * between words. 
     * 
     * @param text The text to evaluate
     * @return The LIX value of the text
     */
    @Override
    public double evaluateText(String text){
        
        numWords = 0;
        numLongWords = 0;
        HashMap uniqueWords = new HashMap();
        
        String[] sents = text.split("\n");
        
        double numSents = sents.length;
        
        for(String sentence : sents){
            
            // retain only words
            String currSentence = sentence.replaceAll("[^A-ZÅÄÖa-zåäö ]", "");
            String[] words = currSentence.split(" ");
            
            // count words
            numWords += words.length;
            
            // count long words
            for(String currW : words){
                uniqueWords.put(currW, currW);
                numLongWords += (currW.length() > 6) ? 1 : 0;

            }
            //numWords = uniqueWords.size();
            
        }
        
        double lix = (numWords / numSents) + ((numLongWords / numWords)  * 100d);

        
        return lix;
    }
    
    public static void main(String[] args){
        
        LIX lix = new LIX();
        String text = DocumentUtils.readFile("/home/christian/Desktop/Texts/DN/2.txt");

        double d = lix.evaluateText(text);
        
        System.out.println(d);
        
    }

    public double getNumOfLongWords() {
        return this.numLongWords;
    }
    

}
