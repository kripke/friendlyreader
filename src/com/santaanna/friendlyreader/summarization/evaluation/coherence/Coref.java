
package com.santaanna.friendlyreader.summarization.evaluation.coherence;

import java.util.ArrayList;

/**
 *
 * @author Christian Smith2
 */
public class Coref {
        protected ArrayList<Integer> sentenceIndices = new ArrayList();
        protected ArrayList<Integer> uniqueSentenceIndices = new ArrayList();
        protected ArrayList<Integer> notLocalUniqueSentenceIndices = new ArrayList();
        
        protected final int head;
        
        Coref(int head){
            this.head = head;
            
        }
        
        public int getHead(){
            return head;
        }

    public ArrayList<Integer> getNotLocalUniqueSentenceIndices() {
        return notLocalUniqueSentenceIndices;
    }

    public ArrayList<Integer> getSentenceIndices() {
        return sentenceIndices;
    }

    public ArrayList<Integer> getUniqueSentenceIndices() {
        return uniqueSentenceIndices;
    }
        
        
        void addSentenceIndex(int i){
            this.sentenceIndices.add(i);
            
            if(!this.uniqueSentenceIndices.contains(i))
                this.uniqueSentenceIndices.add(i);
            
            if(i != head && !this.notLocalUniqueSentenceIndices.contains(i)){
                this.notLocalUniqueSentenceIndices.add(i);
            }
        }
}
