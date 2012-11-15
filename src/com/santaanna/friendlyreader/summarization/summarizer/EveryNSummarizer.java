
package com.santaanna.friendlyreader.summarization.summarizer;

import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.Settings;


/**
 * A summarizer creating summaries based on every n:th sentence.
 * 
 * Useful for creating baseline texts for comparisons.
 * 
 * new EveryNSummarizer(3) for instance creates a summary consisting of
 * every third sentence, possibly starting at a given index, as in 
 * new EveryNSummarizer(3, 3), which would build a summary consisting of every
 * third sentence, beginning on the third sentence, thus not index 3 (fourth).
 * @author Christian Smith, christian.smith@liu.se
 */
public class EveryNSummarizer extends Summarizer{
    
    /*N*/
    private final int N;
    
    /* Starting sentence (index + 1)*/
    private final int start;

    public EveryNSummarizer(int N){
        this(N,1);
        
    }
    
    /**
     * Create an EveryNSummarizer!
     * 
     * @param N Every N:th sentence...
     * @param start Where to start in the document (what sentence index)
     * @param s The Settings to be used when creating summaries. Ignored
     */
    public EveryNSummarizer(int N, int start){
        super(new Settings());
        
        this.N = N;
        this.start = start;
        
    }
    
    /**
     * Rank sentences based on their position.
     * 
     * @return the rank.
     */
    @Override
    protected Summary rank() {
        int numSent = this.document.getSentences().size();
        
        float[] ranks = new float[numSent];
        
        for(int i = this.start; i <= numSent; i++){
            if((i - this.start) % N == 0)
                ranks[i-1] = numSent - i;
            
            else
                ranks[i-1] = -numSent;
        }
        
        Summary sum = new Summary();
        sum.setSentenceRanks(ranks);
        sum.setSentences(document.getSentences());
        
        
        return sum;
        
    }
    
}
